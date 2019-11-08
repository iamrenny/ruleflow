package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.ListContext
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator

class ListCondition : Condition<ListContext> {

    override fun eval(ctx: ListContext, evaluator: ConditionEvaluator): Boolean {
        val value = evaluator.visit(ctx.value).asValue()
        return if (value.isList()) {
            when (ctx.op.type) {
                ANALexer.K_ALL -> value.toList().all { predicate(it, ctx.predicate) }
                ANALexer.K_ANY -> value.toList().any { predicate(it, ctx.predicate) }
                else -> throw RuntimeException("Unexpected token near ${ctx.value.text}")
            }
        } else {
            throw RuntimeException("${ctx.value.text} is not a Collection")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun predicate(it: Any?, ctx: ANAParser.CondContext) =
        ConditionEvaluator(it as Map<String, *>).visit(ctx) as Boolean
}