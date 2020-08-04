package com.rappi.fraud.rules.module

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.rappi.fraud.rules.repositories.Database
import com.rappi.fraud.rules.verticle.JdbcClientProvider
import com.rappi.fraud.rules.verticle.MainRouter
import com.rappi.fraud.rules.verticle.MainVerticle
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.jdbc.JDBCClient
import io.vertx.reactivex.redis.RedisClient

abstract class AbstractModule(private val vertx: Vertx, private val config: JsonObject) : AbstractModule() {

    override fun configure() {
        bind(Vertx::class.java).toInstance(vertx)
        bind(MainVerticle::class.java).`in`(Singleton::class.java)
        bind(MainRouter::class.java).`in`(Singleton::class.java)
        bind(JDBCClient::class.java)
            .toProvider(JdbcClientProvider::class.java)
            .`in`(Singleton::class.java)
    }

    @Provides
    fun dbConfig(): Database.Config {
        val db = config.getJsonObject("database")
        return Database.Config(
            url = db.getString("DB_URL"),
            user = db.getString("DB_USER"),
            pass = db.getString("DB_PASSWORD"),
            driver = db.getString("DRIVER_CLASS"),
            providerClass = db.getString("PROVIDER_CLASS"),
            connTimeout = db.getString("CONNECTION_TIMEOUT").toString().toLong(),
            leakDetectionThreshold = db.getString("LEAK_THRESHOLD").toString().toLong(),
            maxPoolSize = db.getString("MAX_POOL_SIZE").toString().toInt(),
            maxLifetime = db.getString("MAX_LIFETIME").toString().toInt(),
            minimumIdle = db.getString("MINIMUM_IDLE").toString().toInt(),
            idleTimeout = db.getString("IDLE_TIMEOUT").toString().toInt()
        )
    }

    @Provides
    fun database(db: Database.Config): Database {
        return Database(vertx, db)
    }

    @Provides
    fun redisClient(): RedisClient {
        val redisConfig = JsonObject()
            .put("host", config.getJsonObject("redis").getString("REDIS_HOST"))
            .put("port", config.getJsonObject("redis").getString("REDIS_PORT").toInt())
        return RedisClient.create(vertx, redisConfig)
    }

    @Provides
    fun mainRouterConfig(): MainRouter.Config {
        return MainRouter.Config(
            timeout = config.getJsonObject("settings").getLong("APP_RESPONSE_TIMEOUT")
        )
    }
}
