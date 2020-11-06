package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.ListModificationType
import com.rappi.fraud.rules.entities.Workflow
import io.vertx.core.json.JsonObject
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ActiveWorkflowRepositoryTest : BaseTest() {

    private val repository = injector.getInstance(ActiveWorkflowRepository::class.java)

    private val historyRepository = injector.getInstance(ListHistoryRepository::class.java)


    companion object {
        private val SEED = getSeedAsJsonArray("get_all_workflow.json")
            .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java) })
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
            active = true
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
        val expected = Workflow(
            countryCode = SEED[1].countryCode,
            name = SEED[1].name,
            id = SEED[1].id!!,
            userId = SEED[1].userId,
            workflowAsString = SEED[1].workflowAsString,
            active = true
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
        repository.get(countryCode = SEED[0].countryCode!!, name = SEED[0].name)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertResult(
                Workflow(
                    countryCode = SEED[1].countryCode,
                    name = SEED[1].name,
                    userId = SEED[1].userId,
                    id = SEED[1].id!!,
                    version = SEED[1].version,
                    workflowAsString = SEED[1].workflowAsString,
                    createdAt = SEED[1].createdAt,
                    active = true,
                    lists = listOf()
                )
            )
    }

    @Test
    @Order(4)
    fun testGetActiveWorkflow() {
        repository.get(countryCode = SEED[0].countryCode!!, name = SEED[0].name)
                .test()
    }

    @Test
    fun testListHistoryRepository() {

        historyRepository.save(1, ListModificationType.ADD_ITEMS, "", JsonObject())

        historyRepository.getListHistory(1)
    }
}
