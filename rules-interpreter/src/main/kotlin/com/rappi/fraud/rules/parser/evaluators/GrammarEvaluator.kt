package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.removeSingleQuote

class GrammarEvaluator : ANABaseVisitor<String>() {

    override fun visitParse(ctx: ANAParser.ParseContext): String {
        return ctx.workflow().name().text.removeSingleQuote()
    }
}