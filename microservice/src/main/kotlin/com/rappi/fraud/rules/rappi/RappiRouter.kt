package com.rappi.fraud.rules.rappi

import com.google.inject.Inject
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.ErrorHandler
import io.vertx.reactivex.ext.web.handler.LoggerHandler
import java.lang.RuntimeException

class RappiRouter @Inject constructor(private val vertx: Vertx) {

    private val logger by LoggerDelegate()

    fun create(): Router {
        val router = Router.router(vertx)

        router.routeWithRegex("(?!/health-check).*").handler(LoggerHandler.create())
        router.route("/*").failureHandler(ErrorHandler.create())
        router.route().handler(BodyHandler.create())

        router.get("/health-check").handler { it.response().end("OK") }
        router.post("/evaluate").handler(::evaluate)
        router.post("/create-workflow").handler(::createWorkflow)

        router.get(":/workflow").handler(::getWorkflow)
        router.post("/:workflow").handler(::addRule)
        router.put("/:workflow/:rule_id").handler(::updateRule)
        router.delete("/:workflow/:rule_id").handler(::updateRule)
        router.get("/:workflow/:rule_id").handler(::getRule)
        return router
    }

    private fun evaluate(ctx: RoutingContext) {
        val body = ctx.bodyAsJson
        val rappiRulesHandler = RappiRulesHandler()

        rappiRulesHandler.handle()
            .subscribe({
                ctx.response().setStatusCode(200).end(it.toString())
            }, {
                ctx.fail(500, RuntimeException())
            })
    }

    private fun createWorkflow(ctx: RoutingContext) {

    }

    private fun getWorkflow(ctx: RoutingContext) {

    }


    private fun addRule(ctx: RoutingContext ){

    }

    private fun updateRule(ctx: RoutingContext) {

    }

    private fun getRule(ctx: RoutingContext) {

    }


}
