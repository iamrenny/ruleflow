package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.ParenthesisContext
import com.rappi.fraud.rules.parser.evaluators.Visitor

class ParenthesisCondition : Condition<ParenthesisContext> {

    override fun eval(ctx: ParenthesisContext, visitor: Visitor): Any {
        return visitor.visit(ctx.expr())
    }
}