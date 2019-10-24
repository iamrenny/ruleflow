package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser.NumberContext
import com.rappi.fraud.rules.parser.evaluators.FieldEvaluator

class NumericCondition : Condition<NumberContext> {
    override fun eval(ctx: NumberContext, data: Map<String, *>): Boolean {
        val ruleCtx = FieldEvaluator(data).visitFieldRecursive(ctx.field())
                ?: throw RuntimeException("Invalid Operation ${ctx.field().text}")
        val ctxValue = ruleCtx.toString().toLong()
        val literalValue = ctx.value.text.toLong()
        return when (ctx.op.type) {
            ANALexer.LT -> ctxValue < literalValue
            ANALexer.LT_EQ -> ctxValue <= literalValue
            ANALexer.GT -> ctxValue > literalValue
            ANALexer.GT_EQ -> ctxValue >= literalValue
            else -> throw RuntimeException("Invalid condition ${ctx.text}")
        }
    }
}