package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.ListContext
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.evaluators.FieldEvaluator

class ListCondition : Condition<ListContext> {

    override fun eval(ctx: ListContext, data: Map<String, *>): Boolean {
        val list = FieldEvaluator(data).visitField(ctx.field())
        return if (list is Collection<*>) {
            when {
                ctx.K_ALL() != null -> list.all { condition(it, ctx) }
                ctx.K_ANY() != null -> list.any { condition(it, ctx) }
                else -> throw RuntimeException("Unexpected token near ${ctx.field().text}")
            }
        } else {
            throw RuntimeException("${ctx.field().text} is not a Collection")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun condition(it: Any?, ctx: ListContext) =
            ConditionEvaluator(it as Map<String, *>).visit(ctx.cond())
}