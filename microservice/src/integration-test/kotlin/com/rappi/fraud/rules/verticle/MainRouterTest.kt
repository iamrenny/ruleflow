package com.rappi.fraud.rules.verticle

import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.services.WorkflowService
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.http.HttpClientRequest
import java.net.URLEncoder
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
            countryCode = data.getString("countryCode"),
            workflow = data.getString("workflow"),
            version = 10,
            userId = UUID.randomUUID().toString()
        )

        whenever(
            workflowService
                .save(CreateWorkflowRequest(expected.countryCode, expected.workflow, expected.userId))
        )
            .thenReturn(Single.just(expected))

        val request = httpClient
            .postAbs("http://localhost:8080/api/fraud-rules-engine/workflow")
            .putAuthUserHeader(expected.userId)
        request
            .toObservable()
            .subscribe {
                Assertions.assertTrue(it.statusCode() == HttpResponseStatus.OK.code())
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
                        countryCode = data.getString("countryCode"),
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
                    Assertions.assertTrue(it.statusCode() == HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    testContext.completeNow()
                } catch (ex: AssertionError) {
                    testContext.failNow(ex)
                }
            }
        request.end(data.toString())
    }

    @Test
    fun testGet(vertx: Vertx, testContext: VertxTestContext) {
        val data = getSeedAsJsonObject("get_workflow.json")
        val workflow = data.mapTo(Workflow::class.java)

        whenever(
            workflowService
                .get(
                    WorkflowKey(
                        countryCode = workflow.countryCode,
                        name = workflow.name,
                        version = workflow.version!!
                    )
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
                Assertions.assertTrue(it.statusCode() == HttpResponseStatus.OK.code())
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
    }

    @Test
    fun testGetAll(vertx: Vertx, testContext: VertxTestContext) {
        val data = getSeedAsJsonArray("get_all_workflow.json")
        val workflows = data
            .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java) })
            .toList()

        whenever(
            workflowService
                .getAll(
                    GetAllWorkflowRequest(
                        countryCode = workflows[0].countryCode,
                        name = workflows[0].name
                    )
                )
        )

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
                Assertions.assertTrue(it.statusCode() == HttpResponseStatus.OK.code())
                it.bodyHandler { body ->
                    val response = body.toJsonArray()
                        .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java) }).toList()
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

    private fun HttpClientRequest.putAuthUserHeader(userId: String) =
        this.putHeader(MainRouter.HEADER_AUTH_USER, userId)
}
