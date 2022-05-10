package com.rappi.fraud.rules.verticle

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.Grafana
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.lists.cache.EventsListener
import com.rappi.fraud.rules.repositories.Database
import com.rappi.fraud.rules.repositories.ListRepository
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
    private val listRepository: ListRepository,
    private val eventsListener: EventsListener
) :
    AbstractVerticle() {

    private val logger by LoggerDelegate()

    override fun start(promise: Promise<Void>) {
        migration.migrateDB()
            .andThen(startServer())
            .andThen(listRepository.cacheUpdateAll())
            .doOnComplete { eventsListener.listen() }
            .subscribe(CompletableHelper.toObserver(promise))
    }

    private fun startServer(): Completable {
        val router = Router
            .router(vertx)
            .mountSubRouter("/api/fraud-rules-engine", router.create())

        val server = vertx.createHttpServer()
        server.requestStream()
            .toFlowable()
            .onBackpressureBuffer(512) { logger.warn("Buffering requests..") }
            .onBackpressureDrop { req ->
                logger.warn("Dropping Requests...")

                Grafana.noticeError("Request Dropped")
                SignalFx.noticeError("Request Dropped")
                req.response().setStatusCode(503).end()
            }
            .subscribe(router::handle, ::logError)

        val port = config().getInteger("http.port", 8080)

        return server
            .rxListen(port)
            .doOnSuccess { logger.info("server is running at port $port...") }
            .ignoreElement()
    }

    private fun logError(t: Throwable) {
        Grafana.noticeError("Error handling request", t)
        SignalFx.noticeError("Error handling request", t)
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
