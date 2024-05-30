package com.github.iamrenny.rulesflow.vo

data class WorkflowInfo (
    val country: String,
    val version : String ? = null,
    val workflowName : String
)