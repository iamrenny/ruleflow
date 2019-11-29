package com.rappi.fraud.rules.entities

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.rappi.fraud.rules.entities.serializers.LocalDateTimeDeserializer
import com.rappi.fraud.rules.entities.serializers.LocalDateTimeSerializer
import io.reactiverse.reactivex.pgclient.Row
import java.time.LocalDateTime

data class GetAllWorkflowRequest(
    val countryCode: String,
    val name: String
)

data class CreateWorkflowRequest(
    val countryCode: String,
    val workflow: String,
    val userId: String
)

data class ActivateRequest(
    val key: WorkflowKey,
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
    val userId: String,
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = null
) {

    constructor(row: Row) : this(
        id = row.getLong("id")!!,
        name = row.getString("name")!!,
        version = row.getLong("version")!!,
        countryCode = row.getString("country_code")!!,
        workflow = row.getString("workflow")!!,
        userId = row.getString("user_id")!!,
        createdAt = row.getValue("created_at") as? LocalDateTime
    )
}
