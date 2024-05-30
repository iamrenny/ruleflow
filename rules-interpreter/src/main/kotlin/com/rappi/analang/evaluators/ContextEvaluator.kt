package com.github.iamrenny.rulesflow.evaluators

import com.github.iamrenny.rulesflow.visitors.Visitor
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.RuleContext

interface ContextEvaluator<T: ParserRuleContext> {

    fun evaluate(ctx: T, visitor: Visitor): Any
}