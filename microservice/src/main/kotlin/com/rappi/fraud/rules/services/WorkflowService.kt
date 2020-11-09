package com.rappi.fraud.rules.services

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
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
import java.util.concurrent.TimeUnit

class WorkflowService @Inject constructor(
    private val activeWorkflowRepository: ActiveWorkflowRepository,
    private val activeWorkflowHistoryRepository: ActiveWorkflowHistoryRepository,
    private val workflowCache: WorkflowCache,
    private val workflowRepository: WorkflowRepository,
    private val listRepository: ListRepository,
    private val workFlowEditionService: WorkflowEditionService
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
                        if(userEditing == workflow.userId){
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

    fun evaluate(countryCode: String, name: String, version: Long? = null, data: JsonObject): Single<WorkflowResult> {
        val startTimeInMillis = System.currentTimeMillis()
        return getWorkflow(countryCode, name, version)
            .flatMap { workflow ->
                listRepository.findAll()
                    .map { lists ->
                        workflow.evaluator.evaluate(data.map, lists)
                    }
                    .map {  result ->
                        result.copy(workflowInfo = WorkflowInfo(workflow.version?.toString() ?: "active", workflow.name))
                    }
                    .doOnSuccess { result ->
                        result.warnings.forEach {warning ->
                            BackendRegistries.getDefaultNow().counter(
                                "fraud.rules.engine.workflows.warnings",
                                listOf(
                                    Tag.of("workflow", result.workflow),
                                    Tag.of("workflow_country", workflow.countryCode),
                                    Tag.of("workflow_version", workflow.version?.toString() ?: "active") ,
                                    Tag.of("warning", warning)
                                )
                            ).increment()
                        }
                    }
            }
            .doOnError {
                "Workflow not active for key: $countryCode $name $version".let {
                    logger.error(it)
                    SignalFx.noticeError(it)
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
                            .flatMap {
                                Single.just(it)
                            }
                }
                .doOnSuccess {
                    saveHistory(it, request)
                    saveActiveInCache(it)
                }
                .doOnError {
                    logger.error("Activate of workflow $request could not be completed", it)
                    SignalFx.noticeError(it)
                }
    }

    private fun saveActiveInCache(workflow: Workflow) {
        workflowCache
                .set(workflow)
                .doOnError {
                    logger.error("Workflow ${workflow.id} could not be saved in cache", it)
                    SignalFx.noticeError(it)
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
                SignalFx.noticeError(it)
        })
    }

    fun getWorkflowForEdition(countryCode: String, workflowName: String, version: Long, user: String): Single<WorkflowEditionResponse> {
        return workflowRepository.getWorkflow(countryCode, workflowName, version).flatMap { workflow ->
            workFlowEditionService.lockWorkflowEdition(countryCode, workflowName, user).map { workflowEditionStatus ->
                if(workflowEditionStatus.status == "OK")
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

    fun cancelWorkflowEdition(request: UnlockWorkflowEditionRequest): Single<WorkflowEditionService.WorkflowEditionStatus>{
        return workFlowEditionService.cancelWorkflowEdition(request.countryCode, request.workflowName, request.user)
    }

    data class WorkflowEditionResponse(
        val workflow: Workflow? = null,
        val workflowEditionStatus: WorkflowEditionService.WorkflowEditionStatus
    )

    data class LockWorkflowEditionRequest(
        val countryCode: String,
        val workflowName: String,
        val version: Long,
        val user: String
    )

    data class UnlockWorkflowEditionRequest(
        val countryCode: String,
        val workflowName: String,
        val user: String
    )
}
