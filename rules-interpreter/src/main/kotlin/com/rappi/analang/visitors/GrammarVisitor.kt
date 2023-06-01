package com.rappi.analang.visitors

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.removeSingleQuote

class GrammarVisitor : ANABaseVisitor<String>() {

    override fun visitParse(ctx: ANAParser.ParseContext): String {
        return ctx.workflow().workflow_name().text.removeSingleQuote()
    }
}