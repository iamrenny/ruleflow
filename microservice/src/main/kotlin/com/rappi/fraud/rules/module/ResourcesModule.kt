package com.rappi.fraud.rules.module

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.rappi.fraud.rules.rappi.RappiRouter
import com.rappi.fraud.rules.verticle.FlywayMigration
import com.rappi.fraud.rules.verticle.MainVerticle
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx

class ResourcesModule(
    private val vertx: Vertx,
    val config: JsonObject
) : AbstractModule() {

    override fun configure() {
        bind(Vertx::class.java).toInstance(vertx)

        bind(MainVerticle::class.java).`in`(Singleton::class.java)
        bind(RappiRouter::class.java).`in`(Singleton::class.java)
    }

    @Provides
    fun migrationConfig(): FlywayMigration.Config {
        return FlywayMigration.Config("jdbc:postgresql://localhost:5432/fraud-rules-engine", "fraud-rules-engine", "fraud-rules-engine")
    }
}
