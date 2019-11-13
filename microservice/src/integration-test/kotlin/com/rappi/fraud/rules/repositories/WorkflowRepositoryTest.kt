package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowKey
import io.vertx.core.json.JsonObject
import java.util.UUID
import org.junit.jupiter.api.Test

class WorkflowRepositoryTest : BaseTest() {

    private val repository = injector.getInstance(WorkflowRepository::class.java)

    @Test
    fun testSave() {
        val expected = Workflow(
            countryCode = "MX",
            name = "Name",
            workflow = "Workflow",
            userId = UUID.randomUUID().toString()
        )
        repository.save(expected)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue { it.id != null }
            .assertValue { it.countryCode == expected.countryCode }
            .assertValue { it.name == expected.name }
            .assertValue { it.workflow == expected.workflow }
            .assertValue { it.version!! > 0L }
            .dispose()
    }

    @Test
    fun testGet() {
        val expected = getSeedAsJsonObject("get_workflow.json").mapTo(Workflow::class.java)

        val key = WorkflowKey(
            countryCode = expected.countryCode,
            name = expected.name,
            version = expected.version!!
        )
        repository.get(key)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(expected)
            .dispose()
    }

    @Test
    fun testGetAll() {
        val expected = getSeedAsJsonArray("get_all_workflow.json")
            .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java) })
            .toList()

        val request = GetAllWorkflowRequest(
            countryCode = expected[0].countryCode,
            name = expected[1].name
        )
        repository.getAll(request)
            .test()
            .assertSubscribed()
            .await()
            .assertValueSequence(expected.sortedByDescending { it.id })
            .dispose()
    }
}
