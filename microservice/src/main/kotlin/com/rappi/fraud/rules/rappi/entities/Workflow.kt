package com.rappi.fraud.rules.rappi.entities

import io.reactiverse.reactivex.pgclient.Row
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

data class CreateWorkflowRequest(
    val workflow: String,
    val ruleset: String,
    val rules: List<CreateWorkflowRuleRequest>
) {
    fun toJson(): JsonObject {
        return JsonObject().apply {
            put("workflow", workflow)
            put("ruleset", ruleset)
            put("rules", JsonArray().apply {
                rules.forEach {
                    add(it.toJson())
                }
            })
        }
    }
}

data class CreateWorkflowRuleRequest(
    val name: String,
    val condition: String
) {
    fun toJson(): JsonObject {
        return JsonObject().apply {
            put("name", name)
            put("condition", condition)
        }
    }
}

data class GetWorkflowRequest(
    val name: String,
    val version: Long
)

data class EvaluateWorkflowRequest(
    val name: String,
    val version: Long
)

data class GetAllWorkflowRequest(
    val name: String
)

data class WorkflowResponse(
    val id: Long,
    val version: Long,
    val name: String,
    val workflow: CreateWorkflowRequest
) {
    fun toJson(): JsonObject {
        return JsonObject().apply {
            put("id", id)
            put("name", name)
            put("version", version)
            put("worflow", workflow.toJson())
        }
    }
}
data class Workflow(
    val name: String,
    val workflow: String,
    val id: Long,
    val version: Long
) {
    constructor(row: Row) : this(
        id = row.getLong("id"),
        name = row.getString("name"),
        version = row.getLong("version"),
        workflow = row.getString("workflow")
    )

    fun toJson(): JsonObject {
        return JsonObject().apply {
            put("id", id)
            put("name", name)
            put("version", version)
            put("worflow", workflow)
        }
    }
}
