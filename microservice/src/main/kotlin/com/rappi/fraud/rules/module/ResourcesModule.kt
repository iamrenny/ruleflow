package com.rappi.fraud.rules.module

import com.google.inject.Singleton
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import com.rappi.fraud.rules.services.CacheService
import com.rappi.fraud.rules.services.WorkflowService
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx

class ResourcesModule(vertx: Vertx, config: JsonObject) : AbstractModule(vertx, config) {

    override fun configure() {
        super.configure()
        bind(ActiveWorkflowRepository::class.java).`in`(Singleton::class.java)
        bind(ActiveWorkflowHistoryRepository::class.java).`in`(Singleton::class.java)
        bind(CacheService::class.java).`in`(Singleton::class.java)
        bind(WorkflowRepository::class.java).`in`(Singleton::class.java)
        bind(WorkflowService::class.java).`in`(Singleton::class.java)
    }
}
