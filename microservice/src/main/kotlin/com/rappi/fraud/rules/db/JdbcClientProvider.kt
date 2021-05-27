package com.rappi.fraud.rules.db

import com.rappi.fraud.rules.repositories.Database
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.jdbc.JDBCClient
import javax.inject.Inject
import javax.inject.Provider

class JdbcClientProvider @Inject constructor(private val vertx: Vertx, private val config: Database.Config) :
    Provider<JDBCClient> {

    override fun get(): JDBCClient {

        val config = JsonObject().apply {
            put("jdbcUrl", config.url)
            put("driverClassName", config.driver)
            put("username", config.user)
            put("password", config.pass)
            put("max_idle_time", 60)
            put("provider_class", config.providerClass)
            put("connectionTimeout", config.connTimeout)
            put("maximumPoolSize", config.maxPoolSize)
            put("leakDetectionThreshold", config.leakDetectionThreshold)
            put("maxLifetime", config.maxLifetime)
            put("minimumIdle", config.minimumIdle)
        }

        return JDBCClient.createShared(vertx, config)
    }
}
