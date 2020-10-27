package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.reactivex.redis.RedisClient
import org.junit.jupiter.api.Test

class WorkflowEditionServiceTest {
    private val redisClient: RedisClient = mock()
    private val workflowEditingService = WorkflowEditionService(redisClient)

    @Test
    fun `test lock workflow edition`() {
        val countryCode =  "test"
        val workflowName = "test"
        val user = "test"

        val response = WorkflowEditionService.WorkflowEditionStatus(
            "OK",
            "workflow is being edited by test"
        )

        whenever(redisClient.rxExists(any())).thenReturn(Single.just(0))
        whenever(redisClient.rxSetex(any(), any(), any())).thenReturn(Single.just("OK"))

        workflowEditingService.lockWorkflowEdition(countryCode, workflowName, user)
            .test()
            .assertValue(response)
            .assertComplete()
    }

    @Test
    fun `when lock exists and is same user, let it be locked again`() {
        val countryCode =  "test"
        val workflowName = "test"
        val user = "test"

        val response = WorkflowEditionService.WorkflowEditionStatus(
            "OK",
            "workflow is being edited by test"
        )

        whenever(redisClient.rxExists(any())).thenReturn(Single.just(1))
        whenever(redisClient.rxGet(any())).thenReturn(Maybe.just("test"))
        whenever(redisClient.rxSetex(any(), any(), any())).thenReturn(Single.just("OK"))

        workflowEditingService.lockWorkflowEdition(countryCode, workflowName, user)
            .test()
            .assertValue(response)
            .assertComplete()
    }

    @Test
    fun `when lock exists and is not same user, do not let it be locked again`() {
        val countryCode =  "test"
        val workflowName = "test"
        val user = "test"

        val response = WorkflowEditionService.WorkflowEditionStatus(
            "NOT OK",
            "workflow is being edited by test1"
        )

        whenever(redisClient.rxExists(any())).thenReturn(Single.just(1))
        whenever(redisClient.rxGet(any())).thenReturn(Maybe.just("test1"))
        whenever(redisClient.rxSetex(any(), any(), any())).thenReturn(Single.just("OK"))

        workflowEditingService.lockWorkflowEdition(countryCode, workflowName, user)
            .test()
            .assertValue(response)
            .assertComplete()
    }

    @Test
    fun `test lock when redis fail`() {
        val countryCode =  "test"
        val workflowName = "test"
        val user = "test"

        val response = WorkflowEditionService.WorkflowEditionStatus(
            "NOT OK",
            "FAIL"
        )

        whenever(redisClient.rxExists(any())).thenReturn(Single.just(0))
        whenever(redisClient.rxSetex(any(), any(), any())).thenReturn(Single.just("FAIL"))

        workflowEditingService.lockWorkflowEdition(countryCode, workflowName, user)
            .test()
            .assertValue(response)
            .assertComplete()
    }

}