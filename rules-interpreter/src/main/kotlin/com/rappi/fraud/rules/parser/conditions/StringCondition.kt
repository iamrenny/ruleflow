package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser.StringContext
import com.rappi.fraud.rules.parser.evaluators.FieldEvaluator

class StringCondition : Condition<StringContext> {

    override fun eval(ctx: StringContext, data: Map<String, *>): Boolean {
        val ruleCtx = FieldEvaluator(data).visitFieldRecursive(ctx.field())
                ?: throw RuntimeException("Invalid Operation ${ctx.field().text}")
        val value = ctx.value.text.removeSingleQuote()
        return when (ctx.op.type) {
            ANALexer.EQ -> ruleCtx == value
            ANALexer.NOT_EQ1, ANALexer.NOT_EQ2 -> ruleCtx != value
            else -> throw RuntimeException("Invalid condition ${ctx.text}")
        }
    }
}

fun String.removeSingleQuote() = this.replace("'", "")