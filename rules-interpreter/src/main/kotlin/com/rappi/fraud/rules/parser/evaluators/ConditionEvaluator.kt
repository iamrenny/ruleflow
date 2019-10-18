package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.conditions.*

class ConditionEvaluator(private val data: Map<String, *>) : ANABaseVisitor<Boolean>() {

    companion object {
        private val STRING_CONDITION = StringCondition()
        private val NUMERIC_CONDITION = NumericCondition()
        private val LOGICAL_CONDITION = LogicalCondition()
        private val LIST_CONDITION = ListCondition()
        private val COUNT_CONDITION = CountCondition()
        private val AVERAGE_CONDITION = AverageCondition()
    }

    override fun visitString(ctx: ANAParser.StringContext?): Boolean {
        return STRING_CONDITION.eval(ctx!!, data)
    }

    override fun visitNumber(ctx: ANAParser.NumberContext?): Boolean {
        return NUMERIC_CONDITION.eval(ctx!!, data)
    }

    override fun visitLogical(ctx: ANAParser.LogicalContext?): Boolean {
        return LOGICAL_CONDITION.eval(ctx!!, data)
    }

    override fun visitList(ctx: ANAParser.ListContext?): Boolean {
        return LIST_CONDITION.eval(ctx!!, data)
    }

    override fun visitCount(ctx: ANAParser.CountContext?): Boolean {
        return COUNT_CONDITION.eval(ctx!!, data)
    }

    override fun visitAverage(ctx: ANAParser.AverageContext?): Boolean {
        return AVERAGE_CONDITION.eval(ctx!!, data)
    }

    override fun visitParens(ctx: ANAParser.ParensContext?): Boolean {
        return visit(ctx!!.cond())
    }
}