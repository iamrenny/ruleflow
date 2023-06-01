package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.visitors.Visitor

class BinaryOrContextEvaluator : ContextEvaluator<ANAParser.BinaryOrContext> {

    override fun evaluate(ctx: ANAParser.BinaryOrContext, visitor: Visitor): Boolean {
        return visitor.visit(ctx.left) as Boolean || visitor.visit(ctx.right) as Boolean
    }
}