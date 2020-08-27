package com.rappi.fraud.rules.verticle

import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.parser.vo.WorkflowInfo
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import com.rappi.fraud.rules.services.WorkflowService
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.http.HttpClientRequest
import java.net.URLEncoder
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MainRouterTest : BaseTest() {

    private val workflowService = injector.getInstance(WorkflowService::class.java)

    @BeforeEach
    fun before() {
        reset(workflowService)
    }

    @Test
    fun testCreate(vertx: Vertx, testContext: VertxTestContext) {
        val data = getSeedAsJsonObject("create_workflow.json")

        val expected = Workflow(
            id = 20,
            name = "Sample workflow",
            countryCode = data.getString("country_code"),
            workflowAsString = data.getString("workflow"),
            version = 10,
            userId = UUID.randomUUID().toString(),
            createdAt = LocalDateTime.now(),
            active = false
        )

        whenever(
                workflowService
                        .save(CreateWorkflowRequest(expected.countryCode!!, expected.workflowAsString!!, expected.userId!!))
        )
                .thenReturn(Single.just(expected))

        val request = httpClient
            .postAbs("http://localhost:8080/api/fraud-rules-engine/workflow")
            .putAuthUserHeader(expected.userId!!)
        request
                .toObservable()
                .subscribe {
                    Assertions.assertEquals(HttpResponseStatus.OK.code(), it.statusCode())
                    it.bodyHandler { body ->
                        val response = body.toJsonObject().mapTo(Workflow::class.java)
                        try {
                            Assertions.assertEquals(expected, response)
                            testContext.completeNow()
                        } catch (ex: AssertionError) {
                            testContext.failNow(ex)
                        }
                    }
                }
        request.end(data.toString())
    }

    @Test
    fun testCreateWhenFailed(vertx: Vertx, testContext: VertxTestContext) {
        val data = getSeedAsJsonObject("create_workflow.json")

        whenever(
                workflowService
                        .save(
                                CreateWorkflowRequest(
                                        countryCode = data.getString("country_code"),
                                        workflow = data.getString("workflow"),
                                        userId = data.getString("userId")
                                )
                        )
        )
                .thenThrow(RuntimeException())

        val request = httpClient
            .postAbs("http://localhost:8080/api/fraud-rules-engine/workflow")
            .putAuthUserHeader(data.getString("userId"))
        request
                .toObservable()
                .subscribe {
                    try {
                        Assertions.assertEquals(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), it.statusCode())
                        testContext.completeNow()
                    } catch (ex: AssertionError) {
                        testContext.failNow(ex)
                    }
                }
        request.end(data.toString())
    }

    @Test
    fun testGet(vertx: Vertx, testContext: VertxTestContext) {
        try {
            val data = getSeedAsJsonObject("get_workflow.json")
            val workflow = data.mapTo(Workflow::class.java)

            whenever(
                workflowService
                    .getWorkflow(
                        countryCode = workflow.countryCode!!,
                        name = workflow.name,
                        version = workflow.version!!
                    )
            )
                .thenReturn(Single.just(workflow))

            val request = httpClient
                .getAbs(
                    "http://localhost:8080/api/fraud-rules-engine/workflow/${workflow.countryCode}/${URLEncoder.encode(
                        workflow.name,
                        "UTF-8"
                    )}/${workflow.version}"
                )
            request
                .toObservable()
                .subscribe {
                    Assertions.assertEquals(HttpResponseStatus.OK.code(), it.statusCode())
                    it.bodyHandler { body ->
                        val response = body.toJsonObject().mapTo(Workflow::class.java)
                        try {
                            Assertions.assertEquals(workflow, response)
                            testContext.completeNow()
                        } catch (ex: AssertionError) {
                            testContext.failNow(ex)
                        }
                    }
                }
            request.end(data.toString())
        } catch (e: Exception) {
            testContext.failNow(e)
        }
    }

    @Test
    fun testGetAll(vertx: Vertx, testContext: VertxTestContext) {
        val data = getSeedAsJsonArray("get_all_workflow.json")
        val workflows = data
                .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java) })
                .toList()

        whenever(workflowService
                .getWorkflowsByCountryAndName(GetAllWorkflowRequest(
                        countryCode = workflows[0].countryCode!!,
                        name = workflows[0].name
                )))
                .thenReturn(Observable.merge(workflows.map { Observable.just(it) }))

        val request = httpClient
                .getAbs(
                        "http://localhost:8080/api/fraud-rules-engine/workflow/${workflows[0].countryCode}/${URLEncoder.encode(
                                workflows[0].name,
                                "UTF-8"
                        )}"
                )
        request
                .toObservable()
                .subscribe {
                    Assertions.assertEquals(HttpResponseStatus.OK.code(), it.statusCode())
                    it.bodyHandler { body ->
                        val response = body.toJsonArray()
                                .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java) })
                                .toList()
                        try {
                            Assertions.assertEquals(workflows, response)
                            testContext.completeNow()
                        } catch (ex: AssertionError) {
                            testContext.failNow(ex)
                        }
                    }
                }
        request.end(data.toString())
    }

    @Test
    fun testActivate(vertx: Vertx, testContext: VertxTestContext) {
        val expected = getSeedAsJsonObject("get_workflow.json").mapTo(Workflow::class.java)

        val activateRequest = ActivateRequest(
                countryCode = expected.countryCode!!,
                name = expected.name,
                version = expected.version!!,
                userId = UUID.randomUUID().toString()
        )

        whenever(workflowService
                .activate(activateRequest))
                .thenReturn(Single.just(expected))

        val request = httpClient
                .postAbs(
                        "http://localhost:8080/api/fraud-rules-engine/workflow/${expected.countryCode}/${URLEncoder.encode(
                                expected.name,
                                "UTF-8"
                        )}/${expected.version}/activate"
                )
                .putAuthUserHeader(activateRequest.userId)
        request
                .toObservable()
                .subscribe {
                    Assertions.assertEquals(HttpResponseStatus.OK.code(), it.statusCode())
                    it.bodyHandler { body ->
                        val response = body.toJsonObject().mapTo(Workflow::class.java)
                        try {
                            Assertions.assertEquals(expected, response)
                            testContext.completeNow()
                        } catch (ex: AssertionError) {
                            testContext.failNow(ex)
                        }
                    }
                }
        request.end()
    }

    @Test
    fun testEvaluateWithVersion(vertx: Vertx, testContext: VertxTestContext) {
        val workflow = getSeedAsJsonObject("get_workflow.json").mapTo(Workflow::class.java)
        val data = JsonObject()

        val expected = WorkflowResult(
                workflow = "Workflow 1",
                ruleSet = "test",
                rule = "test",
                risk = "block",
                workflowInfo = WorkflowInfo("1","Workflow 1"))

        whenever(workflowService
                .evaluate(countryCode = workflow.countryCode!!, name = workflow.name, version = workflow.version!!, data = data))
                .thenReturn(Single.just(expected))

        val request = httpClient
                .postAbs(
                        "http://localhost:8080/api/fraud-rules-engine/workflow/${workflow.countryCode}/${URLEncoder.encode(
                                workflow.name,
                                "UTF-8"
                        )}/${workflow.version}/evaluate"
                )
        request
                .toObservable()
                .subscribe {
                    Assertions.assertEquals(HttpResponseStatus.OK.code(), it.statusCode())
                    it.bodyHandler { body ->
                        val response = body.toJsonObject().mapTo(WorkflowResult::class.java)
                        try {
                            Assertions.assertEquals(expected, response)
                            testContext.completeNow()
                        } catch (ex: AssertionError) {
                            testContext.failNow(ex)
                        }
                    }
                }
        request.end(data.toString())
    }

    @Test
    fun testEvaluateWithoutVersion(vertx: Vertx, testContext: VertxTestContext) {
        val workflow = getSeedAsJsonObject("get_workflow.json").mapTo(Workflow::class.java)

        val data = JsonObject()

        val expected = WorkflowResult(
                workflow = "Workflow 1",
                ruleSet = "test",
                rule = "test",
                risk = "allow",
                workflowInfo = WorkflowInfo("1","Workflow 1"))

        whenever(workflowService
                .evaluate(countryCode = workflow.countryCode!!, name = workflow.name, data = data))
                .thenReturn(Single.just(expected))

        val request = httpClient
                .postAbs(
                        "http://localhost:8080/api/fraud-rules-engine/workflow/${workflow.countryCode}/${URLEncoder.encode(
                                workflow.name,
                                "UTF-8"
                        )}/evaluate"
                )
        request
                .toObservable()
                .subscribe {
                    Assertions.assertEquals(HttpResponseStatus.OK.code(), it.statusCode())
                    it.bodyHandler { body ->
                        val response = body.toJsonObject().mapTo(WorkflowResult::class.java)
                        try {
                            Assertions.assertEquals(expected, response)
                            testContext.completeNow()
                        } catch (ex: AssertionError) {
                            testContext.failNow(ex)
                        }
                    }
                }
        request.end(data.toString())
    }

    private fun HttpClientRequest.putAuthUserHeader(userId: String) =
        this.putHeader(MainRouter.HEADER_AUTH_USER, userId)
}
