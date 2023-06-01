package com.rappi.analang.vo

data class WorkflowInfo (
    val country: String,
    val version : String ? = null,
    val workflowName : String
)