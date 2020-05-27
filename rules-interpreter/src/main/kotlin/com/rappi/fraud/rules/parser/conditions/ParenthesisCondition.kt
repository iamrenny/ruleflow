package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser.ParenthesisContext
import com.rappi.fraud.rules.parser.evaluators.ConditionEvaluator

class ParenthesisCondition : Condition<ParenthesisContext> {

    override fun eval(ctx: ParenthesisContext, evaluator: ConditionEvaluator): Any? {
        return evaluator.visit(ctx.expr())
    }
}