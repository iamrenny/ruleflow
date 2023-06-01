package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANAParser.ParenthesisContext
import com.rappi.fraud.rules.parser.visitors.Visitor

class ParenthesisContextEvaluator : ContextEvaluator<ParenthesisContext> {

    override fun evaluate(ctx: ParenthesisContext, visitor: Visitor): Any {
        return visitor.visit(ctx.expr())
    }
}