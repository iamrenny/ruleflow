package com.rappi.fraud.rules.errors

import com.fasterxml.jackson.annotation.JsonProperty

class ErrorObject(var message: Any, var cause: Any, @JsonProperty("status_code") val statusCode: Int)
