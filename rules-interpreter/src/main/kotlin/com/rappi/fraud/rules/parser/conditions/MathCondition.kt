package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser.MathContext
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.vo.Value
import java.math.RoundingMode

class MathCondition : Condition<MathContext> {

    override fun eval(ctx: MathContext, evaluator: ConditionEvaluator): Value {
        val leftVal = evaluator.visit(ctx.left).asValue()
        val rightVal = evaluator.visit(ctx.right).asValue()
        if(leftVal.isNullProperty() || rightVal.isNullProperty())
            return Value.property(null)

        val left = leftVal.toRoundedBigDecimal()
        val right = rightVal.toRoundedBigDecimal()
        return Value.property(
            when (ctx.op.type) {
                ANALexer.ADD -> left.add(right)
                ANALexer.SUBTRACT -> left.subtract(right)
                ANALexer.MULTIPLY -> left.multiply(right)
                ANALexer.DIVIDE -> left.divide(right, 2, RoundingMode.DOWN)
                else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.text}")
            }
        )
    }

}