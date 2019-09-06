package com.rappi.fraud.rules.rappi

import com.google.inject.Inject
import com.rappi.fraud.rules.parser.EvaluateRulesetVisitor
import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.ErrorHandler
import io.vertx.reactivex.ext.web.handler.LoggerHandler
import org.jetbrains.kotlin.utils.keysToMap
import java.lang.RuntimeException
import javax.json.JsonObject

class RappiRouter @Inject constructor(private val vertx: Vertx) {

    private val logger by LoggerDelegate()

    fun create(): Router {
        val router = Router.router(vertx)

        router.routeWithRegex("(?!/health-check).*").handler(LoggerHandler.create())
        router.route("/*").failureHandler(ErrorHandler.create())
        router.route().handler(BodyHandler.create())

        router.get("/health-check").handler { it.response().end("OK") }
        router.post("/evaluate").handler(::evaluate)

        router.post("/workflow").handler(::createWorkflow)
        router.put("/workflow").handler(::createWorkflow)
        router.get("/workflow/:name").handler(::getWorkflow)
        router.post("/workflow/:name/evaluate").handler(::evaluate)
        router.post("/workflow/:name/status").handler(::updateStatus)

        return router
    }

    private fun evaluate(ctx: RoutingContext) {
        val body = ctx.bodyAsJson
        val rappiRulesHandler = RappiRulesHandler()
        val p = RuleEngine("").evaluate(body.map)
        rappiRulesHandler.handle()
            .subscribe({
                ctx.response().setStatusCode(200).end(it.toString())
            }, {
                ctx.fail(500, RuntimeException())
            })
    }

    private fun createWorkflow(ctx: RoutingContext) {

        // recibe el string de todo el workflow
    }

    private fun getWorkflow(ctx: RoutingContext) {
    }

    private fun updateStatus(ctx: RoutingContext) {
    }
}
