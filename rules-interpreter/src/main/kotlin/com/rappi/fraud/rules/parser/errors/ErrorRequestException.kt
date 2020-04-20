package com.rappi.fraud.rules.parser.errors

open class ErrorRequestException(override var message:String, open var errorCode:String, var statusCode:Int): Throwable(message)
