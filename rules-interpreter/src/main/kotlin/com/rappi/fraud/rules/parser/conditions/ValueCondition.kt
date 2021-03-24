package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.ValueContext
import com.rappi.fraud.rules.parser.evaluators.Visitor
import com.rappi.fraud.rules.parser.removeSingleQuote
import java.time.LocalDateTime

class ValueCondition : Condition<ValueContext> {

    override fun eval(ctx: ValueContext, visitor: Visitor): Any? {
        return when {
            ctx.validValue().string != null -> ctx.validValue().string.text.removeSingleQuote()
            ctx.validValue().number != null -> ctx.validValue().number.text.toBigDecimal()
            ctx.validValue().nullValue != null -> null
            ctx.validValue().currentDate != null -> LocalDateTime.now()
            else -> throw IllegalArgumentException("Value not supported: ${ctx.text}")
        }
    }
}