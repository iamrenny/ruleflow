package com.rappi.fraud.rules.dto

import com.rappi.fraud.rules.entities.WorkflowInfo

data class WorkflowInfoDTO(
    val country: String,
    val version: String ? = null,
    val workflowName: String
)

class WorkflowInfoDTOMapper {
    fun map(workflowInfo: WorkflowInfo?): WorkflowInfoDTO? {
        if (workflowInfo == null)
            return null
        return WorkflowInfoDTO(workflowInfo.country, workflowInfo.version, workflowInfo.workflowName)
    }
}
