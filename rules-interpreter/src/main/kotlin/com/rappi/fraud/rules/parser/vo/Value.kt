package com.rappi.fraud.rules.parser.vo

import java.math.RoundingMode
import java.time.LocalDateTime

//TODO THIS MUST BE DELETED ASAP
class Value private constructor(private val value: Any?, private val property: Boolean) {

    companion object {
        fun notProperty(value: Any?) = Value(value, false)
        fun property(value: Any?) = Value(value, true)
    }

    fun isNullProperty() = property && value == null

    fun isNotPropertyAndNotNull() = !property && value != null

    fun isNumber() = value is Number

    fun isList() = value is Collection<*>

    fun toList() = value as List<*>

    fun toBigDecimal() = value.toString().toBigDecimal()

    fun toRoundedBigDecimal() = toBigDecimal().setScale(2, RoundingMode.DOWN)!!

    fun toLocalDateTime() = if (value is String) LocalDateTime.parse(value.toString()) else value as LocalDateTime!!

    fun toAny() = value
}