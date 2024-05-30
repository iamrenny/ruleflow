package com.github.iamrenny.rulesflow.visitors

import com.github.iamrenny.rulesflow.evaluators.AggregationContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.BinaryAndContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.BinaryOrContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.ContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.DateDiffContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.DayOfWeekContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.ListContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.MathAddContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.MathMulContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.ParenthesisContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.PropertyContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.RegexContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.UnaryContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.ValidPropertyContextEvaluator
import com.github.iamrenny.rulesflow.evaluators.ValueContextEvaluator
import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.ComparatorContextEvaluator
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