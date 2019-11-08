package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.vo.Value
import java.math.RoundingMode

class AverageCondition : Condition<ANAParser.AverageContext> {

    @Suppress("UNCHECKED_CAST")
    override fun eval(ctx: ANAParser.AverageContext, evaluator: ConditionEvaluator): Value {
        val value = evaluator.visit(ctx.value).asValue()
        return if (value.isList()) {
            val list = value.toList()
            val count = list
                .count { ConditionEvaluator(it as Map<String, *>).visit(ctx.predicate) as Boolean }
                .toBigDecimal()
            Value.notProperty(count.divide(list.size.toBigDecimal(), 3, RoundingMode.DOWN))
        } else {
            throw RuntimeException("${ctx.value.text} is not a Collection")
        }
    }
}