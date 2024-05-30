package com.github.iamrenny.rulesflow.evaluators

import com.rappi.fraud.analang.ANAParser.RegexlikeContext
import com.github.iamrenny.rulesflow.visitors.Visitor

class RegexContextEvaluator: ContextEvaluator<RegexlikeContext> {
    override fun evaluate(ctx: RegexlikeContext, visitor: Visitor): Any {
        val value = visitor.visit(ctx.value)


        return Regex(ctx.regex.text.replace("'", "")).replace(value.toString(), "")
    }
}