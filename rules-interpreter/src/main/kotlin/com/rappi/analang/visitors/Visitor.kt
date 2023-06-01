package com.rappi.analang.visitors

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.evaluators.AggregationContextEvaluator
import com.rappi.analang.evaluators.BinaryAndContextEvaluator
import com.rappi.analang.evaluators.BinaryOrContextEvaluator
import com.rappi.analang.evaluators.ComparatorContextEvaluator
import com.rappi.analang.evaluators.ContextEvaluator
import com.rappi.analang.evaluators.DateDiffContextEvaluator
import com.rappi.analang.evaluators.ListContextEvaluator
import com.rappi.analang.evaluators.MathAddContextEvaluator
import com.rappi.analang.evaluators.MathMulContextEvaluator
import com.rappi.analang.evaluators.ParenthesisContextEvaluator
import com.rappi.analang.evaluators.PropertyContextEvaluator
import com.rappi.analang.evaluators.UnaryContextEvaluator
import com.rappi.analang.evaluators.ValidPropertyContextEvaluator
import com.rappi.analang.evaluators.ValueContextEvaluator
import com.rappi.analang.evaluators.DayOfWeekContextEvaluator
import com.rappi.analang.evaluators.RegexContextEvaluator
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
            is ANAParser.RegexlikeContext -> RegexContextEvaluator()
            else -> throw IllegalArgumentException("Operation not supported: ${ctx.javaClass}")
        } as ContextEvaluator<ParserRuleContext>

        return condition.evaluate(ctx, this)
    }
}