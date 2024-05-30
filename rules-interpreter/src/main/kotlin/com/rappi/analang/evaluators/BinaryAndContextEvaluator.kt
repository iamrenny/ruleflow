package com.github.iamrenny.rulesflow.evaluators

import com.rappi.fraud.analang.ANAParser
import com.github.iamrenny.rulesflow.visitors.Visitor

class BinaryAndContextEvaluator : ContextEvaluator<ANAParser.BinaryAndContext> {

    override fun evaluate(ctx: ANAParser.BinaryAndContext, visitor: Visitor): Boolean {
        return visitor.visit(ctx.left) as Boolean && visitor.visit(ctx.right) as Boolean
        }
}