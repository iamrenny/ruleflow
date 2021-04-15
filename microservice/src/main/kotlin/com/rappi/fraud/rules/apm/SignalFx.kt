package com.rappi.fraud.rules.apm

import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.StatusCode

class SignalFx {
    companion object {
        private val MESSAGE = "message"
        private val ERROR = "error"
        private val ERROR_EXPECTED = "error.expected"
        private val ERROR_STACK = "error.stack"
        private val ERROR_KIND = "error.kind"
        private val ERROR_TYPE = "error.type"
        private val ERROR_OBJECT = "error.object"

        fun noticeError(throwable: Throwable, params: Map<String, Any> = mapOf(), expected: Boolean = false) {
            val span = Span.current()

            span.setStatus(StatusCode.ERROR)
            span.setAttribute(ERROR_KIND, throwable.javaClass.simpleName)
            span.setAttribute(ERROR_TYPE, throwable.javaClass.simpleName)
            span.setAttribute(MESSAGE, throwable.message ?: "N/A")
            span.setAttribute(ERROR_OBJECT, throwable.toString())

            span.setAttribute(ERROR_STACK, throwable.stackTrace.joinToString())
            span.setAttribute(ERROR_EXPECTED, expected)
            span.setAttribute(ERROR, true)

            addCustomParameters(params)
        }

        fun noticeError(message: String?, params: Map<String, Any> = mapOf(), expected: Boolean = false) {
            val span = Span.current()
            span.setStatus(StatusCode.ERROR)
            span.setAttribute(MESSAGE, message)
            span.setAttribute(ERROR, true)
            span.setAttribute(ERROR_EXPECTED, expected)

            addCustomParameters(params)
        }

        fun noticeError(message: String, throwable: Throwable, expected: Boolean = false) {
            val span = Span.current()
            span.setStatus(StatusCode.ERROR)
            span.setAttribute(MESSAGE, message)
            span.setAttribute(ERROR, true)
            span.setAttribute(ERROR_EXPECTED, expected)
            span.setAttribute(ERROR_KIND, throwable.javaClass.simpleName)
            span.setAttribute(ERROR_TYPE, throwable.javaClass.simpleName)
            span.setAttribute(MESSAGE, throwable.message ?: "N/A")
            span.setAttribute(ERROR_OBJECT, throwable.toString())
            span.setAttribute(ERROR_STACK, throwable.stackTrace.joinToString())
        }

        fun addCustomParameters(params: Map<String, Any?>) {
            val span = Span.current()
            params.entries.stream().forEach { (k, v) ->
                span.setAttribute(k, v.toString())
            }
        }

        fun setTransactionName(category: String, name: String) {
            val span = Span.current()
            span.updateName(name)
        }
    }
}
