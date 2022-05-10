package com.rappi.fraud.rules.entities

data class Action(
    val name: String,
    val params: Map<String, String> = emptyMap()
)
