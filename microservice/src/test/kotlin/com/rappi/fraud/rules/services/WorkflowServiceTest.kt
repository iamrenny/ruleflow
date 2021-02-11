package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.times
import com.rappi.fraud.rules.documentdb.DocumentDbDataRepository
import com.rappi.fraud.rules.documentdb.EventData
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.LockWorkflowEditionRequest
import com.rappi.fraud.rules.entities.RulesEngineHistoryRequest
import com.rappi.fraud.rules.entities.RulesEngineOrderListHistoryRequest
import com.rappi.fraud.rules.entities.NoRiskDetailDataWasFound
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowEditionResponse
import com.rappi.fraud.rules.exceptions.BadRequestException
import com.rappi.fraud.rules.parser.errors.ErrorRequestException
import com.rappi.fraud.rules.parser.vo.WorkflowInfo
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.ListRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.micrometer.MicrometerMetricsOptions
import io.vertx.micrometer.backends.BackendRegistries
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception
import java.time.LocalDateTime
import java.util.UUID

class WorkflowServiceTest {

    private val activeWorkflowRepository = mock<ActiveWorkflowRepository>()
    private val activeWorkflowHistoryRepository = mock<ActiveWorkflowHistoryRepository>()
    private val workflowRepository = mock<WorkflowRepository>()
    private val listRepository = mock<ListRepository>()
    private val workFlowEditionService = mock<WorkflowEditionService>()
    private val documentDbDataRepository= mock<DocumentDbDataRepository>()
    private val service = WorkflowService(activeWorkflowRepository, activeWorkflowHistoryRepository,
        workflowRepository, listRepository, workFlowEditionService, documentDbDataRepository)

    @BeforeEach
    fun cleanUp() {
        reset(activeWorkflowRepository, activeWorkflowHistoryRepository, workflowRepository, listRepository)
        whenever(listRepository.findAll())
            .thenReturn(Single.just(mapOf()))
        BackendRegistries.setupBackend(MicrometerMetricsOptions())
        whenever(documentDbDataRepository.saveEventData(any())).then{
            Single.just(it.arguments[0] as EventData)
        }
    }

    @Test
    fun testSave() {
        val expected = baseWorkflow()

        whenever(workflowRepository
            .save(
                Workflow(
                countryCode = expected.countryCode,
                name = expected.name,
                workflowAsString = expected.workflowAsString,
                userId = expected.userId)
            ))
            .thenReturn(Single.just(expected))

        whenever(workflowRepository
            .exists(any(), any()))
            .thenReturn(Single.just(true))

        whenever(workFlowEditionService
            .getUserEditing(any(), any()))
            .thenReturn(Single.just(expected.userId))

        whenever(workFlowEditionService
            .cancelWorkflowEditing(any(), any()))
            .thenReturn(Single.just(WorkflowEditionService.WorkflowEditionStatus(
                "OK",
                "workflow edition canceled"
            )))

        service
            .save(
                CreateWorkflowRequest(
                    countryCode = expected.countryCode!!,
                    workflow = expected.workflowAsString!!,
                    userId = expected.userId!!)
            )
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(expected)
            .dispose()
    }

    @Test
    fun testSaveEditionLocked() {
        val expected = baseWorkflow()

        whenever(workflowRepository
            .save(Workflow(
                countryCode = expected.countryCode,
                name = expected.name,
                workflowAsString = expected.workflowAsString,
                userId = expected.userId)))
            .thenReturn(Single.just(expected))

        whenever(workflowRepository
            .exists(any(), any()))
            .thenReturn(Single.just(true))

        whenever(workFlowEditionService
            .getUserEditing(any(), any()))
            .thenReturn(Single.just("userEditing"))

        whenever(workFlowEditionService
            .cancelWorkflowEditing(any(), any()))
            .thenReturn(Single.just(WorkflowEditionService.WorkflowEditionStatus(
                "OK",
                "workflow edition canceled"
            )))

        service
            .save(
                CreateWorkflowRequest(
                    countryCode = expected.countryCode!!,
                    workflow = expected.workflowAsString!!,
                    userId = expected.userId!!))
            .test()
            .assertSubscribed()
            .await()
            .assertError {
                it is ErrorRequestException &&
                    it.statusCode == 423 &&
                    it.message == """the workflow is being edited by userEditing"""
            }
    }

    @Test
    fun testSaveEditionNotLocked() {
        val expected = baseWorkflow()

        whenever(workflowRepository
            .save(Workflow(
                countryCode = expected.countryCode,
                name = expected.name,
                workflowAsString = expected.workflowAsString,
                userId = expected.userId)))
            .thenReturn(Single.just(expected))

        whenever(workFlowEditionService
            .getUserEditing(any(), any()))
            .thenReturn(Single.just("NOT FOUND"))

        whenever(workflowRepository
            .exists(any(), any()))
            .thenReturn(Single.just(true))

        service
            .save(
                CreateWorkflowRequest(
                    countryCode = expected.countryCode!!,
                    workflow = expected.workflowAsString!!,
                    userId = expected.userId!!))
            .test()
            .assertSubscribed()
            .await()
            .assertError {
                it is ErrorRequestException &&
                    it.statusCode == 400 &&
                    it.message == """no active workflow edition to save"""
            }
    }

