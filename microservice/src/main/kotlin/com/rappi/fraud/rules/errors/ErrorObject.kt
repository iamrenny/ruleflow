package com.rappi.fraud.rules.errors

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ErrorObject(var message: Any?, var cause: Any?, @JsonProperty("status_code") val statusCode: Int)
