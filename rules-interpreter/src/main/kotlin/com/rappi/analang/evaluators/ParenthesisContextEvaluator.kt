package com.github.iamrenny.rulesflow.evaluators

import com.rappi.fraud.analang.ANAParser.ParenthesisContext
import com.github.iamrenny.rulesflow.visitors.Visitor

class ParenthesisContextEvaluator : ContextEvaluator<ParenthesisContext> {

    override fun evaluate(ctx: ParenthesisContext, visitor: Visitor): Any {
        return visitor.visit(ctx.expr())
    }
}