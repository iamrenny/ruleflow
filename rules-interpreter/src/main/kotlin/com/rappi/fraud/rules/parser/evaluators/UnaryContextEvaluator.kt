package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.visitors.Visitor
import kotlin.math.absoluteValue

class UnaryContextEvaluator : ContextEvaluator<ANAParser.UnaryContext> {

    override fun evaluate(ctx: ANAParser.UnaryContext, visitor: Visitor): Any {
        val valLeft = visitor.visit(ctx.left)
        val left = valLeft.toString().toDoubleOrNull()

        if(left == null)
            throw IllegalArgumentException("parameter value not supported: $valLeft")

        return when (ctx.op.type) {
            ANALexer.ABS -> left.absoluteValue
            else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.text}")
        }

    }

}