package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.times
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveKey
import com.rappi.fraud.rules.entities.ActiveWorkflow
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.GetListOfAllWorkflowsRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowInfo
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.micrometer.MicrometerMetricsOptions
import io.vertx.micrometer.backends.BackendRegistries
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WorkflowServiceTest {

    private val activeWorkflowRepository = mock<ActiveWorkflowRepository>()
    private val activeWorkflowHistoryRepository = mock<ActiveWorkflowHistoryRepository>()
    private val cacheService = mock<CacheService>()
    private val workflowRepository = mock<WorkflowRepository>()
    private val service = WorkflowService(activeWorkflowRepository, activeWorkflowHistoryRepository,
        cacheService, workflowRepository)

    @BeforeEach
    fun cleanUp() {
        reset(activeWorkflowRepository, activeWorkflowHistoryRepository, cacheService, workflowRepository)
        whenever(cacheService.set(any(), any()))
            .thenReturn(Single.just(RuleEngine(baseWorkflow().workflow)))
        BackendRegistries.setupBackend(MicrometerMetricsOptions())
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
    fun testGetListOfAllWorkflowsByCountry() {
        val base = WorkflowInfo(
            name = "Sample",
            version = 1
        )

        val expected = listOf(base,
            base.copy(version = 2, name = "Sample2"),
            base.copy(version = 1, name = "Sample3"))

        val request = GetListOfAllWorkflowsRequest(
            countryCode = "MX"
        )

        whenever(workflowRepository.getListOfAllWorkflows(request))
            .thenReturn(Observable.merge(expected.map { Observable.just(it) }))

        service
            .getListOfAllWorkflows(request)
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
                        verify(cacheService).set(
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

        whenever(cacheService.get(key))
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

        verifyZeroInteractions(activeWorkflowRepository)
        verifyZeroInteractions(workflowRepository)
    }

    @Test
    fun testEvaluateFromDbWithoutVersion() {
        val workflow = baseWorkflow()

        val key = WorkflowKey(countryCode = workflow.countryCode, name = workflow.name)

        whenever(cacheService.get(key))
                .thenReturn(Maybe.empty())

        whenever(activeWorkflowRepository.get(ActiveKey(countryCode = key.countryCode, name = key.name)))
                .thenReturn(Single.just(
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
                .assertValue(evaluationResult)
                .dispose()

        verify(cacheService, times(1)).get(any())
        verifyZeroInteractions(workflowRepository)
    }

    @Test
    fun testEvaluateFromDbWithVersion() {
        val workflow = baseWorkflow()

        val key = WorkflowKey(countryCode = workflow.countryCode, name = workflow.name, version = workflow.version)

        whenever(cacheService.get(key))
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

        verify(cacheService, times(1)).get(any())
        verifyZeroInteractions(activeWorkflowRepository)
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
