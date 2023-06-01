package com.rappi.fraud.rules.exceptions

class NotFoundException(override var message:String, override var errorCode: String?) : ErrorRequestException(message, errorCode, 404) {
}
