package com.rappi.fraud.rules.parser.vo

data class Action (
    val name: String,
    val params: Map<String, String> = emptyMap()
)