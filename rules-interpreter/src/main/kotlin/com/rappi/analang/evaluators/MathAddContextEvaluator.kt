package com.github.iamrenny.rulesflow.evaluators

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser.MathAddContext
import com.github.iamrenny.rulesflow.visitors.Visitor
import java.math.RoundingMode

class MathAddContextEvaluator : ContextEvaluator<MathAddContext> {

    override fun evaluate(ctx: MathAddContext, visitor: Visitor): Any {
        val leftVal = visitor.visit(ctx.left)
        val rightVal = visitor.visit(ctx.right)

        val left = leftVal.toString().toBigDecimal().setScale(2, RoundingMode.DOWN)
        val right = rightVal.toString().toBigDecimal().setScale(2, RoundingMode.DOWN)
        return when (ctx.op.type) {
                ANALexer.ADD -> left.add(right)
                ANALexer.MINUS -> left.subtract(right)
                else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.text}")
            }
    }

}