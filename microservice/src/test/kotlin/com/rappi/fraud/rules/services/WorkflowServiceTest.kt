package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.repositories.WorkflowRepository
import io.reactivex.Observable
import io.reactivex.Single
import java.util.UUID
import org.junit.jupiter.api.Test

class WorkflowServiceTest {

    private val repository = mock<WorkflowRepository>()
    private val service = WorkflowService(repository)

    @Test
    fun testSave() {
        val expected = Workflow(
            id = 12,
            countryCode = "MX",
            name = "Sample",
            version = 1,
            workflow = "workflow 'Sample' ruleset 'sample' 'sample' d >= 100 return allow default block end",
            userId = UUID.randomUUID().toString()
        )

        whenever(
            repository.save(
                Workflow(
                    countryCode = expected.countryCode,
                    name = expected.name,
                    workflow = expected.workflow,
                    userId = expected.userId
                )
            )
        ).thenReturn(Single.just(expected))

        service
            .save(
                CreateWorkflowRequest(
                    countryCode = expected.countryCode,
                    workflow = expected.workflow,
                    userId = expected.userId
                )
            )
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(expected)
            .dispose()
    }

    @Test
    fun testGet() {
        val expected = Workflow(
            id = 12,
            countryCode = "MX",
            name = "Sample",
            version = 1,
            workflow = "workflow 'Sample' ruleset 'sample' 'sample' d >= 100 return allow default block end",
            userId = UUID.randomUUID().toString()
        )

        val key = WorkflowKey(
            countryCode = expected.countryCode,
            name = expected.name,
            version = expected.version!!
        )

        whenever(
            repository.get(key)
        ).thenReturn(Single.just(expected))

        service
            .get(key)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue(expected)
            .dispose()
    }

    @Test
    fun testGetAll() {
        val seed = Workflow(
            id = 12,
            countryCode = "MX",
            name = "Sample",
            version = 1,
            workflow = "workflow 'Sample' ruleset 'sample' 'sample' d >= 100 return allow default block end",
            userId = UUID.randomUUID().toString()
        )

        val expected = listOf(seed, seed.copy(id = 13, version = 2))

        val request = GetAllWorkflowRequest(
            countryCode = seed.countryCode,
            name = seed.name
        )

        whenever(
            repository.getAll(request)
        ).thenReturn(Observable.merge(expected.map { Observable.just(it) }))

        service
            .getAll(request)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValueSet(expected)
            .dispose()
    }
}
