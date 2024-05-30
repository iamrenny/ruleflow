package com.github.iamrenny.rulesflow.vo

data class Action (
    val name: String,
    val params: Map<String, String> = emptyMap()
)