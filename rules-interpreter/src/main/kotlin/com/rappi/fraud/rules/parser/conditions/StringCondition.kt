package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.StringContext
import com.rappi.fraud.rules.parser.evaluators.FieldEvaluator

class StringCondition : Condition<StringContext> {

    override fun eval(ctx: StringContext, data: Map<String, *>): Boolean {
        val ruleCtx = FieldEvaluator(data).visitFieldRecursive(ctx.field())
                ?: throw RuntimeException("Invalid Operation ${ctx.field().text}")
        val value = ctx.STRING_LITERAL().text.replace("'", "")
        return when {
            ctx.EQ() != null -> ruleCtx == value
            ctx.NOT_EQ1() != null || ctx.NOT_EQ2() != null -> ruleCtx != value
            else -> throw RuntimeException("Invalid condition ${ctx.text}")
        }
    }
}