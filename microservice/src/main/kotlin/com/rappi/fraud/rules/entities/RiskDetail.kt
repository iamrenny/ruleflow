package com.rappi.fraud.rules.entities

import io.vertx.core.json.JsonObject

data class RiskDetail(
    val id: String? = null,
    val request: JsonObject
)
