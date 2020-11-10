package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.ListModificationType
import com.rappi.fraud.rules.entities.Workflow
import io.vertx.core.json.JsonObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ActiveWorkflowRepositoryTest : BaseTest() {

    private val activeWorkflowRepository = injector.getInstance(ActiveWorkflowRepository::class.java)


    companion object {
        private val SEED = getSeedAsJsonArray("get_all_workflow.json")
            .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java).activate() })
            .toList()
    }

    @Test
    @Order(1)
    fun testSave() {
        val expected = Workflow(
            countryCode = SEED[0].countryCode,
            name = SEED[0].name,
            id = SEED[0].id!!,
            userId = SEED[0].userId,
            workflowAsString = SEED[0].workflowAsString,
            active = SEED[0].active
        )

        activeWorkflowRepository.save(SEED[0])
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue {  actual  ->
                Assertions.assertNotNull(actual.id)
                Assertions.assertEquals(expected.countryCode, actual.countryCode)
                Assertions.assertEquals(expected.name, actual.name)
                Assertions.assertEquals(true, actual.active)
                Assertions.assertEquals(expected.workflowAsString, actual.workflowAsString)
                Assertions.assertEquals(expected.userId, actual.userId)

                true
            }
            .dispose()
    }

    @Test
    @Order(2)
    fun testSaveWithConflict() {
        val expected = Workflow(
            countryCode = SEED[1].countryCode,
            name = SEED[1].name,
            version = SEED[1].version,
            id = SEED[1].id!!,
            userId = SEED[1].userId,
            createdAt = SEED[1].createdAt,
            workflowAsString = SEED[1].workflowAsString,
            active = true
        )
        activeWorkflowRepository.save(SEED[1])
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
        activeWorkflowRepository.get(countryCode = SEED[1].countryCode!!, name = SEED[1].name)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertResult(SEED[1])
    }

    @Test
    @Order(4)
    fun testGetActiveWorkflow() {
        activeWorkflowRepository.get(countryCode = SEED[0].countryCode!!, name = SEED[0].name)
                .test()
                .assertComplete()
                .assertNoErrors()
    }
}
