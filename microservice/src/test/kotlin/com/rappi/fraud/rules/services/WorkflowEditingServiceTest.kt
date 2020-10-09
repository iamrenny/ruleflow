package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.reactivex.redis.RedisClient
import org.junit.jupiter.api.Test

class WorkflowEditingServiceTest {
    private val redisClient: RedisClient = mock()
    private val workflowEditingService = WorkflowEditingService(redisClient)

    @Test
    fun `test lock workflow edtiting`() {
        val request = WorkflowEditingService.LockWorkflowEditingRequest(
            countryCode =  "test",
            workflowName = "test",
            user = "test"
        )

        val response = WorkflowEditingService.LockWorkflowEditingResponse(
            "OK",
            "workflow editing locked by test"
        )

        whenever(redisClient.rxExists(any())).thenReturn(Single.just(0))
        whenever(redisClient.rxSetex(any(), any(), any())).thenReturn(Single.just("OK"))

        workflowEditingService.lockWorkflowEditing(request)
            .test()
            .assertValue(response)
            .assertComplete()
    }

    @Test
    fun `when lock exists and is same user, let it be locked again`() {
        val request = WorkflowEditingService.LockWorkflowEditingRequest(
            countryCode =  "test",
            workflowName = "test",
            user = "test"
        )

        val response = WorkflowEditingService.LockWorkflowEditingResponse(
            "OK",
            "workflow editing locked by test"
        )

        whenever(redisClient.rxExists(any())).thenReturn(Single.just(1))
        whenever(redisClient.rxGet(any())).thenReturn(Maybe.just("test"))
        whenever(redisClient.rxSetex(any(), any(), any())).thenReturn(Single.just("OK"))

        workflowEditingService.lockWorkflowEditing(request)
            .test()
            .assertValue(response)
            .assertComplete()
    }

    @Test
    fun `when lock exists and is not same user, do not let it be locked again`() {
        val request = WorkflowEditingService.LockWorkflowEditingRequest(
            countryCode =  "test",
            workflowName = "test",
            user = "test"
        )

        val response = WorkflowEditingService.LockWorkflowEditingResponse(
            "NOT OK",
            "workflow editing locked by test1"
        )

        whenever(redisClient.rxExists(any())).thenReturn(Single.just(1))
        whenever(redisClient.rxGet(any())).thenReturn(Maybe.just("test1"))
        whenever(redisClient.rxSetex(any(), any(), any())).thenReturn(Single.just("OK"))

        workflowEditingService.lockWorkflowEditing(request)
            .test()
            .assertValue(response)
            .assertComplete()
    }

    @Test
    fun `test unlock workflow edtiting`() {
        val request = WorkflowEditingService.UnlockWorkflowEditingRequest(
            countryCode =  "test",
            workflowName = "test"
        )

        val response = WorkflowEditingService.LockWorkflowEditingResponse(
            "OK",
            "workflow editing unlocked"
        )

        whenever(redisClient.rxDel(any())).thenReturn(Single.just(1))

        workflowEditingService.unlockWorkflowEditing(request)
            .test()
            .assertValue(response)
            .assertComplete()
    }

    @Test
    fun `test lock when redis fail`() {
        val request = WorkflowEditingService.LockWorkflowEditingRequest(
            countryCode =  "test",
            workflowName = "test",
            user = "test"
        )

        val response = WorkflowEditingService.LockWorkflowEditingResponse(
            "NOT OK",
            "FAIL"
        )

        whenever(redisClient.rxExists(any())).thenReturn(Single.just(0))
        whenever(redisClient.rxSetex(any(), any(), any())).thenReturn(Single.just("FAIL"))

        workflowEditingService.lockWorkflowEditing(request)
            .test()
            .assertValue(response)
            .assertComplete()
    }

}