package com.rappi.fraud.rules.parser.vo

import java.math.RoundingMode

class Value private constructor(private val value: Any?, private val property: Boolean) {

    companion object {

        fun property(value: Any?) = Value(value, false)

        fun notProperty(value: Any?) = Value(value, true)

    }

    fun isPropertyAndNull() = property && value == null

    fun isNotPropertyAndNotNull() = !property && value != null

    fun isNumber() = value is Number

    fun isList() = value is Collection<*>

    fun toList() = value as List<*>

    fun toRoundedBigDecimal() = value.toString().toBigDecimal().setScale(2, RoundingMode.DOWN)!!

    fun toAny() = value
}