package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.ListContext
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.vo.Value
import java.math.RoundingMode

class ListCondition : Condition<ListContext> {

    override fun eval(ctx: ListContext, evaluator: ConditionEvaluator): Any? {
        val value = evaluator.visit(ctx.value).asValue()
        return when {
            value.isList() -> {
                when (ctx.op.type) {
                    ANALexer.K_ALL -> value.toList().all { evalPredicate(it, ctx.predicate) as Boolean }
                    ANALexer.K_ANY -> value.toList().any { evalPredicate(it, ctx.predicate) as Boolean }
                    ANALexer.K_AVERAGE -> average(value.toList(), ctx.predicate)
                    ANALexer.K_COUNT -> count(value.toList(), ctx.predicate)
                    ANALexer.K_DISTINCT -> distinctBy(value.toList(), ctx.predicate)
                    else -> throw RuntimeException("Unexpected token near ${ctx.value.text}")
                }
            }
            value.isNullProperty() -> Value.property(null)
            else ->  {
                throw RuntimeException("${ctx.value.text} is not a Collection")
            }
        }
    }

        @Suppress("UNCHECKED_CAST")
        private fun distinctBy(list: List<*>, predicate: ANAParser.ExprContext?): Value {
            return Value.property(
                when (predicate) {
                    null -> list
                    is ANAParser.ValueContext -> list.distinctBy { (it as Map<String, Any>)[predicate.text] }
                    else -> list.distinctBy { evalPredicate(it, predicate) }
                })
        }

        @Suppress("UNCHECKED_CAST")
        private fun average(list: List<*>, predicate: ANAParser.ExprContext): Value {
            val count = count(list, predicate).toBigDecimal()
            return Value.property(count.divide(list.size.toBigDecimal(), 3, RoundingMode.DOWN))
        }

        @Suppress("UNCHECKED_CAST")
        private fun count(list: List<*>, predicate: ANAParser.ExprContext?): Value {
            return Value.property(
                if (predicate == null) {
                    list.count()
                } else {
                    list.count { ConditionEvaluator(it as Map<String, *>).visit(predicate) as Boolean }
                }.toBigDecimal())
        }

        @Suppress("UNCHECKED_CAST")
        private fun evalPredicate(it: Any?, ctx: ANAParser.ExprContext) =
            ConditionEvaluator(it as Map<String, *>).visit(ctx)
    }