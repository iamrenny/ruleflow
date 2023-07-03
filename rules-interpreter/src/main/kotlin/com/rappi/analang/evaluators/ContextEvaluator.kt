package com.rappi.analang.evaluators

import com.rappi.analang.visitors.Visitor
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.RuleContext

interface ContextEvaluator<T: ParserRuleContext> {

    fun evaluate(ctx: T, visitor: Visitor): Any
}