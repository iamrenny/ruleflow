package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.evaluators.FieldEvaluator

class CountCondition : Condition<ANAParser.CountContext> {

    @Suppress("UNCHECKED_CAST")
    override fun eval(ctx: ANAParser.CountContext, data: Map<String, *>): Boolean {
        val list = FieldEvaluator(data).visitField(ctx.field())
        return if (list is Collection<*>) {
            val count = list.count { ConditionEvaluator(it as Map<String, *>).visit(ctx.cond()) }.toLong()
            val value = ctx.NUMERIC_LITERAL().text.toLong()
            when {
                ctx.GT() != null -> count > value
                ctx.GT_EQ() != null -> count >= value
                ctx.LT() != null -> count < value
                ctx.LT_EQ() != null -> count <= value
                else -> throw RuntimeException("Invalid Operation ${ctx.text}")
            }
        } else {
            throw RuntimeException("${ctx.field().text} is not a Collection")
        }
    }
}