package com.rappi.fraud.rules.entities

import io.reactiverse.reactivex.pgclient.Row

data class GetAllWorkflowRequest(
    val countryCode: String,
    val name: String
)

data class CreateWorkflowRequest(
    val countryCode: String,
    val workflow: String,
    val userId: String
)

data class WorkflowKey(
    val countryCode: String,
    val name: String,
    val version: Long
)

data class Workflow(
    val id: Long? = null,
    val countryCode: String,
    val name: String,
    val version: Long? = null,
    val workflow: String,
    val userId: String
) {

    constructor(row: Row) : this(
        id = row.getLong("id")!!,
        name = row.getString("name")!!,
        version = row.getLong("version")!!,
        countryCode = row.getString("country_code")!!,
        workflow = row.getString("workflow")!!,
        userId = row.getString("user_id")!!
    )
}
