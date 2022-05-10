package com.rappi.fraud.rules.dto

import com.rappi.fraud.rules.documentdb.WorkflowResponse
import io.vertx.core.json.JsonObject

data class WorkflowResponseDTO(
    val id: String,
    val request: JsonObject,
    val response: JsonObject,
    val receivedAt: String? = null,
    val countryCode: String,
    val workflowName: String,
    val referenceId: String? = null,
    val error: Boolean = false
)

object WorkflowResponseDTOMapper {
    fun map(workflowResponse: WorkflowResponse): WorkflowResponseDTO {
        return WorkflowResponseDTO(
            id = workflowResponse.id,
            request = workflowResponse.request,
            response = workflowResponse.response,
            receivedAt = workflowResponse.receivedAt,
            countryCode = workflowResponse.countryCode,
            workflowName = workflowResponse.workflowName,
            referenceId = workflowResponse.referenceId,
            error = workflowResponse.error
        )
    }
}
