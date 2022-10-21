package com.rappi.fraud.rules.entities

import io.reactiverse.reactivex.pgclient.Row
import java.time.LocalDateTime

data class WorkflowVersion(
    val id: Long? = null,
    val version: Long? = null,
    val userId: String? = null,
    val createdAt: LocalDateTime? = null,
    val active: Boolean? = null
) {
    constructor(row: Row) : this(
        id = row.getLong("id"),
        version = row.getLong("version"),
        userId = row.getString("user_id"),
        createdAt = row.getValue("created_at") as? LocalDateTime,
        active = row.getBoolean("is_active")
    )

    fun activate(): WorkflowVersion = this.copy(active = true)
}