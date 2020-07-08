package com.rappi.fraud.rules.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.rappi.fraud.rules.entities.serializers.LocalDateTimeDeserializer
import com.rappi.fraud.rules.entities.serializers.LocalDateTimeSerializer
import com.rappi.fraud.rules.parser.WorkflowEvaluator
import io.reactiverse.reactivex.pgclient.Row
import java.time.LocalDateTime

data class GetAllWorkflowRequest(
    val countryCode: String,
    val name: String
)

data class GetListOfAllWorkflowsRequest(
    val countryCode: String
)

data class CreateWorkflowRequest(
    val countryCode: String,
    val workflow: String,
    val userId: String
)

data class ActivateRequest(
    val userId: String,
    val countryCode: String,
    val name: String,
    val version: Long
)

data class Workflow(
    val id: Long? = null,
    val countryCode: String,
    val name: String,
    val version: Long? = null,
    @JsonProperty("workflow")
    val workflowAsString: String,
    val userId: String,
    @JsonIgnore val lists: List<RulesEngineList>? = listOf(),
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = null,
    val active: Boolean = false
) {
    constructor(row: Row) : this(
        id = row.getLong("id")!!,
        name = row.getString("name")!!,
        version = row.getLong("version")!!,
        countryCode = row.getString("country_code")!!,
        workflowAsString = row.getString("workflow")!!,
        userId = row.getString("user_id")!!,
        createdAt = row.getValue("created_at") as? LocalDateTime,
        lists = listOf<RulesEngineList>()
    )

    val evaluator @JsonIgnore get() = WorkflowEvaluator(this.workflowAsString)

    fun activate(): Workflow = this.copy(active = true)
}

data class WorkflowInfo(
    val name: String,
    val version: Long? = null
) {
    constructor(row: Row) : this(
        name = row.getString("name")!!,
        version = row.getLong("version")!!
    )
}
