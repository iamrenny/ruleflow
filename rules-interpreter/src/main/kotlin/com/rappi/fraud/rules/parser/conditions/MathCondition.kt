package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser.MathContext
import com.rappi.fraud.rules.parser.evaluators.Visitor
import java.math.RoundingMode

class MathCondition : Condition<MathContext> {

    override fun eval(ctx: MathContext, evaluator: Visitor): Any? {
        val leftVal = evaluator.visit(ctx.left)
        val rightVal = evaluator.visit(ctx.right)

        val left = leftVal?.toString()?.toBigDecimal()?.setScale(2, RoundingMode.DOWN)
        val right = rightVal?.toString()?.toBigDecimal()?.setScale(2, RoundingMode.DOWN)
        return when (ctx.op.type) {
                ANALexer.ADD -> left?.add(right)
                ANALexer.MINUS -> left?.subtract(right)
                ANALexer.MULTIPLY -> left?.multiply(right)
                ANALexer.DIVIDE -> left?.divide(right, 2, RoundingMode.DOWN)
                else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.text}")
            }
    }

}