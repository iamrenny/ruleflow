package com.rappi.fraud.rules.parser.visitors

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.AggregationContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.BinaryAndContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.BinaryOrContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.ComparatorContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.ContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.DateDiffContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.ListContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.MathAddContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.MathMulContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.ParenthesisContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.PropertyContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.UnaryContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.ValidPropertyContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.ValueContextEvaluator
import com.rappi.fraud.rules.parser.evaluators.DayOfWeekContextEvaluator
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree


class Visitor(
    val data: Map<String, *>,
    val lists:  Map<String, Set<String>> = mapOf(),
    val root: Map<String, *>
) : ANABaseVisitor<Any>() {
    override fun visit(tree: ParseTree?): Any {
        val ctx = tree as ParserRuleContext

        val condition = when (ctx) {
            is ANAParser.ComparatorContext -> ComparatorContextEvaluator()
            is ANAParser.AggregationContext -> AggregationContextEvaluator()
            is ANAParser.MathMulContext -> MathMulContextEvaluator()
            is ANAParser.MathAddContext -> MathAddContextEvaluator()
            is ANAParser.ParenthesisContext -> ParenthesisContextEvaluator()
            is ANAParser.ValueContext -> ValueContextEvaluator()
            is ANAParser.PropertyContext -> PropertyContextEvaluator()
            is ANAParser.ValidPropertyContext -> ValidPropertyContextEvaluator()
            is ANAParser.DateDiffContext -> DateDiffContextEvaluator()
            is ANAParser.ListContext -> ListContextEvaluator()
            is ANAParser.UnaryContext -> UnaryContextEvaluator()
            is ANAParser.BinaryAndContext -> BinaryAndContextEvaluator()
            is ANAParser.BinaryOrContext -> BinaryOrContextEvaluator()
            is ANAParser.DayOfWeekContext -> DayOfWeekContextEvaluator()
            else -> throw IllegalArgumentException("Operation not supported: ${ctx.javaClass}")
        } as ContextEvaluator<ParserRuleContext>

        return condition.evaluate(ctx, this)
    }
}