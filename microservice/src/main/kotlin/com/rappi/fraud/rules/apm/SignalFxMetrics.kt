package com.rappi.fraud.rules.apm

import com.codahale.metrics.MetricRegistry
import com.rappi.fraud.rules.verticle.LoggerDelegate
import com.signalfx.codahale.SfxMetrics
import com.signalfx.codahale.reporter.SignalFxReporter
import com.signalfx.endpoint.SignalFxEndpoint
import com.signalfx.endpoint.SignalFxReceiverEndpoint
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SignalFxMetrics @Inject constructor(private val config: Config) {

    val logger by LoggerDelegate()
    private val sfxMetrics: SfxMetrics

    init {
        val ingestUrl = URL(config.ingestUrl)
        val signalFxEndpoint: SignalFxReceiverEndpoint =
            SignalFxEndpoint(ingestUrl.protocol, ingestUrl.host, ingestUrl.port)
        val metricRegistry = MetricRegistry()
        val signalFxReporter = SignalFxReporter.Builder(
            metricRegistry,
            config.token
        ).setEndpoint(signalFxEndpoint).build()
        signalFxReporter.start(config.timePeriodSeconds, TimeUnit.MILLISECONDS)
        sfxMetrics = SfxMetrics(metricRegistry, signalFxReporter.metricMetadata)
    }

    fun reportMissingFields(warnings: Set<String>, event: String, country: String) {
        val fields = warnings.map { it.split(" ")[0] }
        for(field in fields) {
            sfxMetrics.counter("fraud_rules_engine_missing_fields",
                mapOf("event" to event, "field_name" to field, "country" to country, "env" to  if(config.country == "dev") "dev" else "prod")
            ).inc()
        }
    }
    data class Config(
        val country: String,
        val ingestUrl: String,
        val token: String,
        val timePeriodSeconds: Long
    )
}
