package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.removeSingleQuote
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import java.lang.Exception

class RuleSetEvaluator(private val data: Map<String, *>, private val lists:  Map<String, List<String>>) : ANABaseVisitor<WorkflowResult>() {

    companion object {
        val EMPTY_SET = emptySet<String>()
    }

    override fun visitParse(ctx: ANAParser.ParseContext): WorkflowResult {
        return visitWorkflow(ctx.workflow())
    }

    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): WorkflowResult {
        val ruleEvaluator = Visitor(data, lists)
        val warnings: MutableSet<String> = mutableSetOf()

        ctx.rulesets()
            .forEach { ruleSet ->
                ruleSet.rules()
                    .forEach { rule ->
                        try {
                            val visitedRule = ruleEvaluator.visit(rule.expr())
                            if (visitedRule is Boolean) {
                                if (visitedRule)
                                    return WorkflowResult(
                                        workflow = ctx.name().text.removeSingleQuote(),
                                        ruleSet = ruleSet.name().text.removeSingleQuote(),
                                        rule = rule.name().text.removeSingleQuote(),
                                        risk = rule.result.text,
                                        actions = rule.actions()?.action()?.map { it.text }?.toSet() ?: EMPTY_SET,
                                        warnings = warnings
                                    )
                            }
                        } catch (ex: Exception){
                            warnings.add(ex.message ?: "Unexpected Exception at ${rule.text}")
                        }
                    }
            }
        return WorkflowResult(
            workflow = ctx.name().text.removeSingleQuote(),
            ruleSet = "default",
            rule = "default",
            risk = ctx.defaultResult.result.text,
            warnings = warnings
        )
    }
}