package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import java.util.UUID
import org.junit.jupiter.api.Test

class ActiveWorkflowHistoryRepositoryTest : BaseTest() {

    private val repository = injector.getInstance(ActiveWorkflowHistoryRepository::class.java)

    @Test
    fun testSave() {
        val expected = ActiveWorkflowHistory(
            workflowId = 1,
            userId = UUID.randomUUID().toString()
        )
        repository.save(expected)
            .test()
            .assertSubscribed()
            .await()
            .assertComplete()
            .assertValue { it.id != null }
            .assertValue { it.workflowId == expected.workflowId }
            .assertValue { it.userId == expected.userId }
            .assertValue { it.createdAt != null }
            .dispose()
    }
}
