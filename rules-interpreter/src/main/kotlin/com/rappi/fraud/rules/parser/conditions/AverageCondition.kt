package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.evaluators.FieldEvaluator
import java.math.RoundingMode

class AverageCondition : Condition<ANAParser.AverageContext> {

    @Suppress("UNCHECKED_CAST")
    override fun eval(ctx: ANAParser.AverageContext, data: Map<String, *>): Boolean {
        val list = FieldEvaluator(data).visitField(ctx.field())
        return if (list is Collection<*>) {
            val count = list.count { ConditionEvaluator(it as Map<String, *>).visit(ctx.cond()) }.toBigDecimal()
            val average = count.divide(list.size.toBigDecimal(), 3, RoundingMode.DOWN)
            val value = ctx.NUMERIC_LITERAL().text.toBigDecimal()
            when {
                ctx.GT() != null -> average > value
                ctx.GT_EQ() != null -> average >= value
                ctx.LT() != null -> average < value
                ctx.LT_EQ() != null -> average <= value
                else -> throw RuntimeException("Invalid Operation ${ctx.text}")
            }
        } else {
            throw RuntimeException("${ctx.field().text} is not a Collection")
        }
    }
}