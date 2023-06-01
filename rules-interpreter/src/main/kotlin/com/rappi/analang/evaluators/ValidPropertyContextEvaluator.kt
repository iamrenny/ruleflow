package com.rappi.analang.evaluators

import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.errors.PropertyNotFoundException
import com.rappi.analang.visitors.Visitor

class ValidPropertyContextEvaluator: ContextEvaluator<ANAParser.ValidPropertyContext> {
    override fun evaluate(ctx: ANAParser.ValidPropertyContext, visitor: Visitor): Any {
        return when {
            ctx.property != null -> visitor.data[ctx.ID(0).text]
                ?: throw PropertyNotFoundException("${ctx.ID(0).text} field cannot be found")
            ctx.nestedProperty != null -> {
                val data = if(ctx.root != null) {
                    visitor.root
                } else {
                    visitor.data
                }
                getNestedValue(
                    ctx,
                    data
                )
            }

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