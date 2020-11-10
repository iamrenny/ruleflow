package com.rappi.fraud.rules.parser.conditions
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.AggregationContext
import com.rappi.fraud.rules.parser.evaluators.Visitor
import java.math.RoundingMode

class AggregationCondition : Condition<AggregationContext> {
    override fun eval(ctx: AggregationContext, evaluator: Visitor): Any? {
        val value = evaluator.visit(ctx.value)
        return when {
            value is List<*> -> {
                when (ctx.op.type) {
                    ANALexer.K_ALL -> value.all { data -> evalPredicate(data, evaluator.lists, ctx.predicate) as Boolean }
                    ANALexer.K_ANY -> value.any { data -> evalPredicate(data, evaluator.lists, ctx.predicate) as Boolean }
                    ANALexer.K_NONE -> value.none { data -> evalPredicate(data, evaluator.lists, ctx.predicate) as Boolean }
                    ANALexer.K_AVERAGE -> average(value, ctx.predicate, evaluator.lists)
                    ANALexer.K_COUNT -> count(value, ctx.predicate, evaluator.lists)
                    ANALexer.K_DISTINCT -> distinctBy(value, ctx.predicate, evaluator.lists)
                    else -> throw RuntimeException("Operation not supported: ${ctx.op.text}")
                }
            }
            value == null -> null
            else ->  {
                throw RuntimeException("${ctx.value.text} is not a Collection")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun distinctBy(
        list: List<*>,
        predicate: ANAParser.ExprContext?,
        lists: Map<String, List<String>>
    ): Any? {
        return when (predicate) {
            null -> list
            is ANAParser.ValueContext -> list.distinctBy { (it as Map<String, Any>)[predicate.text] }
            else -> list.distinctBy { evalPredicate(it, lists, predicate) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun average(
        list: List<*>,
        predicate: ANAParser.ExprContext,
        lists: Map<String, List<String>>
    ): Any? {
        val count = count(list, predicate, lists).toString().toBigDecimal()
        return count.divide(list.size.toBigDecimal(), 3, RoundingMode.DOWN)
    }

    @Suppress("UNCHECKED_CAST")
    private fun count(
        list: List<*>,
        predicate: ANAParser.ExprContext?,
        lists: Map<String, List<String>>
    ): Any? {
        return if (predicate == null) {
            list.count().toBigDecimal()
        } else {
            //TODO: REFACTOR THIS FOR BETTER READABILITY
            list.count { data ->  Visitor(data as Map<String, *>, lists).visit(predicate) as Boolean }.toBigDecimal()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun evalPredicate(data: Any?, lists: Map<String, List<String>>, ctx: ANAParser.ExprContext) =
        Visitor(data as Map<String, *>, lists).visit(ctx)
}