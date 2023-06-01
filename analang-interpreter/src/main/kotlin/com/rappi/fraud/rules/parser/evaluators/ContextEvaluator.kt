package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.rules.parser.visitors.Visitor
import org.antlr.v4.runtime.ParserRuleContext

interface ContextEvaluator<T: ParserRuleContext> {

    fun evaluate(ctx: T, visitor: Visitor): Any
}