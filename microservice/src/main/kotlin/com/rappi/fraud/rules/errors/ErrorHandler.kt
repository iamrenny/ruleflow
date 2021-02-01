package com.rappi.fraud.rules.errors

import com.rappi.fraud.rules.apm.Grafana
import com.rappi.fraud.rules.parser.errors.ErrorRequestException
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.reactivex.ext.web.RoutingContext

class ErrorHandler : Handler<RoutingContext> {
    val logger by LoggerDelegate()

    override fun handle(event: RoutingContext?) {
        var response = event!!.response()
        var statusCode: Int
        var message: Any? = null
        var cause: Any? = null

        when (event.failure()) {
            is ErrorRequestException -> {
                var exception = (event.failure() as ErrorRequestException)
                statusCode = exception.statusCode
                message = exception.message
                cause = exception.errorCode
            }
            is IllegalArgumentException -> {
                var exception = (event.failure() as IllegalArgumentException)
                statusCode = 400
                message = exception.message
            }
            is NoSuchElementException -> {
                statusCode = HttpResponseStatus.NOT_FOUND.code()
            }
            else -> {
                statusCode = HttpResponseStatus.INTERNAL_SERVER_ERROR.code()
                message = event.failure().message ?: ""
                cause = "internal.error"
            }
        }

        Grafana.noticeError(event.failure())

        logger.error("Error processing request ${event.request().method()} ${event.request().path()} - ${event.failure().message}")
        response.setStatusCode(statusCode).putHeader("content-type", "application/json; charset=utf-8")
            .end(Json.encode(ErrorObject(message, cause, statusCode)))
    }
}
