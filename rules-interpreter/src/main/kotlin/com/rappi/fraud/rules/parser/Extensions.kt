package com.rappi.fraud.rules.parser

import com.rappi.fraud.rules.parser.vo.Value
import java.math.RoundingMode

fun String.removeSingleQuote() = this.replace("'", "")

fun Any?.asValue() = this as Value