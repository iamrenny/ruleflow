package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.ValueContext
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.removeSingleQuote
import com.rappi.fraud.rules.parser.vo.Value

class ValueCondition : Condition<ValueContext> {

    override fun eval(ctx: ValueContext, evaluator: ConditionEvaluator): Any? {
        return when {
            ctx.validValue().property != null -> Value.notProperty(evaluator.data[ctx.validValue().property.text])
            ctx.validValue().nestedProperty != null -> Value.notProperty(
                getNestedValue(
                    ctx.validValue(),
                    evaluator.data
                )
            )
            ctx.validValue().string != null -> Value.property(ctx.validValue().string.text.removeSingleQuote())
            ctx.validValue().number != null -> Value.property(ctx.validValue().number.text.toBigDecimal())
            ctx.validValue().nullable != null -> Value.property(null)
            else -> throw IllegalArgumentException("Value not supported: ${ctx.text}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getNestedValue(ctx: ANAParser.ValidValueContext, data: Map<String, *>): Any? {
        val queue = mutableListOf(ctx)
        ctx.validValue().forEach {
            queue.add(it)
        }
        var r = (data as MutableMap)
        queue.take(ctx.validValue().size).forEach {
            if (r[it.ID().text] is Map<*, *>) {
                r = r[it.ID().text] as MutableMap<String, *>
            } else {
                throw RuntimeException("${it.parent.text} is an invalid operation")
            }
        }
        return r[queue.last().ID().text]
    }
}