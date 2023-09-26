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


class SignalFxMetrics @Inject constructor(val sfxMetrics: SfxMetrics, private val config: Config) {

    val logger by LoggerDelegate()


    fun reportMissingFields(warnings: Set<String>, event: String, country: String) {
        val fields = warnings.map { it.split(" ")[0] }
        for(field in fields) {
            val replacedFields = replaceFields(field)
            sfxMetrics.counter("fraud_rules_engine_missing_fields",
                mapOf("event" to event, "field_name" to replacedFields, "country" to country, "env" to  if(config.country == "dev") "dev" else "prod")
            ).inc()
        }
    }

    /**
     * This method replaces the field names in the SignalFx metrics to avoid unnecessary metrics.
     */
    private fun replaceFields(field: String): String {
        val sortedNames = config.featureGroups.sortedByDescending { it.length }

        for (name in sortedNames) {
            if (field.startsWith(name)) {
                return name
            }
        }
        return field
    }
    data class Config(
        val country: String,
        val ingestUrl: String,
        val token: String,
        val timePeriodSeconds: Long,
        val featureGroups: Set<String>
    )
}
