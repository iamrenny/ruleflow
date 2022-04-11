package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.documentdb.DocumentDb
import com.rappi.fraud.rules.documentdb.DocumentDbDataRepository
import com.rappi.fraud.rules.documentdb.EventData
import com.rappi.fraud.rules.entities.RulesEngineHistoryRequest
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DocumentDbDataRepositoryTests : BaseTest() {


    private val documentDb = mockk<DocumentDb>(relaxed = true)
    private val config = mockk<DocumentDbDataRepository.Config>(relaxed = true)
    private val repository = DocumentDbDataRepository(documentDb,config)

    @Test
    fun testSaveEventData() {
        val json = getSeedAsJsonObject("simulate_workflow.json")
        val jsonResponse = getSeedAsJsonObject("saveEventData.json")

        val data = EventData(
            request = json,
            response = JsonObject(),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "login"
        )

        every {
            documentDb.save(any(),any())
        } returns Maybe.just(jsonResponse)

        repository.saveEventData(data)
            .test()
            .await()
            .assertComplete()
            .assertValue {
                Assertions.assertEquals(repository.findReferenceId(data), "101000004184")
                it.id!!.isNotBlank()
                !it.error
            }
    }

    @Test
    fun testSaveCourierEventData() {
        val json = getSeedAsJsonObject("handshake_workflow.json")
        val jsonResponse = getSeedAsJsonObject("saveCourierEvent.json")

        val data = EventData(
            request = json,
            response = JsonObject(),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "handshake"
        )

        every {
            documentDb.save(any(),any())
        } returns Maybe.just(jsonResponse)


        repository.saveEventData(data)
            .test()
            .await()
            .assertComplete()
            .assertValue {
                Assertions.assertEquals(repository.findReferenceId(data), "13007")
                it.id!!.isNotBlank()
                !it.error
            }
    }

    @Test
    fun testSaveAndFindEventData() {
        val json = getSeedAsJsonObject("simulate_workflow.json")
        val jsonResponse = getSeedAsJsonObject("saveEventData.json")
        val data = EventData(
            request = json,
            response = JsonObject().put("response", "OK"),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "login"
        )

        every {
            documentDb.save(any(),any())
        } returns Maybe.just(jsonResponse)
        every {
            documentDb.find(any(),any(),any())
        } returns Single.just(listOf(jsonResponse))

        repository.saveEventData(data).flatMap { resultSave ->
            repository.find(resultSave.id!!).map{ response->
                response.request
            }
        }.test().assertComplete().assertValue {
            data.request == it
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
        every {
            documentDb.findBatch(any(),any(),any())
        } returns Single.just(emptyList())

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

        every {
            documentDb.find(any(),any(),any())
        } returns Single.error(DocumentDbDataRepository.NoRequestIdDataWasFound())

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
