package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.MathAddContext
import com.rappi.fraud.rules.parser.evaluators.Visitor
import java.math.RoundingMode

class MathMulCondition : Condition<ANAParser.MathMulContext> {

    override fun eval(ctx: ANAParser.MathMulContext, visitor: Visitor): Any {
        val leftVal = visitor.visit(ctx.left)
        val rightVal = visitor.visit(ctx.right)

        val left = leftVal.toString().toBigDecimal().setScale(2, RoundingMode.DOWN)
        val right = rightVal.toString().toBigDecimal().setScale(2, RoundingMode.DOWN)
        return when (ctx.op.type) {
                ANALexer.MULTIPLY -> left.multiply(right)
                ANALexer.DIVIDE -> left.divide(right, 2, RoundingMode.DOWN)
                else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.text}")
            }
    }

}