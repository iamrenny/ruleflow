package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser

class RuleSetEvaluator(private val data: Map<String, *>) : ANABaseVisitor<String>() {

    override fun visitParse(ctx: ANAParser.ParseContext): String {
        return visitWorkflow(ctx.workflow())
    }

    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): String {
        ctx.stmt_list().stmt().forEach {
            if (ConditionEvaluator(data).visit(it.cond())) {
                return it.result_value().text.replace("'", "")
            }
        }

        return ctx.stmt_list().default_stmt().result_value().text.replace("'", "")
    }
}