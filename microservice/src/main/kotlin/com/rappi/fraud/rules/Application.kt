package com.rappi.fraud.rules

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.Guice
import com.rappi.fraud.rules.config.ConfigParser
import com.rappi.fraud.rules.module.ResourcesModule
import com.rappi.fraud.rules.parser.errors.NotFoundException
import com.rappi.fraud.rules.verticle.MainVerticle
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.VertxOptions
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.Json
import io.vertx.core.logging.SLF4JLogDelegateFactory
import io.vertx.micrometer.MicrometerMetricsOptions
import io.vertx.micrometer.VertxPrometheusOptions
import io.vertx.reactivex.core.Vertx
import org.flywaydb.core.internal.exception.FlywaySqlException
import kotlin.system.exitProcess
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.util.concurrent.TimeoutException

private val log: Logger = getLogger("main")

fun main() {

    // Route all vert.x logging to SLF4J
    System.setProperty("vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory::class.java.name)

    val options = configPrometheus()

    jsonConfig()

    val vertx = Vertx.vertx(options)


    val config = ConfigParser(vertx).read().blockingGet()
    val injector = Guice.createInjector(ResourcesModule(vertx, config))

    val main = injector.getInstance(MainVerticle::class.java)

    vertx.rxDeployVerticle(main).subscribe({
        log.info("Application started")
    }, { cause ->
        when (cause) {
            is FlywaySqlException -> log.error("Unable to obtain connection from database", cause.message)
            else -> log.error("Could not start application", cause)
        }
        exitProcess(1)
    })
}

private fun configPrometheus(): VertxOptions {
    val options = VertxOptions()
    options.metricsOptions = MicrometerMetricsOptions().setPrometheusOptions(
        VertxPrometheusOptions()
            .setEnabled(true)
            .setStartEmbeddedServer(true)
            .setEmbeddedServerOptions(HttpServerOptions().setPort(9090))
            .setEmbeddedServerEndpoint("/metrics")
    )
        .setEnabled(true)
    return options
}

private fun jsonConfig() {
    Json.mapper.apply {
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        registerModule(KotlinModule())
        registerModule(JavaTimeModule())
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    }
}
