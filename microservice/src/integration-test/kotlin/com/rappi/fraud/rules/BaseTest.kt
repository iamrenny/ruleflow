package com.rappi.fraud.rules

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Singleton
import com.nhaarman.mockito_kotlin.mock
import com.rappi.fraud.rules.config.ConfigParser
import com.rappi.fraud.rules.module.AbstractModule
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.Database
import com.rappi.fraud.rules.repositories.WorkflowRepository
import com.rappi.fraud.rules.services.WorkflowService
import com.rappi.fraud.rules.verticle.MainVerticle
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.micrometer.MicrometerMetricsOptions
import io.vertx.micrometer.backends.BackendRegistries
import io.vertx.reactivex.core.http.HttpClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
abstract class BaseTest {

    companion object {

        lateinit var injector: Injector
        lateinit var httpClient: HttpClient
        private lateinit var config: ConfigParser

        init {
            // things that may need to be setup before companion class member variables are instantiated
        }

        @BeforeAll
        @JvmStatic
        fun deployVerticle(vertx: Vertx, testContext: VertxTestContext) {
            Json.mapper.registerModule(KotlinModule())
            val rxVertx = io.vertx.reactivex.core.Vertx(vertx)

            config = ConfigParser(rxVertx)

            injector = Guice.createInjector(TestResourcesModule(rxVertx, config.read().blockingGet()))
            val main = injector.getInstance(MainVerticle::class.java)
            vertx.deployVerticle(main, testContext.succeeding {
                testContext.completeNow()
            })

            httpClient = rxVertx.createHttpClient()

            BackendRegistries.setupBackend(MicrometerMetricsOptions())
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            injector.getInstance(Database::class.java)
                    .connectionPool
                    .rxQuery("""
                        TRUNCATE active_workflows;
                        TRUNCATE list_history,  list_items, lists_workflows, lists RESTART IDENTITY;
                        TRUNCATE active_workflows_history;
                    """)
                .blockingGet()
        }

        @Suppress("all")
        fun getSeedAsJsonObject(dataFile: String) = JsonObject(readFile(dataFile))

        @Suppress("all")
        fun getSeedAsJsonArray(dataFile: String) = JsonArray(readFile(dataFile))

        private fun readFile(file: String) =
                BaseTest::class.java.classLoader.getResourceAsStream("data/$file")!!.reader().readText()
    }

    class TestResourcesModule(vertx: io.vertx.reactivex.core.Vertx, config: JsonObject) :
        AbstractModule(vertx, config) {

        override fun configure() {
            super.configure()
            bind(ActiveWorkflowRepository::class.java).`in`(Singleton::class.java)
            bind(ActiveWorkflowHistoryRepository::class.java).`in`(Singleton::class.java)
            bind(WorkflowRepository::class.java).`in`(Singleton::class.java)
            bind(WorkflowService::class.java).toInstance(mock())
        }
    }
}
