package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.conditions.*
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree

class Visitor(val data: Map<String, *>,val lists:  Map<String, List<String>> = mapOf(), val root: Map<String, *>) : ANABaseVisitor<Any>() {

    override fun visit(tree: ParseTree?): Any? {
        val ctx = tree as ParserRuleContext

        val condition = when (ctx) {
            is ANAParser.BinaryContext -> BinaryCondition()
            is ANAParser.ComparatorContext -> ComparatorCondition()
            is ANAParser.AggregationContext -> AggregationCondition()
            is ANAParser.MathContext -> MathCondition()
            is ANAParser.ParenthesisContext -> ParenthesisCondition()
            is ANAParser.ValueContext -> ValueCondition()
            is ANAParser.PropertyContext -> PropertyCondition()
            is ANAParser.ValidPropertyContext -> ValidPropertyCondition()
            is ANAParser.DateDiffContext -> DateDiffCondition()
            is ANAParser.ListContext -> ListCondition()
            is ANAParser.UnaryContext -> UnaryCondition()
            else -> throw IllegalArgumentException("Context not supported: ${ctx.javaClass}")
        } as Condition<ParserRuleContext>

        return condition.eval(ctx, this)
    }
}