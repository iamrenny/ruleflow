package com.rappi.fraud.rules.services

import com.google.inject.Inject
import com.newrelic.api.agent.NewRelic
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveWorkflow
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject

class WorkflowService @Inject constructor(
    private val activeWorkflowRepository: ActiveWorkflowRepository,
    private val activeWorkflowHistoryRepository: ActiveWorkflowHistoryRepository,
    private val workflowRepository: WorkflowRepository
) {

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

    fun evaluate(a: JsonObject, key: WorkflowKey): Single<String> {
        return workflowRepository.get(key)
            .map {
                RuleEngine(it.workflow).evaluate(a.map).risk
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
                activeWorkflowHistoryRepository
                    .save(
                        ActiveWorkflowHistory(
                            workflowId = it.id!!,
                            userId = request.userId
                        )
                    )
                    .doOnError {
                        NewRelic.noticeError(it)
                    }
            }
    }
}
