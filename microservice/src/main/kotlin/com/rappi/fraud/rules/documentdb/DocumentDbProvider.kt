package com.rappi.fraud.rules.documentdb

import com.google.inject.Inject
import com.google.inject.Provider
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.mongo.MongoClient

class DocumentDbProvider @Inject constructor(
    private val config: Config,
    private val vertx: Vertx
) : Provider<DocumentDb> {

    override fun get(): DocumentDb {
        val settingsWrite = JsonObject().apply {
            put("connection_string", config.connectionStringWrite)
            put("maxPoolSize", config.maxPoolConnections)
            put("connectTimeoutMS", config.connectTimeout)
            put("useObjectId", true)
        }

        val settingsRead = JsonObject().apply {
            put("connection_string", config.connectionStringRead)
            put("maxPoolSize", config.maxPoolConnections)
            put("connectTimeoutMS", config.connectTimeout)
            put("useObjectId", true)
        }

        val mongoWrite = MongoClient.createShared(vertx, settingsWrite)
        val mongoRead = MongoClient.createShared(vertx, settingsRead)
        return DocumentDb(
            delegateRead = mongoRead,
            delegateWrite = mongoWrite)
    }

    data class Config(
        val connectionStringWrite: String,
        val connectionStringRead: String,
        val maxPoolConnections: Int,
        val connectTimeout: Int
    )
}
