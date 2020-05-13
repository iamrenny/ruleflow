package com.rappi.fraud.rules.services

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveKey
import com.rappi.fraud.rules.entities.ActiveWorkflow
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.GetListOfAllWorkflowsRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.entities.WorkflowInfo
import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.parser.errors.NotFoundException
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject

class WorkflowService @Inject constructor(
    private val activeWorkflowRepository: ActiveWorkflowRepository,
    private val activeWorkflowHistoryRepository: ActiveWorkflowHistoryRepository,
    private val cacheService: CacheService,
    private val workflowRepository: WorkflowRepository
) {

    private val logger by LoggerDelegate()

    fun save(request: CreateWorkflowRequest): Single<Workflow> {
        val workflow = Workflow(
                countryCode = request.countryCode,
                name = RuleEngine(request.workflow).validateAndGetWorkflowName(),
                workflow = request.workflow,
                userId = request.userId
        )
        return workflowRepository.save(workflow)
    }

    fun get(key: WorkflowKey): Single<Workflow> {
        return workflowRepository.get(key)
    }

    fun getAll(request: GetAllWorkflowRequest): Observable<Workflow> {
        return workflowRepository.getAll(request)
    }

    fun getListOfAllWorkflows(request: GetListOfAllWorkflowsRequest): Observable<WorkflowInfo> {
        return workflowRepository.getListOfAllWorkflows(request)
    }

    fun evaluate(key: WorkflowKey, data: JsonObject): Single<WorkflowResult> {
        return Maybe.concat(fromCache(key), fromDb(key))
                .firstOrError()
                .map {
                    it.evaluate(data.map)
                }
                .doOnError {
                    "Workflow not active for key: $key".let {
                        logger.error(it)
                        SignalFx.noticeError(it)
                    }
                }
    }

    private fun fromCache(key: WorkflowKey): Maybe<RuleEngine> {
        return Maybe.create { subscriber ->
            cacheService.get(key)
                    .subscribe({
                        subscriber.onSuccess(it)
                    }, {
                        subscriber.onComplete()
                    }, {
                        subscriber.onComplete()
                    })
        }
    }

    private fun fromDb(key: WorkflowKey): Maybe<RuleEngine> {
        return if (key.version == null) {
            fromDbWithoutVersion(key)
        } else {
            fromDbWithVersion(key)
        }
    }

    private fun fromDbWithoutVersion(key: WorkflowKey): Maybe<RuleEngine> {
        return Maybe.create { subscriber ->
            activeWorkflowRepository
                    .get(ActiveKey(countryCode = key.countryCode, name = key.name))
                    .subscribe({
                        RuleEngine(it.workflow!!).apply {
                            subscriber.onSuccess(this)
                            cacheService.set(key, this)
                        }
                    }, {
                        subscriber.onError(when (it) {
                            is NoSuchElementException -> NotFoundException("Workflow not active for key: ${key.name}", "Workflow not active.")
                            else -> it
                        })
                    }, {
                        subscriber.onComplete()
                    })
        }
    }

    private fun fromDbWithVersion(key: WorkflowKey): Maybe<RuleEngine> {
        return Maybe.create { subscriber ->
            workflowRepository.get(key)
                    .subscribe({
                        RuleEngine(it.workflow).apply {
                            subscriber.onSuccess(this)
                            cacheService.set(key, this)
                        }
                    }, {
                        subscriber.onError(when (it) {
                            is NoSuchElementException -> NotFoundException("Workflow not active for key: ${key.name}", "Workflow not active.")
                            else -> it
                        })
                    })
        }
    }

    fun activate(request: ActivateRequest): Single<Workflow> {
        return workflowRepository
                .get(request.key)
                .flatMap { toActivate ->
                    activeWorkflowRepository
                            .save(
                                    ActiveWorkflow(
                                            countryCode = request.key.countryCode,
                                            name = request.key.name,
                                            workflowId = toActivate.id!!
                                    )
                            )
                            .flatMap {
                                Single.just(toActivate)
                            }
                }
                .doOnSuccess {
                    saveHistory(it, request)
                    saveActiveInCache(it)
                }
                .doOnError {
                    logger.error("Activate of workflow ${request.key} could not be completed", it)
                    SignalFx.noticeError(it)
                }
    }

    private fun saveActiveInCache(workflow: Workflow) {
        cacheService
                .set(
                        WorkflowKey(
                                countryCode = workflow.countryCode,
                                name = workflow.name
                        ),
                        RuleEngine(workflow.workflow))
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
                )
                .doOnError {
                    logger.error("Activate of workflow ${workflow.id} could not be saved in the history", it)
                    SignalFx.noticeError(it)
                }
    }
}
