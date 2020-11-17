package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.Visitor
import kotlin.math.absoluteValue

class UnaryCondition : Condition<ANAParser.UnaryContext> {

    override fun eval(ctx: ANAParser.UnaryContext, evaluator: Visitor): Any? {
        val valLeft = evaluator.visit(ctx.left)
        val left = valLeft?.toString()?.toDoubleOrNull()

        if(left == null)
            throw IllegalArgumentException("parameter value not supported: $valLeft")

        return when (ctx.op.type) {
            ANALexer.ABS -> left.absoluteValue
            else -> throw IllegalArgumentException("Operation not supported: ${ctx.op.text}")
        }

    }

}