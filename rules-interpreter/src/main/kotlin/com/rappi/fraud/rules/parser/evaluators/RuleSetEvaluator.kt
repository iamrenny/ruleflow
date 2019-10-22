package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.vo.WorkflowResult

class RuleSetEvaluator(private val data: Map<String, *>) : ANABaseVisitor<WorkflowResult>() {

    override fun visitParse(ctx: ANAParser.ParseContext): WorkflowResult {
        return visitWorkflow(ctx.workflow())
    }

    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): WorkflowResult {
        ctx.rulesets()
            .forEach { ruleSet ->
                ruleSet.rules()
                    .forEach { rule ->
                        if (ConditionEvaluator(data).visit(rule.cond())) {
                            return WorkflowResult(
                                workflow = ctx.workflowName.text.removeSingleQuote(),
                                ruleSet = ruleSet.ruleSetName.text.removeSingleQuote(),
                                rule = rule.name().text,
                                risk = rule.result.text
                            )
                        }
                    }
            }
        return WorkflowResult(
            workflow = ctx.workflowName.text.removeSingleQuote(),
            risk = ctx.defaultResult.result.text
        )
    }
}

fun String.removeSingleQuote() = this.replace("'", "")