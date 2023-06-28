package com.rappi.fraud.rules.parser.evaluators

import com.rappi.analang.evaluators.ContextEvaluator
import com.rappi.analang.visitors.Visitor
import com.rappi.fraud.analang.ANAParser.ComparatorContext
import com.rappi.fraud.analang.ANAParser.EQ
import com.rappi.fraud.analang.ANAParser.EQ_IC
import com.rappi.fraud.analang.ANAParser.GT
import com.rappi.fraud.analang.ANAParser.GT_EQ
import com.rappi.fraud.analang.ANAParser.K_NULL
import com.rappi.fraud.analang.ANAParser.LT
import com.rappi.fraud.analang.ANAParser.LT_EQ
import com.rappi.fraud.analang.ANAParser.NOT_EQ
import org.antlr.v4.runtime.Token
import java.math.RoundingMode

class ComparatorContextEvaluator : ContextEvaluator<ComparatorContext> {

    @SuppressWarnings("unchecked")
    override fun evaluate(ctx: ComparatorContext, visitor: Visitor): Boolean {
        val left = visitor.visit(ctx.left)
        val right = visitor.visit(ctx.right)
        return when {
            left is Number && right is Number -> compareComparable(
                operator = ctx.op,
                left = left.toString().toBigDecimal().setScale(10, RoundingMode.DOWN),
                right = right.toString().toBigDecimal().setScale(10, RoundingMode.DOWN)
            )
            left is String && right is String -> compareComparable(
                operator = ctx.op,
                left = left.toString() as String,
                right = right.toString() as String
            )
            ctx.left.start.type == K_NULL || ctx.right.start.type == K_NULL -> compareNull(
                operator = ctx.op,
                left = left,
                right = right
            )
            left is Comparable<*> && right is Comparable<*> -> compareComparable(
                operator = ctx.op,
                left = left as Comparable<Any>,
                right = right as Comparable<Any>
            )
            else -> false
        }
    }

    private fun <T : Comparable<T>> compareComparable(operator: Token, left: T, right: T): Boolean {
        return when (operator.type) {
            EQ -> left == right
            EQ_IC -> left.toString().compareTo(right.toString(), true) == 0
            NOT_EQ -> left.compareTo(right) != 0
            LT -> left < right
            LT_EQ -> left <= right
            GT -> left > right
            GT_EQ -> left >= right
            else -> throw RuntimeException("Invalid condition ${operator.text}")
        }
    }
    private fun compareNull(operator: Token, left: Any?, right: Any?): Boolean {
        return when (operator.type) {
            EQ -> left == right
            EQ_IC -> (left?.toString() ?: "").compareTo(right?.toString() ?: "", true) == 0
            NOT_EQ -> left != right
            else -> false
        }
    }
}