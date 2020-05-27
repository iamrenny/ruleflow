package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.DateDiffContext
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.vo.Value
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

class DateDiffCondition : Condition<DateDiffContext> {

    override fun eval(ctx: DateDiffContext, evaluator: ConditionEvaluator): Value {
        val valLeft = evaluator.visit(ctx.left).asValue()
        val valRight = evaluator.visit(ctx.right).asValue()
        val interval = ctx.interval

        if(valLeft.isNullProperty() || valRight.isNullProperty())
            return Value.property(null)

        val left = valLeft.toLocalDateTime()
        val right = valRight.toLocalDateTime()

        return Value.property(
                when (interval.start.type) {
                    ANAParser.HOUR -> Duration.between(left, right).toHours()
                    ANAParser.DAY -> Duration.between(left, right.truncatedTo(ChronoUnit.DAYS)).toDays()
                    else -> throw RuntimeException("Interval not supported: ${interval.start}")
                }.absoluteValue
        )
    }

}