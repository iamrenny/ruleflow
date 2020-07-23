package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser.BinaryContext
import com.rappi.fraud.rules.parser.evaluators.Visitor

class BinaryCondition : Condition<BinaryContext> {

    override fun eval(ctx: BinaryContext, evaluator: Visitor): Boolean {
        return when (ctx.op.type) {
            ANALexer.K_AND -> evaluator.visit(ctx.left) as Boolean && evaluator.visit(ctx.right) as Boolean
            ANALexer.K_OR -> evaluator.visit(ctx.left) as Boolean || evaluator.visit(ctx.right) as Boolean
            else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.type}")
        }
    }
}