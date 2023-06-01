package com.rappi.fraud.rules.parser.evaluators
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.AggregationContext
import com.rappi.fraud.rules.parser.visitors.Visitor
import java.math.RoundingMode

class AggregationContextEvaluator : ContextEvaluator<AggregationContext> {
    override fun evaluate(ctx: AggregationContext, visitor: Visitor): Any {
        val value = visitor.visit(ctx.value)
        return when {
            value is List<*> -> {
                when (ctx.op.type) {
                    ANALexer.K_ALL -> value.all { data -> evalPredicate(data, visitor.root, visitor.lists, ctx.predicate) as Boolean }
                    ANALexer.K_ANY -> value.any { data -> evalPredicate(data, visitor.root, visitor.lists, ctx.predicate) as Boolean }
                    ANALexer.K_NONE -> value.none { data -> evalPredicate(data, visitor.root, visitor.lists, ctx.predicate) as Boolean }
                    ANALexer.K_AVERAGE -> average(value, ctx.predicate, visitor.lists, visitor.root)
                    ANALexer.K_COUNT -> count(value, ctx.predicate, visitor.lists, visitor.root)
                    ANALexer.K_DISTINCT -> distinctBy(value, ctx.predicate, visitor.lists, visitor.root)
                    else -> throw RuntimeException("Operation not supported: ${ctx.op.text}")
                }
            }
            else ->  {
                throw RuntimeException("${ctx.value.text} is not a Collection")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun distinctBy(
        list: List<*>,
        predicate: ANAParser.ExprContext?,
        lists: Map<String, Set<String>>,
        root: Any?
    ): Any {
        return when (predicate) {
            null -> list
            is ANAParser.ValueContext -> list.distinctBy { (it as Map<String, Any>)[predicate.text] }
            else -> list.distinctBy { evalPredicate(it, root, lists, predicate) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun average(
        list: List<*>,
        predicate: ANAParser.ExprContext,
        lists: Map<String, Set<String>>,
        root: Any?
    ): Any {
        val count = count(list, predicate, lists, root).toString().toBigDecimal()
        return count.divide(list.size.toBigDecimal(), 3, RoundingMode.DOWN)
    }

    @Suppress("UNCHECKED_CAST")
    private fun count(
        list: List<*>,
        predicate: ANAParser.ExprContext?,
        lists: Map<String, Set<String>>,
        root: Any?
    ): Any {
        return if (predicate == null) {
            list.count().toBigDecimal()
        } else {
            //TODO: REFACTOR THIS FOR BETTER READABILITY
            list.count { data ->
                Visitor(data as Map<String, Any?> , lists, root as Map<String, Any?>)
                    .visit(predicate) as Boolean
            }
                .toBigDecimal()
        }
    }

    @Suppress("UNCHECKED_CAST")
    //TODO: Both data and root might be Map<String, Any>
    private fun evalPredicate(data: Any?, root: Any?, lists: Map<String, Set<String>>, ctx: ANAParser.ExprContext) =
        Visitor(data as Map<String, Any>, lists, root as Map<String, Any>).visit(ctx)
}