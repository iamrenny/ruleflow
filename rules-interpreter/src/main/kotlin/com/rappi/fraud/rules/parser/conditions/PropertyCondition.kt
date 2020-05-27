package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.vo.Value

class PropertyCondition: Condition<ANAParser.PropertyContext> {
    override fun eval(ctx: ANAParser.PropertyContext, evaluator: ConditionEvaluator): Any? {
        return when {
            ctx.validProperty().property != null -> Value.property(evaluator.data[ctx.validProperty().property.text])
            ctx.validProperty().nestedProperty != null -> Value.property(
                getNestedValue(
                    ctx.validProperty(),
                    evaluator.data
                )
            )
            else -> Value.property(null)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getNestedValue(ctx: ANAParser.ValidPropertyContext, data: Map<String, *>): Any? {
        val queue = mutableListOf(ctx)
        ctx.validProperty().forEach {
            queue.add(it)
        }
        var r = (data as MutableMap)
        queue.take(ctx.validProperty().size).forEach {
            if (r[it.ID().text] is Map<*, *>) {
                r = r[it.ID().text] as MutableMap<String, *>
            } else {
                return null
            }
        }
        return r[queue.last().ID().text]
    }
}
