package com.rappi.fraud.rules.entities

import io.reactiverse.reactivex.pgclient.Row

data class ActiveWorkflow(
    val countryCode: String,
    val name: String,
    val workflowId: Long,
    val workflow: String? = null
) {

    constructor(row: Row) : this(
            countryCode = row.getString("country_code"),
            name = row.getString("name"),
            workflowId = row.getLong("workflow_id"),
            workflow = row.getString("workflow")
    )
}

data class ActiveKey(
    val countryCode: String,
    val name: String
)
