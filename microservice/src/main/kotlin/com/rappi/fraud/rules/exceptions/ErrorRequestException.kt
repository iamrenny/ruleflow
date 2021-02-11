package com.rappi.fraud.rules.exceptions

import io.vertx.core.impl.NoStackTraceThrowable

open class ErrorRequestException(override var message: String, open var errorCode: String? = null, var statusCode: Int) : NoStackTraceThrowable(message) {
    enum class ErrorCode {
        TIMEOUT
    }
}
