package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.visitors.Visitor

class PropertyContextEvaluator: ContextEvaluator<ANAParser.PropertyContext> {
    override fun evaluate(ctx: ANAParser.PropertyContext, visitor: Visitor): Any {
        val validPropertyCondition = ValidPropertyContextEvaluator()
        return validPropertyCondition.evaluate(ctx.validProperty(), visitor)
    }
}
