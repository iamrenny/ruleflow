package com.rappi.fraud.rules.services

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.Grafana
import com.rappi.fraud.rules.documentdb.DocumentDbDataRepository
import com.rappi.fraud.rules.documentdb.EventData
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.NoRiskDetailDataWasFound
import com.rappi.fraud.rules.entities.RiskDetail
import com.rappi.fraud.rules.entities.RulesEngineHistoryRequest
import com.rappi.fraud.rules.entities.RulesEngineOrderListHistoryRequest
import com.rappi.fraud.rules.entities.UnlockWorkflowEditionRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowEditionResponse
import com.rappi.fraud.rules.exceptions.BadRequestException
import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.errors.ErrorRequestException
import com.rappi.fraud.rules.parser.vo.WorkflowInfo
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.ListRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.micrometer.core.instrument.Tag
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.micrometer.backends.BackendRegistries
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class WorkflowService @Inject constructor(
    private val activeWorkflowRepository: ActiveWorkflowRepository,
    private val activeWorkflowHistoryRepository: ActiveWorkflowHistoryRepository,
    private val workflowRepository: WorkflowRepository,
    private val listRepository: ListRepository,
    private val workFlowEditionService: WorkflowEditionService,
    private val documentDbDataRepository: DocumentDbDataRepository
) {

    private val logger by LoggerDelegate()

    fun save(request: CreateWorkflowRequest): Single<Workflow> {
        val workflow = Workflow(
                countryCode = request.countryCode,
                name = WorkflowEvaluator(request.workflow).validateAndGetWorkflowName(),
                workflowAsString = request.workflow,
                userId = request.userId
        )

        return workflowRepository.exists(workflow.countryCode!!, workflow.name).flatMap { exists ->
            if (exists) {
                workFlowEditionService.getUserEditing(workflow.countryCode, workflow.name).flatMap { userEditing ->
                    if (userEditing != "NOT FOUND") {
                        if (userEditing == workflow.userId) {
                            workFlowEditionService.cancelWorkflowEditing(workflow.countryCode, workflow.name)
                                .flatMap {
                                    workflowRepository.save(workflow)
                                }
                        } else {
                            throw ErrorRequestException("the workflow is being edited by $userEditing", "workflow.is.being.edited", 423)
                        }
                    } else {
                        throw ErrorRequestException("no active workflow edition to save", "workflow.edition.not.active", 400)
                    }
                }
            } else {
                workflowRepository.save(workflow)
            }
        }
    }

    fun getWorkflow(countryCode: String, name: String, version: Long): Single<Workflow> {
        return workflowRepository.getWorkflow(countryCode, name, version)
    }

    fun getWorkflowsByCountryAndName(request: GetAllWorkflowRequest): Observable<Workflow> {
        return workflowRepository.getWorkflowsByCountryAndName(request)
    }

    fun getActiveWorkflowsByCountry(countryCode: String): Observable<Workflow> {
        return workflowRepository.getActiveWorkflowsByCountry(countryCode)
    }

    fun getAllWorkflowsByCountry(countryCode: String): Observable<Workflow> {
        return workflowRepository.getAllWorkflowsByCountry(countryCode)
    }

    fun evaluate(countryCode: String, name: String, version: Long? = null, data: JsonObject, isSimulation: Boolean? = false): Single<WorkflowResult> {
        val startTimeInMillis = System.currentTimeMillis()
        return getWorkflow(countryCode, name, version)
            .flatMap { workflow ->
                listRepository.findAll()
                    .map { lists ->
                        workflow.evaluator.evaluate(data.map, lists)
                    }
                    .flatMap { result ->
                        if (isSimulation!!) {
                            Single.just(result.copy(
                                workflowInfo = WorkflowInfo(workflow.version?.toString() ?: "active", workflow.name)
                            ))
                        } else {
                            saveDataInDocDBandReturnWorkflowResult(data, result, workflow)
                        }
                    }
                    .doOnSuccess { result ->
                        result.warnings.forEach { warning ->
                            BackendRegistries.getDefaultNow().counter(
                                "fraud.rules.engine.workflows.warnings",
                                listOf(
                                    Tag.of("workflow", result.workflow),
                                    Tag.of("workflow_country", workflow.countryCode!!),
                                    Tag.of("workflow_version", workflow.version?.toString() ?: "active"),
                                    Tag.of("warning", warning)
                                )
                            ).increment()
                        }
                    }
            }
            .doAfterTerminate {
                BackendRegistries.getDefaultNow().timer("fraud.rules.engine.workflowService.evaluate",
                    "countryCode", countryCode,
                    "workflow", name)
                    .record(System.currentTimeMillis() - startTimeInMillis, TimeUnit.MILLISECONDS)
            }
    }

    private fun getWorkflow(
        countryCode: String,
        name: String,
        version: Long?
    ) = if (version == null)
        activeWorkflowRepository.get(countryCode, name)
    else
        workflowRepository.getWorkflow(countryCode, name, version)

    fun activate(request: ActivateRequest): Single<Workflow> {
        return workflowRepository
                .getWorkflow(request.countryCode, request.name, request.version)
                .flatMap { toActivate ->
                    activeWorkflowRepository
                        .save(toActivate)
                }
                .doOnSuccess {
                    saveHistory(it, request)
                }
                .doOnError {
                    logger.error("Activate of workflow $request could not be completed", it)
                    Grafana.noticeError(it)
                }
    }

    private fun saveHistory(workflow: Workflow, request: ActivateRequest) {
        activeWorkflowHistoryRepository
            .save(
                ActiveWorkflowHistory(
                    workflowId = workflow.id!!,
                    userId = request.userId
                )
            ).subscribe({}, { it ->
                logger.error("Activate of workflow ${workflow.id} could not be saved in the history", it)
                Grafana.noticeError(it)
            })
    }

    fun getWorkflowForEdition(countryCode: String, workflowName: String, version: Long, user: String): Single<WorkflowEditionResponse> {
        return workflowRepository.getWorkflow(countryCode, workflowName, version).flatMap { workflow ->
            workFlowEditionService.lockWorkflowEdition(countryCode, workflowName, user).map { workflowEditionStatus ->
                if (workflowEditionStatus.status == "OK")
                    WorkflowEditionResponse(
                        workflow,
                        workflowEditionStatus
                    )
                else
                    WorkflowEditionResponse(
                        workflowEditionStatus = workflowEditionStatus
                    )
                }
        }
    }

    fun cancelWorkflowEdition(request: UnlockWorkflowEditionRequest): Single<WorkflowEditionService.WorkflowEditionStatus> {
        return workFlowEditionService.cancelWorkflowEdition(request.countryCode, request.workflowName, request.user)
    }

    private fun saveDataInDocDBandReturnWorkflowResult(
        data: JsonObject,
        result: WorkflowResult,
        workflow: Workflow
    ): Single<WorkflowResult> {
        val eventData = EventData(
            request = data,
            response = JsonObject.mapFrom(result),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = workflow.countryCode!!,
            workflowName = workflow.name
        )
        return documentDbDataRepository.saveEventData(eventData).map {
            result.copy(
                requestId = it.id,
                workflowInfo = WorkflowInfo(workflow.version?.toString() ?: "active", workflow.name)
            )
        }
    }

    fun getRequestIdData(requestId: String): Single<EventData> {
        return documentDbDataRepository.find(requestId).onErrorResumeNext {
            if (it is DocumentDbDataRepository.NoRequestIdDataWasFound) {
                Single.error(BadRequestException("$requestId was not found", "bad.request"))
            } else {
                Single.error(it)
            }
        }
    }

    fun getEvaluationHistory(request: RulesEngineHistoryRequest): Single<List<RiskDetail>> {
        return documentDbDataRepository.getRiskDetailHistoryFromDocDb(request)
            .onErrorResumeNext {
                if (it is NoRiskDetailDataWasFound) {
                    Single.error(BadRequestException("$request was not found", "bad.request"))
                } else {
                    Single.error(it)
                }
            }
    }

    fun getEvaluationOrderListHistory(request: RulesEngineOrderListHistoryRequest): Single<List<RiskDetail>> {
        return documentDbDataRepository.findInList(request.orders)
            .onErrorResumeNext {
                if (it is NoRiskDetailDataWasFound) {
                    Single.error(BadRequestException("$request was not found", "bad.request"))
                } else {
                    Single.error(it)
                }
            }
    }
}
