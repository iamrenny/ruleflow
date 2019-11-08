package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.vo.Value

class CountCondition : Condition<ANAParser.CountContext> {

    @Suppress("UNCHECKED_CAST")
    override fun eval(ctx: ANAParser.CountContext, evaluator: ConditionEvaluator): Value {
        val value = evaluator.visit(ctx.value).asValue()
        return Value.notProperty(value
            .toList()
            .count { ConditionEvaluator(it as Map<String, *>).visit(ctx.predicate) as Boolean }
            .toBigDecimal())
    }
}