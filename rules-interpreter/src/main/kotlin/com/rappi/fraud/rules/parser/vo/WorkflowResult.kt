package com.rappi.fraud.rules.parser.vo

data class WorkflowResult(
    val workflow: String,
    val ruleSet: String? = null,
    val rule: String? = null,
    val risk: String
)