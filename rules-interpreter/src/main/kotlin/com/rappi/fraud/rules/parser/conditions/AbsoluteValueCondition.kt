package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.Visitor
import kotlin.math.absoluteValue

class AbsoluteValueCondition : Condition<ANAParser.AbsoluteValueContext> {

    override fun eval(ctx: ANAParser.AbsoluteValueContext, evaluator: Visitor): Any? {
        val valLeft = evaluator.visit(ctx.left)
        val left = valLeft?.toString()?.toDoubleOrNull()
        if (left == null)
            return null

        return left!!.absoluteValue

    }

}