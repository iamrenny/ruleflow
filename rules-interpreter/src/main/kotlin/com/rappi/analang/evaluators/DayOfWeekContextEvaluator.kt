package com.rappi.analang.evaluators

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.visitors.Visitor
import java.time.LocalDateTime


class DayOfWeekContextEvaluator : ContextEvaluator<ANAParser.DayOfWeekContext> {

    override fun evaluate(ctx: ANAParser.DayOfWeekContext, visitor: Visitor): Any {
        val valLeft = visitor.visit(ctx.left)
        val left =  LocalDateTime.parse(valLeft.toString())

        if(left == null)
            throw IllegalArgumentException("parameter value not supported: $valLeft")

        return when (ctx.op.type) {
            ANALexer.DAY_OF_WEEK -> left.dayOfWeek
            else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.text}")
        }

    }

}