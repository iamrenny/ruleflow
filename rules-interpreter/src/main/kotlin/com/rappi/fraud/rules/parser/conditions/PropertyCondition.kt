package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.Visitor

class PropertyCondition: Condition<ANAParser.PropertyContext> {
    override fun eval(ctx: ANAParser.PropertyContext, evaluator: Visitor): Any? {
        return when {
            ctx.validProperty().property != null -> evaluator.data[ctx.validProperty().property.text]
            ctx.validProperty().nestedProperty != null ->
                getNestedValue(
                    ctx.validProperty(),
                    evaluator.data
                )
            else -> null
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getNestedValue(ctx: ANAParser.ValidPropertyContext, data: Map<String, *>): Any? {
        var r = data
        ctx.ID().forEach { id ->
            when {
                r[id.text] is Map<*, *> -> r = r[id.text] as Map<String, Any?>
                else -> return r[id.text]
            }
        }
        return null
    }
}
