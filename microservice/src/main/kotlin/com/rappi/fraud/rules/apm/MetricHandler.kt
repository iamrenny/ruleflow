package com.rappi.fraud.rules.apm

import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.micrometer.core.instrument.Tag
import io.vertx.core.Handler
import io.vertx.micrometer.backends.BackendRegistries
import io.vertx.reactivex.ext.web.RoutingContext
import java.util.UUID

val REQUEST_ID = "request_id"
val START_TIME = "start_time"

class MetricHandler : Handler<RoutingContext> {

    private val logger by LoggerDelegate()

    override fun handle(event: RoutingContext) {
        try {
            event.put(START_TIME, System.currentTimeMillis())
            event.put(REQUEST_ID, UUID.randomUUID().toString())
            event.addBodyEndHandler {
                try {
                    val startTime = event.get(START_TIME) as Long
                    val requestId = event.get(REQUEST_ID) as String
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val ownXAppId = System.getenv("x_application_id")

                    val route = formatPath(event.request().path(), event.pathParams())
                    val sourceRequest = event.request().headers().get("X-Application-Id") ?: "NA"
                    val params = event.request().params()
                    val failed = event.failed()
                    val method = event.request().method().toString()

                    val metricMap = mutableMapOf(
                        "source" to sourceRequest,
                        "route" to route,
                        "failed" to failed,
                        "method" to method,
                        "x_app_id" to ownXAppId
                    )
                    val customMap = mutableMapOf(
                        "elapsed_time" to elapsedTime,
                        "source" to sourceRequest,
                        "route" to route,
                        "request_id" to requestId,
                        "failed" to failed,
                        "method" to method,
                        "x_app_id" to ownXAppId
                    )

                    params.entries().forEach {
                        customMap.put(it.key, it.value)
                    }

                    BackendRegistries.getDefaultNow().counter("fraud.rules-engine.requests",
                        metricMap.map { Tag.of(it.key, it.value.toString()) }
                    ).increment()
                } catch (e: Throwable) {
                    Grafana.noticeError(e)
                    SignalFx.noticeError(e)
                }
            }
        } catch (e: Throwable) {
            Grafana.noticeError(e)
            SignalFx.noticeError(e)
            logger.error("Error handling route {}", e.message)
        } finally {
            event.next()
        }
    }

    private fun formatPath(path: String, pathParams: Map<String, String>): String {
        var newPath = path
        pathParams.forEach {
            newPath = path.replace(it.value, "{${it.key}}")
        }
        return newPath
    }

    fun increment(metricName: String, metricTag: Tag) {
        logger.info("Increment Tag ${metricTag.key} ${metricTag.value}")
        BackendRegistries.getDefaultNow().counter(metricName, metricTag.key, metricTag.value).increment()
    }

    fun increment(metricName: String, metricMap: MutableMap<String, Any>) {
        BackendRegistries.getDefaultNow().counter(metricName,
            metricMap.map { Tag.of(it.key, it.value.toString()) }).increment()
    }
    companion object {
        @JvmStatic
        fun create(): MetricHandler {
            return MetricHandler()
        }
    }
}
