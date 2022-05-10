package com.rappi.fraud.rules.parser.vo

data class WorkflowInfo (
    val country: String,
    val version : String ? = null,
    val workflowName : String
)