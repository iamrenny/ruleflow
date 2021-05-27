package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANAParser.ValueContext
import com.rappi.fraud.rules.parser.visitors.Visitor
import com.rappi.fraud.rules.parser.removeSingleQuote
import java.time.LocalDateTime

class ValueContextEvaluator : ContextEvaluator<ValueContext> {

    override fun evaluate(ctx: ValueContext, visitor: Visitor): Any {
        return when {
            ctx.validValue().string != null -> ctx.validValue().string.text.removeSingleQuote()
            ctx.validValue().number != null -> ctx.validValue().number.text.toBigDecimal()
            ctx.validValue().nullValue != null -> ctx.validValue().nullValue
            ctx.validValue().currentDate != null -> LocalDateTime.now()
            else -> throw IllegalArgumentException("Value not supported: ${ctx.text}")
        }
    }
}