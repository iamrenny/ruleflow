package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.NumberContext
import com.rappi.fraud.rules.parser.evaluators.FieldEvaluator
import javafx.util.converter.BigIntegerStringConverter

class NumericCondition : Condition<NumberContext> {
    override fun eval(ctx: NumberContext, data: Map<String, *>): Boolean {
        val ruleCtx = FieldEvaluator(data).visitFieldRecursive(ctx.field())
                ?: throw RuntimeException("Invalid Operation ${ctx.field().text}")
        val ctxValue = BigIntegerStringConverter().fromString(ruleCtx.toString()).toLong()
        val literalValue = ctx.NUMERIC_LITERAL().text.toLong()
        return when {
            ctx.LT() != null -> ctxValue < literalValue
            ctx.LT_EQ() != null -> ctxValue <= literalValue
            ctx.GT() != null -> ctxValue > literalValue
            ctx.GT_EQ() != null -> ctxValue >= literalValue
            else -> throw RuntimeException("Invalid condition ${ctx.text}")
        }
    }
}