    @Test
    fun testInvalidRuleNameHaveSpaces() {
        val invalidWorkflow = workflowWithRuleNameSpaces()

        assertThrows<java.lang.IllegalArgumentException> {
            service
                    .save(
                            CreateWorkflowRequest(
                                    countryCode = invalidWorkflow.countryCode!!,
                                    workflow = invalidWorkflow.workflowAsString!!,
                                    userId = invalidWorkflow.userId!!))
        }

        verifyZeroInteractions(workflowRepository)
    }

    @Test
    fun testInvalidRuleNameHaveSpecialCharacters() {
        val invalidWorkflow = workflowWithRuleNameSpecialChars()

        assertThrows<IllegalArgumentException> {
            service
                    .save(
                            CreateWorkflowRequest(
                                    countryCode = invalidWorkflow.countryCode!!,
                                    workflow = invalidWorkflow.workflowAsString!!,
                                    userId = invalidWorkflow.userId!!))
        }

        verifyZeroInteractions(workflowRepository)
    }

    @Test
    fun testGet() {
        val expected = baseWorkflow()

        whenever(workflowRepository.getWorkflow(
            countryCode = expected.countryCode!!,
            name = expected.name,
            version = expected.version!!)
        )
            .thenReturn(Single.just(expected))

        service
                .getWorkflow(countryCode = expected.countryCode!!, name = expected.name, version = expected.version!!)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(expected)
                .dispose()
    }

    @Test
    fun testGetForEdition() {
        val workflow = baseWorkflow()

        val expected = WorkflowEditionResponse(
            workflow,
            WorkflowEditionService.WorkflowEditionStatus(
                "OK"
            )
        )

        val req = LockWorkflowEditionRequest(
            countryCode = workflow.countryCode!!,
            workflowName = workflow.name,
            version = workflow.version!!,
            user = workflow.userId!!
        )

        whenever(workflowRepository.getWorkflow(
            countryCode = workflow.countryCode!!,
            name = workflow.name,
            version = workflow.version!!)
        )
            .thenReturn(Single.just(workflow))

        whenever(workFlowEditionService
            .lockWorkflowEdition(any(), any(), any()))
            .thenReturn(Single.just(
                WorkflowEditionService.WorkflowEditionStatus("OK")
            ))

        service
            .getWorkflowForEdition(req.countryCode, req.workflowName, req.version, req.user)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(expected)
            .dispose()
    }

    @Test
    fun testFailGetForEdition() {
        val workflow = baseWorkflow()

        val expected = WorkflowEditionResponse(
            null,
            WorkflowEditionService.WorkflowEditionStatus(
                "NOT OK"
            )
        )

        val req = LockWorkflowEditionRequest(
            countryCode = workflow.countryCode!!,
            workflowName = workflow.name,
            version = workflow.version!!,
            user = workflow.userId!!
        )

        whenever(workflowRepository.getWorkflow(
            countryCode = workflow.countryCode!!,
            name = workflow.name,
            version = workflow.version!!)
        )
            .thenReturn(Single.just(workflow))

        whenever(workFlowEditionService
            .lockWorkflowEdition(any(), any(), any()))
            .thenReturn(Single.just(
                WorkflowEditionService.WorkflowEditionStatus("NOT OK")
            ))

        service
            .getWorkflowForEdition(req.countryCode, req.workflowName, req.version, req.user)
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
                countryCode = base.countryCode!!,
                name = base.name
        )

        whenever(workflowRepository.getWorkflowsByCountryAndName(request))
                .thenReturn(Observable.merge(expected.map { Observable.just(it) }))

