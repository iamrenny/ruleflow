package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import io.reactivex.Single
import javax.inject.Inject

class ActiveWorkflowHistoryRepository @Inject constructor(private val database: Database) {

    fun save(activeWorkflowHistory: ActiveWorkflowHistory): Single<ActiveWorkflowHistory> {
        val insertActiveWorkflowHistory = """
            INSERT INTO active_workflows_history (workflow_id, user_id) 
                 VALUES ($1, $2)
              RETURNING id, workflow_id, user_id, created_at
        """

        val params = listOf(
                activeWorkflowHistory.workflowId,
                activeWorkflowHistory.userId
        )
        return database.executeWithParams(insertActiveWorkflowHistory, params)
                .map { ActiveWorkflowHistory(it) }
    }
}
