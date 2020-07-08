package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.rules.parser.evaluators.Visitor
import org.antlr.v4.runtime.ParserRuleContext

interface Condition<T : ParserRuleContext> {

    fun eval(ctx: T, evaluator: Visitor): Any?
}