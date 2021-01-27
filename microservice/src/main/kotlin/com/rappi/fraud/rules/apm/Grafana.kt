package com.rappi.fraud.rules.apm

import io.vertx.micrometer.backends.BackendRegistries

class Grafana {
    companion object {
        private const val errorMetricName = "com.rappi.errors"

        fun noticeError(throwable: Throwable, expected: Boolean = false) {
            BackendRegistries.getDefaultNow().counter(
                    errorMetricName,
                    "error_class", throwable.javaClass.simpleName,
                    "error_message", throwable.message ?: throwable.javaClass.simpleName,
                    "stacktrace", (throwable.stackTrace.firstOrNull { it.className.startsWith("com.rappi.fraud.") }?.toString() ?: "NA"),
            "message", "NA",
                    "expected", expected.toString()

            ).increment()
        }

        fun noticeError(message: String, throwable: Throwable, expected: Boolean = false) {
            BackendRegistries.getDefaultNow().counter(
                    errorMetricName,
                    "error_message", throwable.message ?: throwable.javaClass.simpleName,
                    "error_class", throwable.javaClass.simpleName,
                    "stacktrace", (throwable.stackTrace.firstOrNull { it.className.startsWith("com.rappi.fraud.") }?.toString() ?: "NA"),
                    "message", message,
                    "expected", expected.toString()
            ).increment()
        }

        fun noticeError(message: String?, expected: Boolean = false) {
            BackendRegistries.getDefaultNow().counter(
                    errorMetricName,
                    "error_class", "NA",
                    "error_message", message ?: "NA",
                    "stacktrace", "NA",
                    "message", "NA",
                    "expected", expected.toString()
            ).increment()
        }
    }
}
