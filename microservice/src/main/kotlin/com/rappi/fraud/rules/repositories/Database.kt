package com.rappi.fraud.rules.repositories

import io.reactiverse.pgclient.PgPoolOptions
import io.reactiverse.reactivex.pgclient.PgPool
import io.reactiverse.reactivex.pgclient.Row
import io.reactiverse.reactivex.pgclient.Tuple
import io.reactiverse.reactivex.pgclient.data.Json
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import java.util.concurrent.TimeUnit

private const val MAX_CONNECTIONS_PER_INSTANCE = 10

class Database(vertx: Vertx, config: Config) {

    val connectionPool = createClient(vertx, config)

    private fun createClient(vertx: Vertx, cfg: Config): PgPool {
        val url = cfg.url.removePrefix("jdbc:")
        val config = PgPoolOptions.fromUri(url).apply {
            user = cfg.user
            password = cfg.pass
            maxSize = MAX_CONNECTIONS_PER_INSTANCE
            connectTimeout = cfg.connTimeout.toInt()
            idleTimeout = cfg.idleTimeout
            reconnectAttempts=2
            idleTimeoutUnit = TimeUnit.MILLISECONDS
        }
        return PgPool.pool(vertx, config)
    }

    fun executeWithParams(query: String, params: List<Any>): Single<Row> {
        val p = Tuple.tuple()
        params.forEach {
            if (it is JsonObject) p.addJson(Json.create(it))
            else p.addValue(it)
        }
        return connectionPool
                .rxPreparedQuery(query, p)
            .map {
                it.iterator().next()
            }
    }

    fun executeDelete(statements: String, params: List<Any>): Single<Int> {
        val p = Tuple.tuple()
        params.forEach { p.addValue(it) }

        return connectionPool.rxPreparedQuery(statements, p)
            .map { it.rowCount() }
    }

    fun executeBatch(query: String, tuples: List<Tuple>): Single<Int> {

        return connectionPool.rxPreparedBatch(query, tuples)
            .map { it.rowCount() }
    }

    fun executeBatchDelete(query: String, tuples: List<Tuple>): Single<Int> {

        return connectionPool.rxPreparedBatch(query, tuples)
            .map { it.rowCount() }
    }

    fun get(query: String, params: List<Any> = listOf()): Observable<Row> {
        val p = Tuple.tuple()
        params.forEach { p.addValue(it) }
        return connectionPool
                .rxPreparedQuery(query, p)
                .flatMapObservable { Observable.fromIterable(it.delegate) }
                .map(::Row)
    }

    data class Config(
        val url: String,
        val user: String,
        val pass: String,
        val driver: String,
        val providerClass: String,
        val connTimeout: Long,
        val leakDetectionThreshold: Long,
        val maxPoolSize: Int,
        val maxLifetime: Int,
        val minimumIdle: Int,
        val idleTimeout: Int
    )
}
