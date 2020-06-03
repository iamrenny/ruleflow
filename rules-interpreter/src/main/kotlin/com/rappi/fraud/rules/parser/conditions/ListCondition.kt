package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.removeSingleQuote

class ListCondition : Condition<ANAParser.ListContext> {

    override fun eval(ctx: ANAParser.ListContext, evaluator: ConditionEvaluator): Any {
        val value = evaluator.visit(ctx.value).asValue()
        return when (ctx.op.type) {
                ANALexer.K_CONTAINS -> evalContains(value.toAny().toString(), ctx.values.text.removeSingleQuote().split(","))
                ANALexer.K_IN -> evalIn(value.toAny().toString(), ctx.values, evaluator)
                else -> throw RuntimeException("Unexpected token near ${ctx.value.text}")
        }
    }
    private fun evalIn(value: String, ctx: ANAParser.ListElemsContext, evaluator: ConditionEvaluator): Any {
        return when
        {
            (ctx.aList != null) -> {
                val list = ctx.aList.text.removeSingleQuote().split(",")
                list.contains(value)
            }
            (ctx.validProperty()?.property != null) -> (evaluator.data[ctx.validProperty().property.text] as List<*>).contains(value)
            (ctx.validProperty()?.nestedProperty != null) -> getNestedValue(ctx.validProperty(), evaluator.data).contains(value)

            else -> error("asda")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun evalContains(value: String, values: List<String>) =
        values.any {
            value.contains(it, true)
        }


    @Suppress("UNCHECKED_CAST")
    fun getNestedValue(ctx: ANAParser.ValidPropertyContext, data: Map<String, *>): List<Any?> {
        val queue = mutableListOf(ctx)
        ctx.validProperty().forEach {
            queue.add(it)
        }
        var r = (data as MutableMap)
        queue.take(ctx.validProperty().size).forEach {
            if (r[it.ID().text] is Map<*, *>) {
                r = r[it.ID().text] as MutableMap<String, *>
            } else {
                return listOf()
            }
        }
        return  r[queue.last().ID().text] as List<Any?>
    }
}