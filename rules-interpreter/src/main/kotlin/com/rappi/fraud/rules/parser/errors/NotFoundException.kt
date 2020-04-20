package com.rappi.fraud.rules.parser.errors

class NotFoundException(override var message:String, override var errorCode: String) : ErrorRequestException(message, errorCode, 404) {
}
