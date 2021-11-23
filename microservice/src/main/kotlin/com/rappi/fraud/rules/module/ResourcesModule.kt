package com.rappi.fraud.rules.module

import com.rappi.fraud.rules.lists.cache.EventsListener
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.ListHistoryRepository
import com.rappi.fraud.rules.repositories.ListRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import com.rappi.fraud.rules.services.WorkflowService
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx

class ResourcesModule(vertx: Vertx, config: JsonObject) : AbstractModule(vertx, config) {

    override fun configure() {
        super.configure()
        bind(ActiveWorkflowRepository::class.java).asEagerSingleton()
        bind(ActiveWorkflowHistoryRepository::class.java).asEagerSingleton()
        bind(WorkflowRepository::class.java).asEagerSingleton()
        bind(WorkflowService::class.java).asEagerSingleton()
        bind(ListRepository::class.java).asEagerSingleton()
        bind(ListHistoryRepository::class.java).asEagerSingleton()
        bind(EventsListener::class.java).asEagerSingleton()
    }
}
