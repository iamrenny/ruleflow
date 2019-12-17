package com.rappi.fraud.rules.services

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.parser.RuleEngine
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CacheServiceTest : BaseTest() {

    private val service = injector.getInstance(CacheService::class.java)

    companion object {
        private val SEED_KEY = WorkflowKey(countryCode = "MX", name = "Sample", version = 1)
        private val SEED_ENGINE = RuleEngine("workflow 'Sample' ruleset 'Sample' 'test' d > 100 return allow default block end")
    }

    @Test
    @Order(1)
    fun testSet() {
        service.set(SEED_KEY, SEED_ENGINE)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .dispose()
    }

    @Test
    @Order(2)
    fun testGet() {
        service.get(SEED_KEY)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue { it.workflow == SEED_ENGINE.workflow }
                .dispose()
    }
}
