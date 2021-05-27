package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser.BinaryContext
import com.rappi.fraud.rules.parser.visitors.Visitor

class BinaryContextEvaluator : ContextEvaluator<BinaryContext> {

    override fun evaluate(ctx: BinaryContext, visitor: Visitor): Boolean {
        return when (ctx.op.type) {
            ANALexer.K_AND -> visitor.visit(ctx.left) as Boolean && visitor.visit(ctx.right) as Boolean
            ANALexer.K_OR -> visitor.visit(ctx.left) as Boolean || visitor.visit(ctx.right) as Boolean
            else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.type}")
        }
    }
}