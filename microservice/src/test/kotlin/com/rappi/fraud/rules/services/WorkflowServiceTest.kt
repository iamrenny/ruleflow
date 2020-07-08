package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.GetListOfAllWorkflowsRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowInfo
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.ListRepository
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
    private val listRepository = mock<ListRepository>()
    private val service = WorkflowService(activeWorkflowRepository, activeWorkflowHistoryRepository,
        cacheService, workflowRepository, listRepository)

    @BeforeEach
    fun cleanUp() {
        reset(activeWorkflowRepository, activeWorkflowHistoryRepository, cacheService, workflowRepository, listRepository)
        whenever(cacheService.set(any()))
            .thenReturn(Single.just(baseWorkflow()))
        whenever(listRepository.findAllWithEntries())
            .thenReturn(Single.just(mapOf()))
        BackendRegistries.setupBackend(MicrometerMetricsOptions())
    }

    @Test
    fun testSave() {
        val expected = baseWorkflow()

        whenever(workflowRepository
            .save(Workflow(
                countryCode = expected.countryCode,
                name = expected.name,
                workflowAsString = expected.workflowAsString,
                userId = expected.userId)))
            .thenReturn(Single.just(expected))

        service
            .save(
                CreateWorkflowRequest(
                    countryCode = expected.countryCode,
                    workflow = expected.workflowAsString,
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

        whenever(workflowRepository.get(
            countryCode = expected.countryCode,
            name = expected.name,
            version = expected.version!!)
        )
            .thenReturn(Single.just(expected))

        service
                .get(countryCode = expected.countryCode, name = expected.name, version = expected.version!!)
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
            .listAllWorkflows(request)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValueSet(expected)
            .dispose()
    }

    @Test
    fun testActivate() {
        val expected = baseWorkflow().activate()

        val request = ActivateRequest(
                        countryCode = expected.countryCode,
                        name = expected.name,
                        version = expected.version!!,
                        userId = UUID.randomUUID().toString()
        )

        whenever(workflowRepository
            .get(
                countryCode = expected.countryCode,
                name = expected.name,
                version = expected.version!!
            )
        )
            .thenReturn(Single.just(expected))

        whenever(activeWorkflowRepository.save(expected))
            .thenReturn(Single.just(expected.activate()))

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
                    argumentCaptor<Workflow>().apply {
                        verify(cacheService).set(
                                capture()
                        )
                        Assertions.assertEquals(expected.workflowAsString, firstValue.workflowAsString)
                    }
                }
                .dispose()
    }

    @Test
    fun testEvaluateFromCache() {
        val workflow = baseWorkflow()

        whenever(cacheService.get(workflow.countryCode, workflow.name))
                .thenReturn(Maybe.just(workflow))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow"
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(countryCode = workflow.countryCode, name = workflow.name, data = data)
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

        whenever(cacheService.get(workflow.countryCode, workflow.name))
                .thenReturn(Maybe.empty())

        whenever(activeWorkflowRepository.get(countryCode = workflow.countryCode, name = workflow.name))
                .thenReturn(Single.just(
                        Workflow(
                                id = workflow.id!!,
                                countryCode = workflow.countryCode,
                                name = workflow.name,
                                userId = workflow.userId,
                                workflowAsString = workflow.workflowAsString
                        )))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow"
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(countryCode = workflow.countryCode, name = workflow.name, data = data)
                .test()
                .assertSubscribed()
                .await()
                .assertValue(evaluationResult)
                .dispose()

        verify(cacheService, times(1)).get(any(), any(), eq(null))
        verifyZeroInteractions(workflowRepository)
    }

    @Test
    fun testEvaluateFromDbWithVersion() {
        val workflow = baseWorkflow()

        whenever(cacheService.get(workflow.countryCode, workflow.name, workflow.version))
                .thenReturn(Maybe.empty())

        whenever(workflowRepository.get(workflow.countryCode, workflow.name, workflow.version!!))
                .thenReturn(Single.just(workflow))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow"
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(countryCode = workflow.countryCode, name = workflow.name, version = workflow.version, data = data)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(evaluationResult)
                .dispose()

        verify(cacheService, times(1)).get(any(), any(), any())
        verifyZeroInteractions(activeWorkflowRepository)
    }

    private fun baseWorkflow(): Workflow {
        val request = "workflow 'Sample' ruleset 'Sample' 'Deny' d >= 100 return allow default block end"
        return Workflow(
                id = 12,
                countryCode = "MX",
                name = "Sample",
                version = 1,
                workflowAsString = request,
                userId = UUID.randomUUID().toString()
        )
    }
}
