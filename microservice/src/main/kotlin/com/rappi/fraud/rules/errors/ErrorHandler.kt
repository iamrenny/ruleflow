package com.rappi.fraud.rules.errors

import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.parser.errors.ErrorRequestException
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.reactivex.ext.web.RoutingContext

class ErrorHandler: Handler<RoutingContext> {
    val logger by LoggerDelegate()

    override fun handle(event: RoutingContext?) {
        var response = event!!.response()
        var statusCode: Int = HttpResponseStatus.INTERNAL_SERVER_ERROR.code()
        var message: Any
        var cause: Any

        when (event.failure()) {
            is ErrorRequestException -> {
                var exception = (event.failure() as ErrorRequestException)
                statusCode = exception.statusCode
                message = exception.message
                cause = exception.errorCode ?: ""
            }
            else -> {
                statusCode = HttpResponseStatus.INTERNAL_SERVER_ERROR.code()
                message = event.failure().message ?: ""
                cause = "internal.error"
            }
        }

        SignalFx.noticeError(event.failure())

        logger.error("Error processing request ${event.request().method()} ${event.request().path()} - ${event.failure().message}")
        response.setStatusCode(statusCode).putHeader("content-type", "application/json; charset=utf-8")
            .end(Json.encode(ErrorObject(message, cause, statusCode)))
    }
}
