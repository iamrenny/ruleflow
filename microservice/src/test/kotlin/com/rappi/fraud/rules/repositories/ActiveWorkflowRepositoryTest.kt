package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.Workflow
import io.vertx.core.json.JsonObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ActiveWorkflowRepositoryTest : BaseTest() {

    private val activeWorkflowRepository = injector.getInstance(ActiveWorkflowRepository::class.java)
    private val workflowRepository = injector.getInstance(WorkflowRepository::class.java)
    private val workflowHistoryRepository = injector.getInstance(ActiveWorkflowHistoryRepository::class.java)

    lateinit var SEED: List<Workflow>
    lateinit var workflows: List<Workflow>

    @BeforeEach
    fun before() {
        SEED = getSeedAsJsonArray("get_all_workflow.json")
            .mapTo(mutableListOf(), { x -> (x as JsonObject).mapTo(Workflow::class.java).activate() })
            .toList()

        workflows = SEED.map { workflow ->
            workflowRepository.save(workflow)
                .blockingGet()
        }
    }

    @Test
    fun testSave() {
        val expected = Workflow(
            countryCode = workflows[0].countryCode,
            name = workflows[0].name,
            id = workflows[0].id!!,
            userId = workflows[0].userId,
            workflowAsString = workflows[0].workflowAsString,
            active = workflows[0].active
        )

        activeWorkflowRepository.save(workflows[0])
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue { actual ->
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
        activeWorkflowRepository.save(workflows[1])
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue { actual ->
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
    fun testGet() {
        activeWorkflowRepository.save(workflows[1])
            .ignoreElement()
            .andThen(
                activeWorkflowRepository
                    .get(countryCode = workflows[1].countryCode!!, name = workflows[1].name)
            )
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue { actual ->
                Assertions.assertNotNull(actual.id)
                Assertions.assertEquals(workflows[1].countryCode, actual.countryCode)
                Assertions.assertEquals(workflows[1].name, actual.name)
                Assertions.assertEquals(true, actual.active)
                Assertions.assertEquals(workflows[1].workflowAsString, actual.workflowAsString)
                Assertions.assertEquals(workflows[1].userId, actual.userId)

                true
            }
    }

    @Test
    fun testGetActiveWorkflow() {
        activeWorkflowRepository.get(countryCode = workflows[0].countryCode!!, name = workflows[0].name)
                .test()
                .assertComplete()
                .assertNoErrors()
    }

    @Test
    fun testSaveHistory() {
        val expected = workflows[1]
        workflowRepository.save(workflows[1])
            .flatMap { activeWorkflowRepository.save(it) }
            .flatMap { (workflowHistoryRepository.save(ActiveWorkflowHistory(workflowId = it.id!!, userId = "15"))) }
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue { it.id != null }
            .assertValue { it.workflowId != null }
            .assertValue { it.userId == "15" }
            .assertValue { it.createdAt != null }
            .dispose()
    }
}
