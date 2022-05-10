package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.documentdb.DocumentDb
import com.rappi.fraud.rules.documentdb.DocumentDbDataRepository
import com.rappi.fraud.rules.documentdb.WorkflowResponse
import com.rappi.fraud.rules.entities.RulesEngineHistoryRequest
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime
import org.junit.jupiter.api.Test

class DocumentDbDataRepositoryTests : BaseTest() {

    private val documentDb = mockk<DocumentDb>(relaxed = true)
    private val config = mockk<DocumentDbDataRepository.Config>(relaxed = true)
    private val repository = DocumentDbDataRepository(documentDb, config)

    @Test
    fun testSaveEventData() {
        val json = getSeedAsJsonObject("simulate_workflow.json")
        val jsonResponse = getSeedAsJsonObject("saveEventData.json")

        val data = WorkflowResponse(
            id = "",
            request = json,
            response = JsonObject(),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "login"
        )

        every {
            documentDb.insert(any(), any())
        } returns Completable.complete()

        repository.save(data)
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun testSaveCourierEventData() {
        val json = getSeedAsJsonObject("handshake_workflow.json")
        val jsonResponse = getSeedAsJsonObject("saveCourierEvent.json")

        val data = WorkflowResponse(
            id = "",
            request = json,
            response = JsonObject(),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "handshake"
        )

        every {
            documentDb.insert(any(), any())
        } returns Completable.complete()

        repository.save(data)
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun testSaveAndFindEventData() {
        val json = getSeedAsJsonObject("simulate_workflow.json")
        val jsonResponse = getSeedAsJsonObject("saveEventData.json")
        val data = WorkflowResponse(
            id = "id",
            request = json,
            response = JsonObject().put("response", "OK"),
            receivedAt = LocalDateTime.now().toString(),
            countryCode = "co",
            workflowName = "login"
        )

        every {
            documentDb.insert(any(), any())
        } returns Completable.complete()
        every {
            documentDb.find(any(), any())
        } returns Maybe.just(jsonResponse)

        repository.save(data).andThen { resultSave ->
            repository.find(data.id!!).map { response ->
                response.request
            }
        }.test()
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
            documentDb.findBatch(any(), any(), any())
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

        val data = WorkflowResponse(
            id = "3",
            request = json,
            response = JsonObject().put("response", "OK"),
            countryCode = "DEV",
            workflowName = "create_order"
        )

        repository.save(data)
            .doOnComplete() {
                repository.findInList(listOf("3")).test()
                    .await()
                    .assertValue { it.isNotEmpty() }
            }
            .test()
            .await()
    }

    @Test
    fun tryingToFindNonExistingRequestIdWillFail() {

        every {
            documentDb.find(any(), any())
        } returns Maybe.empty()

        repository.find("121qwa23").test()
            .await()
            .assertComplete()
    }

    @Test
    fun `Finding order information with create_order workflow query`() {
        val json = getSeedAsJsonObject("simulate_workflow.json")

        val data = WorkflowResponse(
            id = "3",
            request = json,
            response = JsonObject().put("response", "OK"),
            countryCode = "DEV",
            workflowName = "create_order"
        )

        repository.save(data)
            .doOnComplete {
                repository.findInList(listOf("3"), "create_order").test()
                    .await()
                    .assertValue { it.isNotEmpty() }
            }
            .test()
            .await()
    }

    @Test
    fun `Not finding order information with create_order workflow query`() {
        val json = getSeedAsJsonObject("simulate_workflow.json")

        val data = WorkflowResponse(
            id = "3",
            request = json,
            response = JsonObject().put("response", "OK"),
            countryCode = "DEV",
            workflowName = "any_workflow_name"
        )

        repository.save(data)
            .doOnComplete() {
                repository.findInList(listOf("3"), "create_order").test()
                    .await()
                    .assertValue { it.isEmpty() }
            }
            .test()
            .await()
    }

    @Test
    fun `Finding order information with countryCode workflow query`() {
        val json = getSeedAsJsonObject("simulate_workflow.json")

        val data = WorkflowResponse(
            id = "3",
            request = json,
            response = JsonObject().put("response", "OK"),
            countryCode = "dev",
            workflowName = "create_order"
        )

        repository.save(data)
            .doOnComplete() {
                repository.findInList(listOf("3"), "create_order", "dev").test()
                    .await()
                    .assertValue { it.isNotEmpty() }
            }
            .test()
            .await()
    }
}
