package com.rappi.fraud.rules.module

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.rappi.fraud.rules.repositories.Database
import com.rappi.fraud.rules.repositories.DatabaseConfig
import com.rappi.fraud.rules.verticle.MainRouter
import com.rappi.fraud.rules.verticle.MainVerticle
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.redis.RedisClient

abstract class AbstractModule(private val vertx: Vertx, private val config: JsonObject) : AbstractModule() {

    override fun configure() {
        bind(Vertx::class.java).toInstance(vertx)
        bind(MainVerticle::class.java).`in`(Singleton::class.java)
        bind(MainRouter::class.java).`in`(Singleton::class.java)
    }

    @Provides
    fun dbConfig(): DatabaseConfig {
        val db = config.getJsonObject("database")
        return DatabaseConfig(
            url = db.getString("DB_URL"),
            user = db.getString("DB_USER"),
            pass = db.getString("DB_PASSWORD"),
            driver = db.getString("DRIVER_CLASS")
        )
    }

    @Provides
    fun database(db: DatabaseConfig): Database {
        return Database(vertx, db)
    }

    @Provides
    fun redisClient(): RedisClient {
        val redisConfig = JsonObject()
            .put("host", config.getJsonObject("redis").getString("REDIS_HOST"))
            .put("port", config.getJsonObject("redis").getString("REDIS_PORT").toInt())
        return RedisClient.create(vertx, redisConfig)
    }
}
