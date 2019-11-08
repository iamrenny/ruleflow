package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.conditions.*
import org.antlr.v4.runtime.tree.ParseTree
import org.omg.CORBA.Object

class ConditionEvaluator(val data: Map<String, *>) : ANABaseVisitor<Any>() {

    override fun visit(tree: ParseTree?): Any? {
        val ctx = tree as ANAParser.CondContext
        return ConditionFactory.get(ctx).eval(ctx, this)
    }
}