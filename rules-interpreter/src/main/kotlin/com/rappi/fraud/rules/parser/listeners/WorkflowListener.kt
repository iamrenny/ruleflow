package com.rappi.fraud.rules.parser.listeners

import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANAParser

class WorkflowListener : ANABaseListener() {

    override fun enterWorkflow(ctx: ANAParser.WorkflowContext) {
        println("Setting workflow name as: ${ctx.STRING_LITERAL(0)}")
        println("Setting ruleSet name as: ${ctx.STRING_LITERAL(1)}")

    }

    override fun exitStmt(ctx: ANAParser.StmtContext?) {
        println("Condition is: ${ctx!!.cond().text}")
    }
}