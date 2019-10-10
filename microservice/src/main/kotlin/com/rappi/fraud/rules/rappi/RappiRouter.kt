package com.rappi.fraud.rules.rappi

import com.google.inject.Inject
import com.rappi.fraud.rules.rappi.entities.*
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.ErrorHandler
import io.vertx.reactivex.ext.web.handler.LoggerHandler

class RappiRouter @Inject constructor(
    private val vertx: Vertx,
    private val workflowsService: WorkflowsService
) {

    private val logger by LoggerDelegate()

    fun create(): Router {
        val router = Router.router(vertx)

        router.routeWithRegex("(?!/health-check).*").handler(LoggerHandler.create())
        router.route("/*").failureHandler(ErrorHandler.create())
        router.route().handler(BodyHandler.create())

        router.get("/health-check").handler { it.response().end("OK") }
        router.post("/evaluate/:name/:version").handler(::evaluate)

        router.post("/workflow").handler(::createWorkflow)
        router.put("/workflow").handler(::createWorkflow)
        router.get("/workflow/:name/:version").handler(::getWorkflow)
        router.get("/workflow/:name").handler(::getAllWorkflows)
        router.post("/workflow/:name/evaluate").handler(::evaluate)
        router.post("/workflow/:name/status").handler(::updateStatus)

        return router
    }

    private fun evaluate(ctx: RoutingContext) {
        Single.just(ctx.bodyAsJson).flatMap {
            val workflow = EvaluateWorkflowRequest(
                name = ctx.pathParams().get("name")!!,
                version = ctx.pathParams().get("version")!!.toLong()
            )
            workflowsService.evaluate(it, workflow)
        }.subscribe({
            ctx.response().setStatusCode(200)
                .end(it)
        }, {
            logger.error(it.message)
            ctx.fail(500, it)
        })
    }

    private fun createWorkflow(ctx: RoutingContext) {
        Single.just(ctx.bodyAsJson).map {
            val rules = it.getJsonArray("rules")
            .map { rule ->
                CreateWorkflowRuleRequest(
                    name = (rule as JsonObject).getString("name"),
                    condition = rule.getString("condition")
                )
            }
            CreateWorkflowRequest(
                workflow = it.getString("workflow"),
                ruleset = it.getString("ruleset"),
                rules = rules
            )
        }.flatMap {
            workflowsService.save(it)
        }.subscribe({
            ctx.response().setStatusCode(200)
                .putHeader("Content-Type", "application/json")
                .end(it.toJson().toString())
        }, {
            logger.error(it.message)
            ctx.fail(500, it)
        })
    }

    private fun getWorkflow(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).map {
            GetWorkflowRequest(
                name = it.get("name")!!,
                version = it.get("version")!!.toLong()
            )
        }.flatMap {
            workflowsService.get(it)
        }.subscribe({
            ctx.response().setStatusCode(200)
                .putHeader("Content-Type", "application/json")
                .end(it.toJson().toString())
        }, {
            logger.error(it.message)
            ctx.fail(500, it)
        })
    }

    private fun getAllWorkflows(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).map {
            GetAllWorkflowRequest(
                name = it.get("name")!!
            )
        }.flatMap {
            workflowsService.getAll(it).toList()
        }.subscribe({
            ctx.response().setStatusCode(200)
                .putHeader("Content-Type", "application/json")
                .end(it.map { it.toJson() }.toString())
        }, {
            logger.error(it.message)
            ctx.fail(500, it)
        })
    }
    private fun updateStatus(ctx: RoutingContext) {
    }
}
