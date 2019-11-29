package com.rappi.fraud.rules.entities

import io.reactiverse.reactivex.pgclient.Row
import java.time.LocalDateTime

data class ActiveWorkflowHistory(
    val id: Long? = null,
    val workflowId: Long,
    val userId: String,
    val createdAt: LocalDateTime? = null
) {

    constructor(row: Row) : this(
            id = row.getLong("id"),
            workflowId = row.getLong("workflow_id"),
            userId = row.getString("user_id"),
            createdAt = row.getValue("created_at") as? LocalDateTime
    )
}
