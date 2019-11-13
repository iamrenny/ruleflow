package com.rappi.fraud.rules.module

import com.google.inject.Singleton
import com.rappi.fraud.rules.repositories.WorkflowRepository
import com.rappi.fraud.rules.services.WorkflowService
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx

class ResourcesModule(vertx: Vertx, config: JsonObject) : AbstractModule(vertx, config) {

    override fun configure() {
        super.configure()
        bind(WorkflowRepository::class.java).`in`(Singleton::class.java)
        bind(WorkflowService::class.java).`in`(Singleton::class.java)
    }
}
