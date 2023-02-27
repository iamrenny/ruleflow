package com.rappi.fraud.rules.entities

data class WorkflowInfo(
    val country: String,
    val version: String ? = null,
    val workflowName: String,
    val referenceIds: List<String>
)
