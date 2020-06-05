package com.rappi.fraud.rules.verticle

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.GetListOfAllWorkflowsRequest
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.parser.errors.NotFoundException
import com.rappi.fraud.rules.services.WorkflowService
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.ErrorHandler
import io.vertx.reactivex.ext.web.handler.LoggerHandler
import java.net.URLDecoder

class MainRouter @Inject constructor(
    private val vertx: Vertx,
    private val workflowService: WorkflowService
) {

    private val logger by LoggerDelegate()

    companion object {
        const val HEADER_AUTH_USER = "X-Auth-User"
        private val X_APP_ID = System.getenv("x_application_id")
    }

    fun create(): Router {
        val router = Router.router(vertx)

        router.routeWithRegex("(?!/health-check).*").handler(LoggerHandler.create())
        router.route("/*").failureHandler(ErrorHandler.create())
        router.route().handler(BodyHandler.create())

        router.get("/health-check").handler { it.response().end("OK") }

        router.post("/workflow").handler(::createWorkflow)
        router.get("/workflow/:countryCode/:name/:version").handler(::getWorkflow)
        router.get("/workflow/:countryCode/:name").handler(::getAllWorkflows)
        router.get("/workflow/:countryCode").handler(::getListOfAllWorkflowsByCountry)
        router.post("/workflow/:countryCode/:name/evaluate").handler(::evaluateActive)
        router.post("/workflow/:countryCode/:name/:version/evaluate").handler(::evaluate)
        router.post("/workflow/:countryCode/:name/:version/activate").handler(::activateWorkflow)

        return router
    }

    private fun getListOfAllWorkflowsByCountry(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).map {
            GetListOfAllWorkflowsRequest(
                countryCode = it["countryCode"]!!
            )
        }.flatMap {
            workflowService.getListOfAllWorkflows(it).toList()
        }.subscribe({
            ctx.ok(it.map { w ->
                JsonObject.mapFrom(w).toString()
            }.toString())
        }, {
            ctx.serverError(it)
        })
    }

    private fun evaluateActive(ctx: RoutingContext) {
        Single.just(ctx.bodyAsJson).flatMap {
            val workflow = WorkflowKey(
                countryCode = ctx.pathParam("countryCode"),
                name = URLDecoder.decode(ctx.pathParam("name"), "UTF-8")
            )
            workflowService.evaluate(workflow, it)
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, {cause ->
            when (cause) {
                is NoSuchElementException -> ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
                else -> ctx.fail(cause)
            }
        })
    }

    private fun evaluate(ctx: RoutingContext) {
        Single.just(ctx.bodyAsJson).flatMap {
            val workflow = WorkflowKey(
                countryCode = ctx.pathParam("countryCode"),
                name = URLDecoder.decode(ctx.pathParam("name"), "UTF-8"),
                version = ctx.pathParam("version").toLong()
            )
            workflowService.evaluate(workflow, it)
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, {cause ->
            when (cause) {
                is NotFoundException -> ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
                else -> ctx.fail(cause)
            }
        })
    }

    private fun createWorkflow(ctx: RoutingContext) {
        Single.just(ctx.bodyAsJson).map {
            CreateWorkflowRequest(
                countryCode = it.getString("country_code"),
                workflow = it.getString("workflow"),
                userId = ctx.getUserId()
            )
        }.flatMap {
            workflowService.save(it)
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, {
            ctx.serverError(it)
        })
    }

    private fun getWorkflow(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).map {
            WorkflowKey(
                name = URLDecoder.decode(it["name"]!!, "UTF-8"),
                version = it["version"]!!.toLong(),
                countryCode = it["countryCode"]!!
            )
        }.flatMap {
            workflowService.get(it)
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, {
            ctx.serverError(it)
        })
    }

    private fun getAllWorkflows(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).map {
            GetAllWorkflowRequest(
                countryCode = it["countryCode"]!!,
                name = URLDecoder.decode(it["name"]!!, "UTF-8")!!
            )
        }.flatMap {
            workflowService.getAll(it).toList()
        }.subscribe({
            ctx.ok(it.map { w -> JsonObject.mapFrom(w).toString() }.toString())
        }, {
            ctx.serverError(it)
        })
    }

    private fun activateWorkflow(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).map {
            ActivateRequest(
                key = WorkflowKey(
                    countryCode = it["countryCode"]!!,
                    name = URLDecoder.decode(it["name"]!!, "UTF-8")!!,
                    version = it["version"]!!.toLong()
                ),
                userId = ctx.getUserId()
            )
        }.flatMap {
            workflowService.activate(it)
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, {
            ctx.serverError(it)
        })
    }

    private fun RoutingContext.getUserId(): String {
        return request().getHeader(HEADER_AUTH_USER)
            ?: throw RuntimeException("$HEADER_AUTH_USER is missing")
    }

    private fun RoutingContext.ok(chunk: String) =
        response()
            .setStatusCode(HttpResponseStatus.OK.code())
            .putHeader("Content-Type", "application/json")
            .end(chunk)

    private fun RoutingContext.serverError(throwable: Throwable) {
        logger.error(throwable.message)
        SignalFx.noticeError(throwable)
        fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), throwable)
    }
}
