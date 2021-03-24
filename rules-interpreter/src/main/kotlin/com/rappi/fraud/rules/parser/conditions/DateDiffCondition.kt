package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.DateDiffContext
import com.rappi.fraud.rules.parser.evaluators.Visitor
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

class DateDiffCondition : Condition<DateDiffContext> {

    override fun eval(ctx: DateDiffContext, visitor: Visitor): Any? {
        val valLeft = visitor.visit(ctx.left)
        val valRight = visitor.visit(ctx.right)
        if (valLeft == null || valRight == null)
            return null
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