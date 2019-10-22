package com.rappi.fraud.rules.parser.listeners

import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANAParser

class WorkflowListener : ANABaseListener() {

    override fun enterWorkflow(ctx: ANAParser.WorkflowContext) {
        println("Setting workflow name as: ${ctx.workflowName.text}")
    }

    override fun enterRulesets(ctx: ANAParser.RulesetsContext?) {
        println("Setting ruleSet name as: ${ctx!!.ruleSetName.text}")
    }

    override fun exitRules(ctx: ANAParser.RulesContext?) {
        println("Statement is: ${ctx!!.cond().text}")
    }
}