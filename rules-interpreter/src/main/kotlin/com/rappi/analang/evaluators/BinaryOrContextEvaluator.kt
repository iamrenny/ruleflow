package com.rappi.analang.evaluators

import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.visitors.Visitor

class BinaryOrContextEvaluator : ContextEvaluator<ANAParser.BinaryOrContext> {

    override fun evaluate(ctx: ANAParser.BinaryOrContext, visitor: Visitor): Boolean {
        return visitor.visit(ctx.left) as Boolean || visitor.visit(ctx.right) as Boolean
    }
}