package com.github.iamrenny.rulesflow.evaluators

import com.rappi.fraud.analang.ANAParser
import com.github.iamrenny.rulesflow.visitors.Visitor

class PropertyContextEvaluator: ContextEvaluator<ANAParser.PropertyContext> {
    override fun evaluate(ctx: ANAParser.PropertyContext, visitor: Visitor): Any {
        val validPropertyCondition = ValidPropertyContextEvaluator()
        return validPropertyCondition.evaluate(ctx.validProperty(), visitor)
    }
}
