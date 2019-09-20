package com.rappi.fraud.rules.bd

import com.google.inject.Inject
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactiverse.pgclient.PgPoolOptions
import io.reactiverse.reactivex.pgclient.PgPool
import io.reactiverse.reactivex.pgclient.PgRowSet
import io.reactiverse.reactivex.pgclient.Row
import io.reactiverse.reactivex.pgclient.Tuple
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.reactivex.core.Vertx


private const val MAX_CONNECTIONS_PER_INSTANCE = 5

class Database(vertx: Vertx, dbConfig: DatabaseConfig) {

    private val logger by LoggerDelegate()

    private val jdbc = createClient(vertx, dbConfig)

    private fun createClient(vertx: Vertx, cfg: DatabaseConfig): PgPool {
        val url = cfg.url.removePrefix("jdbc:")
        val config = PgPoolOptions.fromUri(url).apply {
            user = cfg.user
            password = cfg.pass
            maxSize = MAX_CONNECTIONS_PER_INSTANCE
        }
        return PgPool.pool(vertx, config)
    }

    fun save(query: String, params: List<Any>): Single<Row> {
        val p = Tuple.tuple()
        params.forEach { p.addValue(it) }

        return jdbc.rxPreparedQuery(query, p).map { it.iterator().next()}
    }

    fun get(query: String, params: List<Any> = listOf()): Observable<Row> {
        val p = Tuple.tuple()
        params.forEach { p.addValue(it) }

        return jdbc.rxPreparedQuery(query, p)
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


