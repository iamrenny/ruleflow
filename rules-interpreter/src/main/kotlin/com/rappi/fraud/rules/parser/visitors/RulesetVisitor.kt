package com.rappi.fraud.rules.parser.visitors

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.errors.PropertyNotFoundException
import com.rappi.fraud.rules.parser.removeSingleQuote
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.slf4j.LoggerFactory
import java.lang.Exception

class RulesetVisitor(private val data: Map<String, *>, private val lists:  Map<String, Set<String>>) : ANABaseVisitor<WorkflowResult>() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun visitParse(ctx: ANAParser.ParseContext): WorkflowResult {
        return visitWorkflow(ctx.workflow())
    }

    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): WorkflowResult {
        val ruleEvaluator = Visitor(data, lists, data)
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
                                        workflow = ctx.workflow_name().text.removeSingleQuote(),
                                        ruleSet = ruleSet.name().text.removeSingleQuote(),
                                        rule = rule.name().text.removeSingleQuote(),
                                        risk = rule.result.text,
                                        warnings = warnings
                                    ).let {
                                        if(rule.actions() != null) {
                                            val actions = ActionsVisitor().visit(rule.actions())
                                            it.copy(
                                                actions = actions.keys,
                                                actionsWithParams = actions
                                            )
                                        } else {
                                            it
                                        }

                                    }
                            }
                        } catch (ex: Exception){
                            val message = ex.message ?: "Unexpected Exception at ${rule.text}"
                            when(ex) {
                                is PropertyNotFoundException -> {
                                    logger.warn(message)
                                }
                                else -> {
                                    logger.error("Error while evaluating rule ${ctx.workflow_name().text} ${rule.name().text}", ex)
                                }
                            }
                            warnings.add(message)
                        }
                    }
            }
        return WorkflowResult(
            workflow = ctx.workflow_name().text.removeSingleQuote(),
            ruleSet = "default",
            rule = "default",
            risk = ctx.defaultResult.result.text,
            warnings = warnings
        )
    }
}