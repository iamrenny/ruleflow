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
                    ANALexer.K_ALL -> value.all { evalPredicate(it, ctx.predicate) as Boolean }
                    ANALexer.K_ANY -> value.any { evalPredicate(it, ctx.predicate) as Boolean }
                    ANALexer.K_AVERAGE -> average(value, ctx.predicate)
                    ANALexer.K_COUNT -> count(value, ctx.predicate)
                    ANALexer.K_DISTINCT -> distinctBy(value, ctx.predicate)
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
        private fun distinctBy(list: List<*>, predicate: ANAParser.ExprContext?): Any? {
            return when (predicate) {
                    null -> list
                    is ANAParser.ValueContext -> list.distinctBy { (it as Map<String, Any>)[predicate.text] }
                    else -> list.distinctBy { evalPredicate(it, predicate) }
                }
        }

        @Suppress("UNCHECKED_CAST")
        private fun average(list: List<*>, predicate: ANAParser.ExprContext): Any? {
            val count = count(list, predicate).toString().toBigDecimal()
            return count.divide(list.size.toBigDecimal(), 3, RoundingMode.DOWN)
        }

        @Suppress("UNCHECKED_CAST")
        private fun count(list: List<*>, predicate: ANAParser.ExprContext?): Any? {
            return if (predicate == null) {
                    list.count().toBigDecimal()
                } else {
                    //TODO: REFACTOR THIS FOR BETTER READABILITY
                    list.count { Visitor(it as Map<String, *>).visit(predicate) as Boolean }.toBigDecimal()
                }
        }

        @Suppress("UNCHECKED_CAST")
        private fun evalPredicate(it: Any?, ctx: ANAParser.ExprContext) =
            Visitor(it as Map<String, *>).visit(ctx)
    }