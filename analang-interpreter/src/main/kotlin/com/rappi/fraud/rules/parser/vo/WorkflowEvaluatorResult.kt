package com.rappi.fraud.rules.parser.vo

data class WorkflowEvaluatorResult(
    val workflow: String,
    val ruleSet: String? = null,
    val rule: String? = null,
    val risk: String,
    val actions: Set<String> = emptySet(),
    val warnings: Set<String> = emptySet(),
    val actionsWithParams: Map<String, Map<String, String>> = emptyMap(),
    val workflowInfo : WorkflowInfo? = null,
    val actionsList: List<Action> = emptyList(),
    val error: Boolean = false
)