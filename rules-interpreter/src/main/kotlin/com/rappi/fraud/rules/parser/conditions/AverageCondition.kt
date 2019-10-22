package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
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
            val value = ctx.value.text.toBigDecimal()
            when (ctx.op.type) {
                ANALexer.GT -> average > value
                ANALexer.GT_EQ -> average >= value
                ANALexer.LT -> average < value
                ANALexer.LT_EQ -> average <= value
                else -> throw RuntimeException("Invalid Operation ${ctx.text}")
            }
        } else {
            throw RuntimeException("${ctx.field().text} is not a Collection")
        }
    }
}