package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.conditions.*
import org.antlr.v4.runtime.tree.ParseTree

class ConditionEvaluator(val data: Map<String, *>) : ANABaseVisitor<Any>() {

    override fun visit(tree: ParseTree?): Any? {
        val ctx = tree as ANAParser.ExprContext

        val condition = when (ctx) {
            is ANAParser.BinaryContext -> BinaryCondition()
            is ANAParser.ComparatorContext -> ComparatorCondition()
            is ANAParser.AggregationContext -> AggregationCondition()
            is ANAParser.MathContext -> MathCondition()
            is ANAParser.ParenthesisContext -> ParenthesisCondition()
            is ANAParser.ValueContext -> ValueCondition()
            is ANAParser.PropertyContext -> PropertyCondition()
            is ANAParser.DateDiffContext -> DateDiffCondition()
            is ANAParser.ListContext -> ListCondition()
            else -> throw IllegalArgumentException("Context not supported: ${ctx.javaClass}")
        } as Condition< ANAParser.ExprContext>

        return condition.eval(ctx, this)
    }
}