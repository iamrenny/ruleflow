package com.rappi.fraud.rules.verticle

import com.google.inject.Inject
import com.rappi.fraud.rules.documentdb.DocumentDbInit
import com.rappi.fraud.rules.repositories.Database
import io.reactivex.Completable
import io.vertx.core.Promise
import io.vertx.reactivex.CompletableHelper
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import kotlin.reflect.KProperty
import org.flywaydb.core.Flyway
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainVerticle @Inject constructor(
    private val router: MainRouter,
    private val migration: FlywayMigration,
    private val documentDbInit: DocumentDbInit
) :
    AbstractVerticle() {

    private val logger by LoggerDelegate()

    override fun start(promise: Promise<Void>) {
        migration.migrateDB()
            .andThen(documentDbInit.createIndexes())
            .andThen(startServer())
            .subscribe(CompletableHelper.toObserver(promise))
    }

    private fun startServer(): Completable {
        val router = Router.router(vertx).mountSubRouter("/api/fraud-rules-engine", router.create())

        val server = vertx.createHttpServer()
        server.requestStream()
            .toFlowable()
            .onBackpressureBuffer(512)
            .onBackpressureDrop { it.response().setStatusCode(503).end() }
            .subscribe(router::handle, ::logError)

        val port = config().getInteger("http.port", 8080)

        return server
            .rxListen(port)
            .doOnSuccess { logger.info("server is running at port $port...") }
            .ignoreElement()
    }

    private fun logError(t: Throwable) {
        logger.error("Error handling request: ", t)
    }
}

class FlywayMigration @Inject constructor(private val vertx: Vertx, private val database: Database.Config) {
    fun migrateDB(): Completable {
        return vertx.rxExecuteBlocking<Unit> {
            val flyway = Flyway.configure()
                .dataSource(database.url, database.user, database.pass)
                .load()
            flyway.migrate()
            it.complete()
        }.ignoreElement()
    }
}

class LoggerDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Logger {

        if (thisRef == null) {
            LoggerFactory.getLogger("DEFAULT").error("Trying to obtain a logger for a null instance {}", property)
            return LoggerFactory.getLogger("DEFAULT")
        }
        return LoggerFactory.getLogger(thisRef.javaClass)
    }
}
