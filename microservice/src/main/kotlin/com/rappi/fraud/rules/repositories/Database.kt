package com.rappi.fraud.rules.repositories

import io.reactiverse.pgclient.PgPoolOptions
import io.reactiverse.reactivex.pgclient.PgPool
import io.reactiverse.reactivex.pgclient.Row
import io.reactiverse.reactivex.pgclient.Tuple
import io.reactivex.Observable
import io.reactivex.Single
import io.reactiverse.reactivex.pgclient.data.Json
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx

private const val MAX_CONNECTIONS_PER_INSTANCE = 5

class Database(vertx: Vertx, dbConfig: DatabaseConfig) {

    val connectionPool = createClient(vertx, dbConfig)

    private fun createClient(vertx: Vertx, cfg: DatabaseConfig): PgPool {
        val url = cfg.url.removePrefix("jdbc:")
        val config = PgPoolOptions.fromUri(url).apply {
            user = cfg.user
            password = cfg.pass
            maxSize = MAX_CONNECTIONS_PER_INSTANCE
        }
        return PgPool.pool(vertx, config)
    }

    fun executeWithParams(query: String, params: List<Any>): Single<Row> {
        val p = Tuple.tuple()
        params.forEach {
            if(it is JsonObject) p.addJson(Json.create(it))
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
}

data class DatabaseConfig(
    val url: String,
    val user: String,
    val pass: String,
    val driver: String
)
