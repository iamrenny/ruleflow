package com.rappi.analang.vo

data class Action (
    val name: String,
    val params: Map<String, String> = emptyMap()
)