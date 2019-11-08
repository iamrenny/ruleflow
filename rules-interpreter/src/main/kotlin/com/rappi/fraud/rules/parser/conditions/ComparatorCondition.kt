package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.*
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.vo.Value
import org.antlr.v4.runtime.Token
import java.math.BigDecimal

class ComparatorCondition : Condition<ComparatorContext> {

    override fun eval(ctx: ComparatorContext, evaluator: ConditionEvaluator): Boolean {
        val left = evaluator.visit(ctx.left).asValue()
        val right = evaluator.visit(ctx.right).asValue()
        return when {
            isNotComparableToNull(left, right) -> return false
            left.isNumber() || right.isNumber() -> compareNumbers(
                operator = ctx.op.start,
                left = left.toRoundedBigDecimal(),
                right = right.toRoundedBigDecimal()
            )
            else -> compareAny(
                operator = ctx.op.start,
                left = left.toAny(),
                right = right.toAny()
            )
        }
    }

    private fun isNotComparableToNull(left: Value, right: Value) =
        ((left.isPropertyAndNull() && right.isNotPropertyAndNotNull())
                || (right.isPropertyAndNull() && left.isNotPropertyAndNotNull()))

    private fun compareNumbers(operator: Token, left: BigDecimal, right: BigDecimal): Boolean {
        return when (operator.type) {
            LT -> left < right
            LT_EQ -> left <= right
            GT -> left > right
            GT_EQ -> left >= right
            EQ, EQ_IC -> left == right
            NOT_EQ -> left != right
            else -> throw RuntimeException("Invalid condition ${operator.text}")
        }
    }

    private fun compareAny(operator: Token, left: Any?, right: Any?): Boolean {
        return when (operator.type) {
            EQ -> left == right
            EQ_IC -> (left?.toString() ?: "").compareTo(right?.toString() ?: "", true) == 0
            NOT_EQ -> left != right
            else -> throw IllegalArgumentException("Operation not supported: ${operator.text}")
        }
    }
}