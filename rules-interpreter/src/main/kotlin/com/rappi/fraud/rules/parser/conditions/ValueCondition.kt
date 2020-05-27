package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.ValueContext
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.removeSingleQuote
import com.rappi.fraud.rules.parser.vo.Value
import java.time.LocalDateTime

class ValueCondition : Condition<ValueContext> {

    override fun eval(ctx: ValueContext, evaluator: ConditionEvaluator): Any? {
        return when {
            ctx.validValue().string != null -> Value.notProperty(ctx.validValue().string.text.removeSingleQuote())
            ctx.validValue().number != null -> Value.notProperty(ctx.validValue().number.text.toBigDecimal())
            ctx.validValue().nullValue != null -> Value.notProperty(null)
            ctx.validValue().currentDate != null -> Value.notProperty(LocalDateTime.now())
            else -> throw IllegalArgumentException("Value not supported: ${ctx.text}")
        }
    }
}