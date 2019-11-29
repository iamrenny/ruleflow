package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.ActiveKey
import com.rappi.fraud.rules.entities.ActiveWorkflow
import com.rappi.fraud.rules.entities.Workflow
import io.vertx.core.json.JsonObject
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ActiveWorkflowRepositoryTest : BaseTest() {

    private val repository = injector.getInstance(ActiveWorkflowRepository::class.java)

    companion object {
        private val SEED = getSeedAsJsonArray("get_all_workflow.json")
            .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java) })
            .toList()
    }

    @Test
    @Order(1)
    fun testSave() {
        val expected = ActiveWorkflow(
            countryCode = SEED[0].countryCode,
            name = SEED[0].name,
            workflowId = SEED[0].id!!
        )
        repository.save(expected)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(expected)
            .dispose()
    }

    @Test
    @Order(2)
    fun testSaveWithConflict() {
        val expected = ActiveWorkflow(
            countryCode = SEED[1].countryCode,
            name = SEED[1].name,
            workflowId = SEED[1].id!!
        )
        repository.save(expected)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(expected)
            .dispose()
    }

    @Test
    @Order(3)
    fun testGet() {
        repository.get(ActiveKey(countryCode = SEED[0].countryCode, name = SEED[0].name))
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertResult(
                ActiveWorkflow(
                    countryCode = SEED[1].countryCode,
                    name = SEED[1].name,
                    workflowId = SEED[1].id!!,
                    workflow = SEED[1].workflow
                )
            )
    }
}
