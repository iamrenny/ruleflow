package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.documentdb.DocumentDb
import com.rappi.fraud.rules.documentdb.DocumentDbDataRepository
import com.rappi.fraud.rules.documentdb.EventData
import com.rappi.fraud.rules.entities.RulesEngineHistoryRequest
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DocumentDbDataRepositoryTests : BaseTest() {

    private val repository = injector.getInstance(DocumentDbDataRepository::class.java)

    @AfterEach
    fun clean() {
        val db = injector.getInstance(DocumentDb::class.java)
        db.delegate.rxDropCollection(repository.collection).blockingGet()
    }

    @Test
    fun testSaveEventData() {
        val json = getSeedAsJsonObject("simulate_workflow.json")

        val data = EventData(
            request = json,
            response = JsonObject(),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "login"
        )

        repository.saveEventData(data)
            .test()
            .await()
            .assertComplete()
            .assertValue {
                Assertions.assertEquals(repository.findReferenceId(data), "101000004184")
                it.id!!.isNotBlank()
            }
    }

    @Test
    fun testSaveCourierEventData() {
        val json = getSeedAsJsonObject("handshake_workflow.json")

        val data = EventData(
            request = json,
            response = JsonObject(),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "handshake"
        )

        repository.saveEventData(data)
            .test()
            .await()
            .assertComplete()
            .assertValue {
                Assertions.assertEquals(repository.findReferenceId(data), "13007")
                it.id!!.isNotBlank()
            }
    }

    @Test
    fun testSaveAndFindEventData() {
        val json = getSeedAsJsonObject("simulate_workflow.json")
        val data = EventData(
            request = json,
            response = JsonObject().put("response", "OK"),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "login"
        )

        repository.saveEventData(data).flatMap {
            repository.find(it.id!!)
        }.test()
            .await()
            .assertComplete()
            .assertValue {
                data.request == it.request &&
                        data.response == it.response &&
                        "101000004184" == it.referenceId
            }
    }

    @Test
    fun getEvaluationHistoryData() {
        val data = RulesEngineHistoryRequest(
            endDate = LocalDateTime.now(),
            startDate = LocalDateTime.now(),
            countryCode = "co",
            workflowName = "login"
        )

        repository.getRiskDetailHistoryFromDocDb(data).test().await().assertComplete().assertValue {
            it.isEmpty()
        }
    }

    @Test
    fun `trying To Find In List With empty RefIds Array Returns Empty List`() {

        repository.findInList(emptyList()).test()
            .await()
            .assertValue(emptyList())
    }

    @Test
    fun `trying To Find In List With not empty RefIds Array Returns Not Empty List`() {
        val json = getSeedAsJsonObject("simulate_workflow.json")

        val data = EventData(
            id = "3",
            request = json,
            response = JsonObject().put("response", "OK"),
            countryCode = "DEV",
            workflowName = "create_order"
        )

        repository.saveEventData(data)
            .doOnSuccess {
                repository.findInList(listOf("3")).test()
                    .await()
                    .assertValue { it.isNotEmpty() }
            }
    }

    @Test
    fun tryingToFindNonExistingRequestIdWillFail() {

        repository.find("121qwa23").test()
            .await()
            .assertError(DocumentDbDataRepository.NoRequestIdDataWasFound::class.java)
    }

    @Test
    fun `Finding order information with create_order workflow query`() {
        val json = getSeedAsJsonObject("simulate_workflow.json")

        val data = EventData(
            id = "3",
            request = json,
            response = JsonObject().put("response", "OK"),
            countryCode = "DEV",
            workflowName = "create_order"
        )

        repository.saveEventData(data)
            .doOnSuccess {
                repository.findInList(listOf("3"), "create_order").test()
                    .await()
                    .assertValue { it.isNotEmpty() }
            }
    }

    @Test
    fun `Not finding order information with create_order workflow query`() {
        val json = getSeedAsJsonObject("simulate_workflow.json")

        val data = EventData(
            id = "3",
            request = json,
            response = JsonObject().put("response", "OK"),
            countryCode = "DEV",
            workflowName = "any_workflow_name"
        )

        repository.saveEventData(data)
            .doOnSuccess {
                repository.findInList(listOf("3"), "create_order").test()
                    .await()
                    .assertValue { it.isEmpty() }
            }
    }

    @Test
    fun `Finding order information with countryCode workflow query`() {
        val json = getSeedAsJsonObject("simulate_workflow.json")

        val data = EventData(
            id = "3",
            request = json,
            response = JsonObject().put("response", "OK"),
            countryCode = "dev",
            workflowName = "create_order"
        )

        repository.saveEventData(data)
            .doOnSuccess {
                repository.findInList(listOf("3"), "create_order", "dev").test()
                    .await()
                    .assertValue { it.isNotEmpty() }
            }
    }
}
