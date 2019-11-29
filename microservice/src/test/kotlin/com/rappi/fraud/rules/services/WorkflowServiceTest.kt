package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.ActiveWorkflow
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.repositories.ActiveWorkflowHistoryRepository
import com.rappi.fraud.rules.repositories.ActiveWorkflowRepository
import com.rappi.fraud.rules.repositories.WorkflowRepository
import io.reactivex.Observable
import io.reactivex.Single
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test

class WorkflowServiceTest {

    private val activeWorkflowRepository = mock<ActiveWorkflowRepository>()
    private val activeWorkflowHistoryRepository = mock<ActiveWorkflowHistoryRepository>()
    private val workflowRepository = mock<WorkflowRepository>()
    private val service = WorkflowService(activeWorkflowRepository, activeWorkflowHistoryRepository, workflowRepository)

    @Test
    fun testSave() {
        val expected = baseWorkflow()

        whenever(workflowRepository
                .save(Workflow(
                        countryCode = expected.countryCode,
                        name = expected.name,
                        workflow = expected.workflow,
                        userId = expected.userId
                )))
                .thenReturn(Single.just(expected))

        service
                .save(CreateWorkflowRequest(
                        countryCode = expected.countryCode,
                        workflow = expected.workflow,
                        userId = expected.userId))
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(expected)
                .dispose()
    }

    @Test
    fun testGet() {
        val expected = baseWorkflow()

        val key = WorkflowKey(
                countryCode = expected.countryCode,
                name = expected.name,
                version = expected.version!!)

        whenever(workflowRepository.get(key))
                .thenReturn(Single.just(expected))

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
        val base = baseWorkflow()

        val expected = listOf(base, base.copy(id = 13, version = 2))

        val request = GetAllWorkflowRequest(
                countryCode = base.countryCode,
                name = base.name)

        whenever(workflowRepository.getAll(request))
                .thenReturn(Observable.merge(expected.map { Observable.just(it) }))

        service
                .getAll(request)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValueSet(expected)
                .dispose()
    }

    @Test
    fun testActivate() {
        val expected = baseWorkflow()

        val request = ActivateRequest(
                key = WorkflowKey(
                        countryCode = expected.countryCode,
                        name = expected.name,
                        version = expected.version!!
                ),
                userId = UUID.randomUUID().toString())

        whenever(workflowRepository
                .get(WorkflowKey(
                        countryCode = expected.countryCode,
                        name = expected.name,
                        version = expected.version!!)))
                .thenReturn(Single.just(expected))

        val activeWorkflow = ActiveWorkflow(
                countryCode = expected.countryCode,
                name = expected.name,
                workflowId = expected.id!!)
        whenever(activeWorkflowRepository
                .save(activeWorkflow))
                .thenReturn(Single.just(activeWorkflow))

        whenever(activeWorkflowHistoryRepository
                .save(ActiveWorkflowHistory(
                    workflowId = expected.id!!,
                    userId = request.userId
                )))
            .thenReturn(Single.just(ActiveWorkflowHistory(
                id = 1,
                workflowId = expected.id!!,
                userId = request.userId,
                createdAt = LocalDateTime.now()
            )))

        service
                .activate(request)
                .test()
                .assertSubscribed()
                .await()
                .assertComplete()
                .assertValue(expected)
                .assertOf {
                    verify(activeWorkflowHistoryRepository).save(ActiveWorkflowHistory(
                            workflowId = expected.id!!,
                            userId = request.userId
                    ))
                }
                .dispose()
    }

    private fun baseWorkflow(): Workflow {
        return Workflow(
                id = 12,
                countryCode = "MX",
                name = "Sample",
                version = 1,
                workflow = "workflow 'Sample' ruleset 'sample' 'sample' d >= 100 return allow default block end",
                userId = UUID.randomUUID().toString()
        )
    }
}
