package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser.LogicalContext
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator

class LogicalCondition : Condition<LogicalContext> {

    override fun eval(ctx: LogicalContext, data: Map<String, *>): Boolean {
        val evaluator = ConditionEvaluator(data)
        return when (ctx.op.type) {
            ANALexer.K_AND -> evaluator.visit(ctx.left) && evaluator.visit(ctx.right)
            ANALexer.K_OR -> evaluator.visit(ctx.left) || evaluator.visit(ctx.right)
            else -> throw IllegalArgumentException("Operation not supported ${ctx.cond()}")
        }
    }
}