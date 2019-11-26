package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.analang.ANAParser.DateDiffContext
import com.rappi.fraud.rules.parser.asValue
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator
import com.rappi.fraud.rules.parser.vo.Value
import java.time.Duration
import kotlin.math.absoluteValue

class DateDiffCondition : Condition<DateDiffContext> {

    override fun eval(ctx: DateDiffContext, evaluator: ConditionEvaluator): Value {
        val interval = ctx.interval
        val left = evaluator.visit(ctx.left).asValue().toLocalDateTime()
        val right = evaluator.visit(ctx.right).asValue().toLocalDateTime()
        val duration = Duration.between(left, right)
        return Value.notProperty(
                when (interval.start.type) {
                    ANAParser.HOUR -> duration.toHours()
                    ANAParser.DAY -> duration.toDays()
                    else -> throw RuntimeException("Interval not supported: ${interval.start}")
                }.absoluteValue
        )
    }

}