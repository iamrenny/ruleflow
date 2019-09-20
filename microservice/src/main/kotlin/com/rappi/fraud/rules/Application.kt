package com.rappi.fraud.rules

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.Guice
import com.rappi.fraud.rules.config.ConfigParser
import com.rappi.fraud.rules.module.ResourcesModule
import com.rappi.fraud.rules.verticle.MainVerticle
import io.vertx.core.json.Json
import io.vertx.core.logging.SLF4JLogDelegateFactory
import io.vertx.reactivex.core.Vertx
import kotlin.system.exitProcess
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

private val log: Logger = getLogger("main")

fun main() {

    // Route all vert.x logging to SLF4J
    System.setProperty("vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory::class.java.name)

    val vertx = Vertx.vertx()

    jsonConfig()

    val config = ConfigParser(vertx).read().blockingGet()
    val injector = Guice.createInjector(ResourcesModule(vertx, config))

    val main = injector.getInstance(MainVerticle::class.java)

    vertx.rxDeployVerticle(main).subscribe({ ar ->
        log.info("Application started")
    }, {
        log.error("Could not start application", it.message)
        exitProcess(1)
    })

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
