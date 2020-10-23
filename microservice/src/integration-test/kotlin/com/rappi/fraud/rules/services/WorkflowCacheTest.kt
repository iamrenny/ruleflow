package com.rappi.fraud.rules.services

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.parser.WorkflowEvaluator
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WorkflowCacheTest : BaseTest() {

    private val service = injector.getInstance(WorkflowCache::class.java)

    companion object {
        private val SEED_KEY = Workflow(countryCode = "MX", name = "Sample", version = 1, userId = "123", workflowAsString = "workflow 'Sample' ruleset 'Sample' 'test' d > 100 return allow default block end")
        private val SEED_ENGINE = WorkflowEvaluator("workflow 'Sample' ruleset 'Sample' 'test' d > 100 return allow default block end")
    }

    @Test
    @Order(1)
    fun testSet() {
        service.set(SEED_KEY)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .dispose()
    }

    @Test
    @Order(2)
    fun testGet() {
        service.get(countryCode = "MX", name = "Sample", version = 1)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue { it.workflowAsString == SEED_ENGINE.workflow }
                .dispose()
    }

    @Test
    @Order(3)
    fun testExists() {
        service.exists(countryCode = "MX", name = "Sample", version = 1)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .dispose()
    }
}
