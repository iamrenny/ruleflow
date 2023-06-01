package com.rappi.analang.evaluators

import com.rappi.fraud.analang.ANAParser.ParenthesisContext
import com.rappi.analang.visitors.Visitor

class ParenthesisContextEvaluator : ContextEvaluator<ParenthesisContext> {

    override fun evaluate(ctx: ParenthesisContext, visitor: Visitor): Any {
        return visitor.visit(ctx.expr())
    }
}