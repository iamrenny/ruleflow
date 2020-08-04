package com.rappi.fraud.rules.services

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.GetListOfAllWorkflowsRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowInfo
import com.rappi.fraud.rules.parser.WorkflowEvaluator
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
    private val listRepository: ListRepository
) {

    private val logger by LoggerDelegate()

    fun save(request: CreateWorkflowRequest): Single<Workflow> {
        val workflow = Workflow(
                countryCode = request.countryCode,
                name = WorkflowEvaluator(request.workflow).validateAndGetWorkflowName(),
                workflowAsString = request.workflow,
                userId = request.userId
        )
        return workflowRepository.save(workflow)
    }

    fun get(countryCode: String, name: String, version: Long): Single<Workflow> {
        return workflowRepository.get(countryCode, name, version)
    }

    fun getAll(request: GetAllWorkflowRequest): Observable<Workflow> {
        return workflowRepository.getAll(request)
    }

    fun listAllWorkflows(request: GetListOfAllWorkflowsRequest): Observable<WorkflowInfo> {
        return workflowRepository.getListOfAllWorkflows(request)
    }

    fun evaluate(countryCode: String, name: String, version: Long? = null, data: JsonObject): Single<WorkflowResult> {
        val startTimeInMillis = System.currentTimeMillis()
        return getWorkflow(countryCode, name, version)
            .flatMap { workflow ->
                listRepository.findAllWithEntries()
                    .flatMap { lists ->
                        Single.just(workflow.evaluator.evaluate(data.map, lists))
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
    ) = workflowCache.get(countryCode, name, version)
        .switchIfEmpty(Single.defer {
            if (version == null) {
                fromDbWithoutVersion(countryCode, name)
            } else {
                fromDbWithVersion(countryCode, name, version)
            }
        })

    private fun fromDb(countryCode: String, name: String, version: Long): Single<Workflow> {
        logger.info("Getting value in db for $countryCode $name $version")
        return fromDbWithVersion(countryCode, name, version)
    }

    private fun fromDbWithoutVersion(countryCode: String, name: String) =
            activeWorkflowRepository
                    .get(countryCode, name)
                .flatMap {
                    workflowCache.set(it)
                }

    private fun fromDbWithVersion(countryCode: String, name: String, version: Long) =
            workflowRepository.get(countryCode, name, version)
                .flatMap {
                    workflowCache.set(it)
                }

    fun activate(request: ActivateRequest): Single<Workflow> {
        return workflowRepository
                .get(request.countryCode, request.name, request.version)
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
}
