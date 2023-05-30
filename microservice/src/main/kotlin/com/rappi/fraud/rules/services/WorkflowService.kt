package com.rappi.fraud.rules.services

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.Grafana
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.documentdb.DocumentDbDataRepository
import com.rappi.fraud.rules.documentdb.WorkflowResponse
import com.rappi.fraud.rules.entities.Action
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.RiskDetail
import com.rappi.fraud.rules.entities.RulesEngineHistoryRequest
import com.rappi.fraud.rules.entities.RulesEngineOrderListHistoryRequest
import com.rappi.fraud.rules.entities.UnlockWorkflowEditionRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowVersion
import com.rappi.fraud.rules.entities.WorkflowEditionResponse
import com.rappi.fraud.rules.entities.WorkflowInfo
import com.rappi.fraud.rules.entities.WorkflowResult
import com.rappi.fraud.rules.entities.GetVersionRequest
import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.errors.ErrorRequestException
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.ListRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.*
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime
import org.bson.types.ObjectId

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
                            throw ErrorRequestException(
                                "the workflow is being edited by $userEditing",
                                "workflow.is.being.edited",
                                423
                            )
                        }
                    } else {
                        throw ErrorRequestException(
                            "no active workflow edition to save",
                            "workflow.edition.not.active",
                            400
                        )
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

    fun getTheLastWorkflowVersions(request: GetVersionRequest): Observable<WorkflowVersion> {
        return workflowRepository.getTheLastWorkflowVersions(request)
    }

    fun evaluate(
        countryCode: String,
        name: String,
        version: Long? = null,
        data: JsonObject,
        isSimulation: Boolean = false
    ): Single<WorkflowResult> {
        return getWorkflow(countryCode, name, version)
            .map { workflow ->
                val result = workflow.evaluator.evaluate(data.map, listRepository.getCached())

                WorkflowResult(
                    requestId = ObjectId.get().toHexString(),
                    workflowInfo = WorkflowInfo(workflow.countryCode!!, workflow.version.toString(), workflow.name),
                    workflow = name,
                    rule = result.rule,
                    ruleSet = result.ruleSet,
                    risk = result.risk,
                    actionsWithParams = result.actionsWithParams,
                    actions = result.actions,
                    warnings = result.warnings,
                    actionsList = result.actionsList.map { action -> Action(action.name, action.params) },
                    error = result.error
                )
            }
            .doOnSuccess { result ->
                if (!isSimulation) {
                    val workflowResponse = WorkflowResponse(
                        id = result.requestId,
                        request = data,
                        response = JsonObject.mapFrom(result),
                        receivedAt = LocalDateTime.now().toString(),
                        countryCode = countryCode,
                        workflowName = name
                    )

                    Grafana.warn(result.warnings, result.workflow, version?.toString() ?: "active", countryCode)

                    documentDbDataRepository.save(workflowResponse)
                        .subscribe({}, {
                            SignalFx.noticeError("Could not save workflow event to to collection in ${workflowResponse.countryCode}", it)
                            logger.error("Could not save workflow event for ${workflowResponse.id} to collection in ${workflowResponse.countryCode}", it)
                        })
                }
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

    fun getWorkflowForEdition(
        countryCode: String,
        workflowName: String,
        version: Long,
        user: String
    ): Single<WorkflowEditionResponse> {
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

    fun getRequestIdData(requestId: String): Maybe<WorkflowResponse> {
        return  Maybe.concat(
            documentDbDataRepository.find(requestId),
            documentDbDataRepository.find(requestId,true)
        ).firstElement()
    }

    fun getRequestIdData(country: String, requestId: String): Maybe<WorkflowResponse> {
        return documentDbDataRepository.find(country, requestId)
    }

    fun getEvaluationHistory(request: RulesEngineHistoryRequest): Single<List<RiskDetail>> {
        return documentDbDataRepository.getRiskDetailHistoryFromDocDb(request, false)
    }

    fun getEvaluationOrderListHistory(request: RulesEngineOrderListHistoryRequest): Single<List<RiskDetail>> {
        return documentDbDataRepository.findInList(request.orders, request.workflowName, request.countryCode, false)
    }

    fun deleteDocumentsHistory(): Completable{
        return documentDbDataRepository.removeBatchDocDb()
    }
}
