package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANAParser.DateDiffContext
import com.rappi.fraud.rules.parser.visitors.Visitor
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

class DateDiffContextEvaluator : ContextEvaluator<DateDiffContext> {

    override fun evaluate(ctx: DateDiffContext, visitor: Visitor): Any {
        val valLeft = visitor.visit(ctx.left)
        val valRight = visitor.visit(ctx.right)
        val left =  LocalDateTime.parse(valLeft.toString())
        val right = LocalDateTime.parse(valRight.toString())

        return when {
                    ctx.MINUTE() != null ->  Duration.between(left, right).toMinutes()
                    ctx.HOUR() != null -> Duration.between(left, right).toHours()
                    ctx.DAY() != null -> Duration.between(left, right.truncatedTo(ChronoUnit.DAYS)).toDays()
                    else -> throw RuntimeException("Interval not supoorted in ${ctx.text}")
                }.absoluteValue

    }

}