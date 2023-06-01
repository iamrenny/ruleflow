package com.rappi.analang.evaluators

import com.rappi.fraud.analang.ANAParser.ComparatorContext
import com.rappi.fraud.analang.ANAParser.EQ
import com.rappi.fraud.analang.ANAParser.EQ_IC
import com.rappi.fraud.analang.ANAParser.GT
import com.rappi.fraud.analang.ANAParser.GT_EQ
import com.rappi.fraud.analang.ANAParser.LT
import com.rappi.fraud.analang.ANAParser.LT_EQ
import com.rappi.fraud.analang.ANAParser.NOT_EQ
import com.rappi.analang.visitors.Visitor
import org.antlr.v4.runtime.Token
import java.math.BigDecimal
import java.math.RoundingMode

class ComparatorContextEvaluator : ContextEvaluator<ComparatorContext> {

    override fun evaluate(ctx: ComparatorContext, visitor: Visitor): Boolean {
        val left = visitor.visit(ctx.left)
        val right = visitor.visit(ctx.right)
        return when {
            left is Number && right is Number -> compareNumbers(
                operator = ctx.op,
                left = left.toString().toBigDecimal().setScale(10, RoundingMode.DOWN),
                right = right.toString().toBigDecimal().setScale(10, RoundingMode.DOWN)
            )
            else -> compareAny(
                operator = ctx.op,
                left = left,
                right = right
            )
        }
    }


    private fun compareNumbers(operator: Token, left: BigDecimal, right: BigDecimal?): Boolean {
        return when (operator.type) {
            EQ, EQ_IC -> left == right
            NOT_EQ -> left != right
            LT -> left < right
            LT_EQ -> left <= right
            GT -> left > right
            GT_EQ -> left >= right

            else -> throw RuntimeException("Invalid condition ${operator.text}")
        }
    }

    private fun compareAny(operator: Token, left: Any?, right: Any?): Boolean {
        return when (operator.type) {
            EQ -> left == right
            EQ_IC -> (left?.toString() ?: "").compareTo(right?.toString() ?: "", true) == 0
            NOT_EQ -> left != right
            else -> false
        }
    }
}