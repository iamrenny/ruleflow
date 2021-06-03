package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.visitors.Visitor
import java.math.BigDecimal
import java.math.RoundingMode

class MathMulContextEvaluator : ContextEvaluator<ANAParser.MathMulContext> {

    override fun evaluate(ctx: ANAParser.MathMulContext, visitor: Visitor): Any {
        val leftVal = visitor.visit(ctx.left)
        val rightVal = visitor.visit(ctx.right)

        val left = leftVal.toString().toBigDecimal().setScale(2, RoundingMode.DOWN)
        val right = rightVal.toString().toBigDecimal().setScale(2, RoundingMode.DOWN)

        return when {
            right.compareTo(BigDecimal.ZERO) == 0 -> return BigDecimal.ZERO
            ctx.op.type == ANALexer.MULTIPLY -> left.multiply(right)
            ctx.op.type ==    ANALexer.DIVIDE -> left.divide(right, 2, RoundingMode.DOWN)
            else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.text}")
            }
    }

}