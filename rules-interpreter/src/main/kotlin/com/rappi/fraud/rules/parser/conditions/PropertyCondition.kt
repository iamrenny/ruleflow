package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.Visitor

class PropertyCondition: Condition<ANAParser.PropertyContext> {
    override fun eval(ctx: ANAParser.PropertyContext, visitor: Visitor): Any? {
        val validPropertyCondition = ValidPropertyCondition()
        return validPropertyCondition.eval(ctx.validProperty(), visitor)
    }
}
