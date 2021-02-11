package com.rappi.fraud.rules.exceptions

class BadRequestException(override var message: String, override var errorCode: String? = null) :
    ErrorRequestException(message, errorCode, statusCode = 400)
