package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveKey
import com.rappi.fraud.rules.entities.ActiveWorkflow
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WorkflowServiceTest {

    private val activeWorkflowRepository = mock<ActiveWorkflowRepository>()
    private val activeWorkflowHistoryRepository = mock<ActiveWorkflowHistoryRepository>()
    private val ruleEngineCacheService = mock<CacheService>()
    private val workflowRepository = mock<WorkflowRepository>()
    private val service = WorkflowService(activeWorkflowRepository, activeWorkflowHistoryRepository,
        ruleEngineCacheService, workflowRepository)

    @BeforeEach
    fun cleanUp() {
        reset(activeWorkflowRepository, activeWorkflowHistoryRepository, ruleEngineCacheService, workflowRepository)
    }

    @Test
    fun testSave() {
        val expected = baseWorkflow()

        whenever(workflowRepository
            .save(Workflow(
                countryCode = expected.countryCode,
                name = expected.name,
                workflow = expected.workflow,
                userId = expected.userId)))
            .thenReturn(Single.just(expected))

        service
            .save(
                CreateWorkflowRequest(
                    countryCode = expected.countryCode,
                    workflow = expected.workflow,
                    userId = expected.userId))
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(expected)
            .dispose()
    }

    @Test
    fun testGet() {
        val expected = baseWorkflow()

        val key = WorkflowKey(
                countryCode = expected.countryCode,
                name = expected.name,
                version = expected.version!!
        )

        whenever(workflowRepository.get(key))
                .thenReturn(Single.just(expected))

        service
                .get(key)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(expected)
                .dispose()
    }

    @Test
    fun testGetAll() {
        val base = baseWorkflow()

        val expected = listOf(base, base.copy(id = 13, version = 2))

        val request = GetAllWorkflowRequest(
                countryCode = base.countryCode,
                name = base.name
        )

        whenever(workflowRepository.getAll(request))
                .thenReturn(Observable.merge(expected.map { Observable.just(it) }))

        service
                .getAll(request)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValueSet(expected)
                .dispose()
    }

    @Test
    fun testActivate() {
        val expected = baseWorkflow()

        val request = ActivateRequest(
                key = WorkflowKey(
                        countryCode = expected.countryCode,
                        name = expected.name,
                        version = expected.version!!
                ),
                userId = UUID.randomUUID().toString()
        )

        whenever(workflowRepository
            .get(WorkflowKey(
                    countryCode = expected.countryCode,
                    name = expected.name,
                    version = expected.version!!)))
            .thenReturn(Single.just(expected))

        val activeWorkflow = ActiveWorkflow(
                countryCode = expected.countryCode,
                name = expected.name,
                workflowId = expected.id!!
        )
        whenever(activeWorkflowRepository
            .save(activeWorkflow))
            .thenReturn(Single.just(activeWorkflow))

        whenever(activeWorkflowHistoryRepository
            .save(ActiveWorkflowHistory(
                    workflowId = expected.id!!,
                    userId = request.userId
                )))
            .thenReturn(Single.just(ActiveWorkflowHistory(
                id = 1,
                workflowId = expected.id!!,
                userId = request.userId,
                createdAt = LocalDateTime.now()
            )))

        whenever(ruleEngineCacheService
            .set(any(), any()))
            .thenReturn(Completable.complete())

        service
                .activate(request)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(expected)
                .assertOf {
                    verify(activeWorkflowHistoryRepository).save(
                            ActiveWorkflowHistory(
                                    workflowId = expected.id!!,
                                    userId = request.userId
                            )
                    )
                    argumentCaptor<RuleEngine>().apply {
                        verify(ruleEngineCacheService).set(
                                eq(WorkflowKey(countryCode = expected.countryCode, name = expected.name)), capture()
                        )
                        Assertions.assertEquals(expected.workflow, firstValue.workflow)
                    }
                }
                .dispose()
    }

    @Test
    fun testEvaluateFromCache() {
        val workflow = baseWorkflow()

        val key = WorkflowKey(countryCode = workflow.countryCode, name = workflow.name)

        whenever(ruleEngineCacheService.get(key))
                .thenReturn(Maybe.just(RuleEngine(workflow.workflow)))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow"
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(key, data)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(evaluationResult)
                .dispose()
    }

    @Test
    fun testEvaluateFromDbWithoutVersion() {
        val workflow = baseWorkflow()

        val key = WorkflowKey(countryCode = workflow.countryCode, name = workflow.name)

        whenever(ruleEngineCacheService.get(key))
                .thenReturn(Maybe.empty())

        whenever(activeWorkflowRepository.get(ActiveKey(countryCode = key.countryCode, name = key.name)))
                .thenReturn(Maybe.just(
                        ActiveWorkflow(
                                countryCode = workflow.countryCode,
                                name = workflow.name,
                                workflowId = workflow.id!!,
                                workflow = workflow.workflow
                        )))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow"
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(key, data)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(evaluationResult)
                .dispose()
    }

    @Test
    fun testEvaluateFromDbWithVersion() {
        val workflow = baseWorkflow()

        val key = WorkflowKey(countryCode = workflow.countryCode, name = workflow.name, version = workflow.version)

        whenever(ruleEngineCacheService.get(key))
                .thenReturn(Maybe.empty())

        whenever(activeWorkflowRepository.get(ActiveKey(countryCode = key.countryCode, name = key.name)))
                .thenReturn(Maybe.empty())

        whenever(workflowRepository.get(key))
                .thenReturn(Single.just(workflow))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow"
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(key, data)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(evaluationResult)
                .dispose()
    }

    private fun baseWorkflow(): Workflow {
        return Workflow(
                id = 12,
                countryCode = "MX",
                name = "Sample",
                version = 1,
                workflow = "workflow 'Sample' ruleset 'Sample' 'Deny' d >= 100 return allow default block end",
                userId = UUID.randomUUID().toString()
        )
    }
}
