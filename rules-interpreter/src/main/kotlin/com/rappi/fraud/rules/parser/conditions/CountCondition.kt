package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.evaluators.FieldEvaluator
import org.antlr.v4.runtime.Token

class CountCondition : Condition<ANAParser.CountContext> {

    @Suppress("UNCHECKED_CAST")
    override fun eval(ctx: ANAParser.CountContext, data: Map<String, *>): Boolean {
        val list = FieldEvaluator(data).visitField(ctx.field())
        return if (list is Collection<*>) {
            val count = list.count { ConditionEvaluator(it as Map<String, *>).visit(ctx.cond()) }.toLong()
            val value = ctx.value.text.toLong()
            when (ctx.op.type) {
                ANALexer.GT -> count > value
                ANALexer.GT_EQ -> count >= value
                ANALexer.LT -> count < value
                ANALexer.LT_EQ -> count <= value
                else -> throw RuntimeException("Invalid Operation ${ctx.text}")
            }
        } else {
            throw RuntimeException("${ctx.field().text} is not a Collection")
        }
    }
}