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
            put("connectTimeoutMS", config.connectTimeout)
            put("useObjectId", true)
            put("readPreference", "secondaryPreferred")
            put("replicaSet", "rs0")
        }

        return DocumentDb(
            delegate = MongoClient.createShared(vertx, settingsWrite)
        )
    }

    data class Config(
        val connectionStringWrite: String,
        val connectTimeout: Int
    )
}
