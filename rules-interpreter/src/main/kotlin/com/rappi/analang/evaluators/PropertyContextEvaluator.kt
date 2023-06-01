package com.rappi.analang.evaluators

import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.visitors.Visitor

class PropertyContextEvaluator: ContextEvaluator<ANAParser.PropertyContext> {
    override fun evaluate(ctx: ANAParser.PropertyContext, visitor: Visitor): Any {
        val validPropertyCondition = ValidPropertyContextEvaluator()
        return validPropertyCondition.evaluate(ctx.validProperty(), visitor)
    }
}
