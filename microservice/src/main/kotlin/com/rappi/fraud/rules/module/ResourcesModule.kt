package com.rappi.fraud.rules.module

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.rappi.fraud.rules.bd.Database
import com.rappi.fraud.rules.bd.DatabaseConfig
import com.rappi.fraud.rules.rappi.RappiRouter
import com.rappi.fraud.rules.rappi.WorkflowsService
import com.rappi.fraud.rules.rappi.repositories.WorkflowRepository
import com.rappi.fraud.rules.verticle.MainVerticle
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx

class ResourcesModule(
    private val vertx: Vertx,
    val config: JsonObject
) : AbstractModule() {

    override fun configure() {
        bind(Vertx::class.java).toInstance(vertx)
        bind(WorkflowRepository::class.java).`in`(Singleton::class.java)
        bind(WorkflowsService::class.java).`in`(Singleton::class.java)
        bind(MainVerticle::class.java).`in`(Singleton::class.java)
        bind(RappiRouter::class.java).`in`(Singleton::class.java)
    }

    @Provides
    fun dbConfig(): DatabaseConfig {
        val databases = config.getJsonObject("storage")
        val db = databases.getJsonObject("database")
        return DatabaseConfig(
            url = db.getString("url"),
            user = db.getString("user"),
            pass = db.getString("pass"),
            driver = db.getString("driver")
        )
    }

    @Provides
    fun database(db: DatabaseConfig): Database {
        return Database(vertx, db)
    }
}