        service
                .getWorkflowsByCountryAndName(request)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValueSet(expected)
                .dispose()
    }

    @Test
    fun testGetListOfAllWorkflowsByCountry() {
        val base = Workflow(
            id = 20,
            name = "Sample",
            countryCode = "co",
            workflowAsString = "workflow",
            version = 10,
            userId = UUID.randomUUID().toString(),
            createdAt = LocalDateTime.now()
        )

        val expected = listOf(base,
            base.copy(name = "Sample2"),
            base.copy(name = "Sample3"))

        val countryCode = "MX"

        whenever(workflowRepository.getActiveWorkflowsByCountry(countryCode))
            .thenReturn(Observable.merge(expected.map { Observable.just(it) }))

        service
            .getActiveWorkflowsByCountry(countryCode)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValueSet(expected)
            .dispose()
    }

    @Test
    fun getAllWorkflowsByCountry() {
        val base = Workflow(
                id = 20,
                name = "Sample",
                countryCode = "mx",
                workflowAsString = "workflow",
                version = 10,
                userId = UUID.randomUUID().toString(),
                createdAt = LocalDateTime.now()
        )

        val expected = listOf(base,
                base.copy(name = "Sample2"),
                base.copy(name = "Sample3"))

        val countryCode = "MX"

        whenever(workflowRepository.getAllWorkflowsByCountry(countryCode))
                .thenReturn(Observable.merge(expected.map { Observable.just(it) }))

        service
                .getAllWorkflowsByCountry(countryCode)
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
                        countryCode = expected.countryCode!!,
                        name = expected.name,
                        version = expected.version!!,
                        userId = UUID.randomUUID().toString()
        )

        whenever(workflowRepository
            .getWorkflow(
                countryCode = expected.countryCode!!,
                name = expected.name,
                version = expected.version!!
            )
        )
            .thenReturn(Single.just(expected))

        whenever(activeWorkflowRepository.save(expected))
            .thenReturn(Single.just(expected.activate()))

        whenever(activeWorkflowHistoryRepository
            .save(
                ActiveWorkflowHistory(
                    workflowId = expected.id!!,
                    userId = request.userId
                )
            ))
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
                }
                .dispose()
    }

    @Test
    fun testEvaluateFromCache() {
        val workflow = baseWorkflow()

        whenever(activeWorkflowRepository.get(workflow.countryCode!!, workflow.name))
                .thenReturn(Single.just(workflow))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow",
                workflowInfo = WorkflowInfo("1", "Sample")
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(countryCode = workflow.countryCode!!, name = workflow.name, data = data)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(evaluationResult)
                .dispose()

        verify(activeWorkflowRepository, times(1)).get(any(), any())
        verifyZeroInteractions(workflowRepository)
    }

    @Test
    fun testEvaluateFromDbWithoutVersion() {
        val workflow = baseWorkflow()

        whenever(activeWorkflowRepository.get(countryCode = workflow.countryCode!!, name = workflow.name))
                .thenReturn(Single.just(
                        Workflow(
                                id = workflow.id!!,
                                countryCode = workflow.countryCode,
                                name = workflow.name,
                                userId = workflow.userId,
                                version = workflow.version,
                                workflowAsString = workflow.workflowAsString
                        )))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow",
                workflowInfo = WorkflowInfo("1", "Sample")
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(countryCode = workflow.countryCode!!, name = workflow.name, data = data)
                .test()
                .assertSubscribed()
                .await()
                .assertValue(evaluationResult)
                .dispose()

        verify(activeWorkflowRepository, times(1)).get(any(), any())
        verifyZeroInteractions(workflowRepository)
    }

    @Test
    fun testEvaluateFromDbWithVersion() {
        val workflow = baseWorkflow()

        whenever(workflowRepository.getWorkflow(workflow.countryCode!!, workflow.name, workflow.version!!))
                .thenReturn(Single.just(workflow))

        val evaluationResult = WorkflowResult(
                workflow = "Sample",
                ruleSet = "Sample",
                rule = "Deny",
                risk = "allow",
                workflowInfo = WorkflowInfo("1", "Sample")
        )

        val data = JsonObject()
                .put("d", 101)

        service.evaluate(countryCode = workflow.countryCode!!, name = workflow.name, version = workflow.version, data = data)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(evaluationResult)
                .dispose()

        verify(workflowRepository, times(1)).getWorkflow(any(), any(), any())
        verifyZeroInteractions(activeWorkflowRepository)
        verify(documentDbDataRepository, times(1)).saveEventData(any())
    }

    @Test
    fun testSimulateWorkflowWithVersion() {
        val workflow = baseWorkflow()

        whenever(workflowRepository.getWorkflow(workflow.countryCode!!, workflow.name, workflow.version!!))
            .thenReturn(Single.just(workflow))

        val evaluationResult = WorkflowResult(
            workflow = "Sample",
            ruleSet = "Sample",
            rule = "Deny",
            risk = "allow",
            workflowInfo = WorkflowInfo("1", "Sample")
        )

        val data = JsonObject()
            .put("d", 101)

        service.evaluate(countryCode = workflow.countryCode!!, name = workflow.name, version = workflow.version, data = data, isSimulation = true)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(evaluationResult)
            .dispose()

        verify(workflowRepository, times(1)).getWorkflow(any(), any(), any())
        verifyZeroInteractions(activeWorkflowRepository)
        verify(documentDbDataRepository, times(0)).saveEventData(any())
    }

    @Test
    fun `get data - getEventData`() {
        val request = EventData(
            request = JsonObject().put("pepe", "test"),
            response = JsonObject().put("response", "OK"),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "login",
            id = "601c0a4bd103cb30b67bb19f"
        )

        documentDbDataRepository.saveEventData(request)

        whenever(documentDbDataRepository.find(request.id!!))
            .then { Single.just(request) }

        service.getRequestIdData(request.id!!)
            .test()
            .await()
            .assertValue { it.id.equals("601c0a4bd103cb30b67bb19f") }
            .assertComplete()
            .assertNoErrors()

        whenever(documentDbDataRepository.find(any()))
            .then { Single.error<Exception>(DocumentDbDataRepository.NoRequestIdDataWasFound()) }

        service.getRequestIdData("601c0a4bd103cb30b67bb191")
            .test()
            .await()
            .assertError {
                it.message.equals("601c0a4bd103cb30b67bb191 was not found")
            }

        whenever(documentDbDataRepository.find(any()))
            .then {
                Single.error<Exception>(
                    ErrorRequestException(
                        "timeout", ErrorRequestException.ErrorCode.TIMEOUT.toString(),
                        HttpResponseStatus.REQUEST_TIMEOUT.code()
                    )
                )
            }

        service.getRequestIdData("601c0a4bd103cb30b67bb197")
            .test()
            .await()
            .assertError {
                it.message.equals("timeout")
            }
    }

    fun testGetHistoryFromDb() {
        val request = RulesEngineHistoryRequest(LocalDateTime.now(),
            LocalDateTime.now(),
            "create_order",
            "dev")

        whenever(documentDbDataRepository.getRiskDetailHistoryFromDocDb(any()))
            .thenReturn(Single.just(listOf()))

        service.getEvaluationHistory(request)
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun testGetHistoryFromDbThrowsException() {
        val request = RulesEngineHistoryRequest(LocalDateTime.now(),
            LocalDateTime.now(),
            "create_order",
            "dev")

        whenever(documentDbDataRepository.getRiskDetailHistoryFromDocDb(any()))
            .then { Single.error<Exception>(RuntimeException()) }

        service.getEvaluationHistory(request)
            .test().await()
            .assertFailure(RuntimeException::class.java)
            .dispose()
    }

    @Test
    fun testGetHistoryFromDbThrowsNoRiskDetailDataWasFound() {
        val request = RulesEngineHistoryRequest(LocalDateTime.now(),
            LocalDateTime.now(),
            "create_order",
            "dev")

        whenever(documentDbDataRepository.getRiskDetailHistoryFromDocDb(any()))
            .then { Single.error<Exception>(NoRiskDetailDataWasFound()) }

        service.getEvaluationHistory(request)
            .test()
            .assertFailure(BadRequestException::class.java)
            .dispose()
    }

    @Test
    fun testGetOrdersHistory() {
        val request = RulesEngineOrderListHistoryRequest(
            listOf("3"),
            "create_order",
            "dev")

        whenever(documentDbDataRepository.findInList(any()))
            .thenReturn(Single.just(listOf()))

        service.getEvaluationOrderListHistory(request)
            .test()
            .assertComplete()
            .dispose()
    }

    @Test
    fun testGetOrdersHistoryThrowsException() {
        val request = RulesEngineOrderListHistoryRequest(
            listOf("3"),
            "create_order",
            "dev")

        whenever(documentDbDataRepository.findInList(any()))
            .then { Single.error<Exception>(RuntimeException()) }

        service.getEvaluationOrderListHistory(request)
            .test().await()
            .assertFailure(RuntimeException::class.java)
            .dispose()
    }

    @Test
    fun testGetOrdersHistoryThrowsNoRiskDetailDataWasFound() {
        val request = RulesEngineOrderListHistoryRequest(
            listOf("3"),
            "create_order",
            "dev")

        whenever(documentDbDataRepository.findInList(any()))
            .then { Single.error<Exception>(NoRiskDetailDataWasFound()) }

        service.getEvaluationOrderListHistory(request)
            .test()
            .assertFailure(BadRequestException::class.java)
            .dispose()
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

    private fun workflowWithRuleNameSpaces(): Workflow {
        val request = "workflow 'Sample 1' ruleset 'Sample' " +
                "'Invalid rule' d >= 100 return allow " +
                "'valid_rule' d >= 100 return allow " +
                "default block end"
        return Workflow(
                id = 12,
                countryCode = "MX",
                name = "Sample",
                version = 1,
                workflowAsString = request,
                userId = UUID.randomUUID().toString()
        )
    }

    private fun workflowWithRuleNameSpecialChars(): Workflow {
        val request = "workflow 'Sample#' ruleset 'Sample' " +
                "'valid_rule' d >= 100 return allow " +
                "'Invalid_rule_&_#_^' d >= 100 return allow " +
                "default block end"
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
