package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.entities.ActiveWorkflowHistory
import io.reactivex.Single
import javax.inject.Inject

class ActiveWorkflowHistoryRepository @Inject constructor(private val database: Database) {

    companion object {
        private const val INSERT = """
            INSERT INTO active_workflows_history (workflow_id, user_id) 
                 VALUES ($1, $2)
              RETURNING id, workflow_id, user_id, created_at
        """
    }

    fun save(activeWorkflowHistory: ActiveWorkflowHistory): Single<ActiveWorkflowHistory> {
        val params = listOf(
                activeWorkflowHistory.workflowId,
                activeWorkflowHistory.userId
        )
        return database.executeWithParams(INSERT, params)
                .map { ActiveWorkflowHistory(it) }
    }
}
