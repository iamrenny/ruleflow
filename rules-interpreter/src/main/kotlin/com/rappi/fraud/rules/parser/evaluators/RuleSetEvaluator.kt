package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.removeSingleQuote
import com.rappi.fraud.rules.parser.vo.WorkflowResult

class RuleSetEvaluator(private val data: Map<String, *>) : ANABaseVisitor<WorkflowResult>() {

    companion object {
        val EMPTY_SET = emptySet<String>()
    }

    override fun visitParse(ctx: ANAParser.ParseContext): WorkflowResult {
        return visitWorkflow(ctx.workflow())
    }

    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): WorkflowResult {
        val ruleEvaluator = ConditionEvaluator(data)
        ctx.rulesets()
            .forEach { ruleSet ->
                ruleSet.rules()
                    .forEach { rule ->
                        val visitedRule = ruleEvaluator.visit(rule.expr())
                        if (visitedRule is Boolean) {
                            if(visitedRule)
                                return WorkflowResult(
                                    workflow = ctx.name().text.removeSingleQuote(),
                                    ruleSet = ruleSet.name().text.removeSingleQuote(),
                                    rule = rule.name().text.removeSingleQuote(),
                                    risk = rule.result.text,
                                    actions = rule.actions()?.action()?.map { it.text }?.toSet() ?: EMPTY_SET
                                )
                        }
                    }
            }
        return WorkflowResult(
            workflow = ctx.name().text.removeSingleQuote(),
            risk = ctx.defaultResult.result.text
        )
    }
}