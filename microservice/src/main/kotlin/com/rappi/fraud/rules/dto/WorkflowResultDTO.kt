package com.rappi.fraud.rules.dto

import com.rappi.fraud.rules.entities.WorkflowResult

data class WorkflowResultDTO(
    val requestId: String,
    val workflow: String,
    val ruleSet: String? = null,
    val rule: String? = null,
    val risk: String,
    val actions: Set<String> = emptySet(),
    val warnings: Set<String> = emptySet(),
    val actionsWithParams: Map<String, Map<String, String>> = emptyMap(),
    val workflowInfo: WorkflowInfoDTO? = null,
    val actionsList: List<ActionDTO> = emptyList(),
    val error: Boolean = false
)

class WorkflowResultDTOMapper {
    fun map(workflowResult: WorkflowResult): WorkflowResultDTO {
        return WorkflowResultDTO(
            requestId = workflowResult.requestId,
            workflow = workflowResult.workflow,
            ruleSet = workflowResult.ruleSet,
            rule = workflowResult.rule,
            risk = workflowResult.risk,
            actions = workflowResult.actions,
            warnings = workflowResult.warnings,
            actionsWithParams = workflowResult.actionsWithParams,
            workflowInfo = WorkflowInfoDTOMapper().map(workflowResult.workflowInfo),
            actionsList = workflowResult.actionsList.map { ActionDTOMapper().map(it) }
        )
    }
}
