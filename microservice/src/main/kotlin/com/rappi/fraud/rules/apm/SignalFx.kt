package com.rappi.fraud.rules.apm

import com.signalfx.tracing.context.TraceScope
import io.opentracing.Span
import io.opentracing.Tracer
import io.opentracing.util.GlobalTracer


class SignalFx {
    companion object {
        private val MESSAGE = "message"
        private val ERROR = "error"
        private val ERROR_EXPECTED = "error.expected"
        private val ERROR_STACK = "sfx.error.stack"
        private val ERROR_KIND = "error.kind"
        private val ERROR_TYPE = "sfx.error.type"
        private val ERROR_OBJECT = "sfx.error.object"
        private val SFX_ERROR_KIND = "sfx.error.kind"




        @Deprecated("This is a noop. Use grafana + prometheus instead.")
        fun recordMetric(name: String, value: Double) {
        }

        @Deprecated("This is a noop. Use grafana + prometheus instead.")
        fun recordResponseTimeMetric(name: String, millis: Long) {
        }

        @Deprecated("This is a noop. Use grafana + prometheus instead.")
        fun incrementCounter(name: String, count: Int = 1) {
        }

        fun ignoreTransaction() {
            // TODO: see if this is necessary
        }

        fun noticeError(throwable: Throwable, params: Map<String, Any> = mapOf(), expected: Boolean = false) {
            applyCurrentSpan { span ->
                span.setTag(SFX_ERROR_KIND, throwable.javaClass.simpleName)
                span.setTag(ERROR_KIND, throwable.javaClass.simpleName)
                span.setTag(ERROR_TYPE, throwable.javaClass.simpleName)
                span.setTag(MESSAGE, throwable.message ?: "N/A")
                span.setTag(ERROR_OBJECT, throwable.toString())

                span.setTag(ERROR_STACK, throwable.stackTrace.joinToString())
                span.setTag(ERROR_EXPECTED, expected)
                span.setTag(ERROR, !expected)

                val log = mutableMapOf<String, Any?>()
                log.putAll(params)
                span.log(log)
            }
        }

        fun noticeError(message: String?, params: Map<String, Any> = mapOf(), expected: Boolean = false) {
            applyCurrentSpan { span ->
                span.setTag(MESSAGE, message)
                span.setTag(ERROR, !expected)
                span.setTag(ERROR_EXPECTED, expected)

                val log = mutableMapOf<String, Any?>()
                log.putAll(params)
                span.setTag(SFX_ERROR_KIND, message)

                span.log(log)
            }
        }

        fun addCustomParameter(key: String, value: String) {
            val log = mutableMapOf<String, Any>()
            log.put(key, value)
            applyCurrentSpan { span ->
                span.log(log)
            }
        }

        fun addCustomParameters(params: Map<String, Any?>) {
            applyCurrentSpan { span ->
                span.log(params)
            }
        }

        fun setTransactionName(name: String) {
            applyCurrentSpan { span ->
                span.setOperationName(name)
            }
        }

        @Deprecated("Use setTransactionName(name) instead for categories is part of forbidden custom fields")
        fun setTransactionName(category: String, name: String) {
            applyCurrentSpan { span ->
                span.setOperationName(name)
            }

        }

        fun startTrace(name: String, async: Boolean): () -> Unit {
            val tracer: Tracer = GlobalTracer.get() ?: return {}

            val span = tracer.buildSpan(name).start()
            if (async) {
                val traceScope: TraceScope = span as TraceScope
                traceScope.setAsyncPropagation(true)
            }

            return {
                span.finish()
            }
        }

        fun applyCurrentSpan(fn: (Span) -> Unit) {
            val tracer = GlobalTracer.get() ?: return
            val span= tracer.scopeManager().activeSpan()
            if (span != null) {
                fn.invoke(span)
                span.finish()
            }
        }
    }
}