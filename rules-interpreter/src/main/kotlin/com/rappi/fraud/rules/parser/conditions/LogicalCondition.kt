package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.LogicalContext
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator

class LogicalCondition : Condition<LogicalContext> {

    override fun eval(ctx: LogicalContext, data: Map<String, *>): Boolean {
        val evaluator = ConditionEvaluator(data)
        return when {
            ctx.K_AND() != null -> evaluator.visit(ctx.cond(0)) && evaluator.visit(ctx.cond(1))
            ctx.K_OR() != null -> evaluator.visit(ctx.cond(0)) || evaluator.visit(ctx.cond(1))
            else -> throw IllegalArgumentException("Operation not supported ${ctx.cond()}")
        }
    }
}