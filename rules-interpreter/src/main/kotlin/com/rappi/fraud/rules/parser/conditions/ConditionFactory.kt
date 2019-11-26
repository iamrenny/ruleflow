package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser

class ConditionFactory {

    companion object {
        private val BINARY_CONDITION = BinaryCondition()
        private val COMPARATOR_CONDITION = ComparatorCondition()
        private val LIST_CONDITION = ListCondition()
        private val MATH_CONDITION = MathCondition()
        private val PARENTHESIS_CONDITION = ParenthesisCondition()
        private val VALUE_CONDITION = ValueCondition()
        private val DATE_DIFF_CONDITION = DateDiffCondition()

        @Suppress("UNCHECKED_CAST")
        fun <T : ANAParser.CondContext> get(ctx: T): Condition<T> {
            return when (ctx) {
                is ANAParser.BinaryContext -> BINARY_CONDITION
                is ANAParser.ComparatorContext -> COMPARATOR_CONDITION
                is ANAParser.ListContext -> LIST_CONDITION
                is ANAParser.MathContext -> MATH_CONDITION
                is ANAParser.ParenthesisContext -> PARENTHESIS_CONDITION
                is ANAParser.ValueContext -> VALUE_CONDITION
                is ANAParser.DateDiffContext -> DATE_DIFF_CONDITION
                else -> throw IllegalArgumentException("Context not supported: ${ctx.javaClass}")
            } as Condition<T>
        }
    }
}