package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.errors.PropertyNotFoundException
import com.rappi.fraud.rules.parser.evaluators.Visitor

class ValidPropertyCondition: Condition<ANAParser.ValidPropertyContext> {
    override fun eval(ctx: ANAParser.ValidPropertyContext, evaluator: Visitor): Any? {
        return when {
            ctx.property != null -> evaluator.data[ctx.property.text]
                ?: throw PropertyNotFoundException("${ctx.property.text} field cannot be found")
            ctx.nestedProperty != null ->
                getNestedValue(
                    ctx,
                    evaluator.data
                )
            else -> throw PropertyNotFoundException("${ctx.text} field cannot be found")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getNestedValue(ctx: ANAParser.ValidPropertyContext, data: Map<String, *>): Any {
        var r = data
        ctx.ID().forEach { id ->
            when {
                r[id.text] is Map<*, *> -> r = r[id.text] as Map<String, Any?>
                else -> return r[id.text] ?: throw PropertyNotFoundException("${id.text} field cannot be found")
            }
        }
        throw PropertyNotFoundException("${ctx.text} cannot be found")
    }
}