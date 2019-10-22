package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import org.antlr.v4.runtime.ParserRuleContext

interface Condition<T : ParserRuleContext> {

    fun eval(ctx: T, data: Map<String, *>): Boolean
}