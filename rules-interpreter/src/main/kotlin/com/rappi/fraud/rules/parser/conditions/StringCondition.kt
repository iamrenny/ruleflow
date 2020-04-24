package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.ListContext
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.removeSingleQuote
import com.rappi.fraud.rules.parser.vo.Value
import java.math.RoundingMode

class StringCondition : Condition<ANAParser.StringContext> {

    override fun eval(ctx: ANAParser.StringContext, evaluator: ConditionEvaluator): Any {
        val value = evaluator.visit(ctx.value).asValue()
        return when (ctx.op.type) {
                ANALexer.K_CONTAINS -> evalContains(value.toAny().toString(), ctx.values.text.removeSingleQuote().split(","))
                else -> throw RuntimeException("Unexpected token near ${ctx.value.text}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun evalContains(value: String, values: List<String>) =
        values.any {
            value.contains(it, true)
        }

}