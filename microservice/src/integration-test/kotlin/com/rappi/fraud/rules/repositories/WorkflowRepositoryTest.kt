package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test

class WorkflowRepositoryTest : BaseTest() {

    private val repository = injector.getInstance(WorkflowRepository::class.java)

    @Test
    fun testSave() {
        val expected = Workflow(
            countryCode = "MX",
            name = "Name",
            workflowAsString = "Workflow",
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
            .assertValue { it.workflowAsString == expected.workflowAsString }
            .assertValue { it.version!! > 0L }
            .assertValue { it.createdAt != null }
            .dispose()
    }

    @Test
    fun testGet() {
        val expected = Workflow(
            id = 1,
            version = 1,
            name = "Workflow 1",
            countryCode = "CO",
            workflowAsString = "Workflow \"Workflow 1\" ruleset \"test\" \"test\" total >= 100 return allow default block end",
            userId = "84b22591-7894-4063-943f-511a157409c3",
            createdAt = LocalDateTime.parse("2019-10-31T08:31:58.129"),
            lists = listOf()
        )

        repository.get(
            countryCode = expected.countryCode,
            name = expected.name,
            version = expected.version!!
        )
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
