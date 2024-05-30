package com.github.iamrenny.rulesflow.visitors

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.github.iamrenny.rulesflow.errors.PropertyNotFoundException
import com.github.iamrenny.rulesflow.removeSingleQuote
import com.github.iamrenny.rulesflow.vo.Action
import com.github.iamrenny.rulesflow.vo.WorkflowResult
import org.slf4j.LoggerFactory

class RulesetVisitor(private val data: Map<String, *>, private val lists:  Map<String, Set<String>>) : ANABaseVisitor<WorkflowResult>() {
    val logger = LoggerFactory.getLogger(RulesetVisitor::class.java)

    override fun visitParse(ctx: ANAParser.ParseContext): WorkflowResult {
        return visitWorkflow(ctx.workflow())
    }

    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): WorkflowResult {
        val visitor = Visitor(data, lists, data)
        val warnings: MutableSet<String> = mutableSetOf()

        ctx.rulesets()
            .forEach { ruleSet ->
                ruleSet.rules()
                    .forEach { rule ->
                        try {
                            val visitedRule = visitor.visit(rule.rule_body().expr())
                            if (visitedRule  is Boolean && visitedRule) {

                                val exprResult = if (rule.rule_body().return_result().expr() != null) {
                                    visitor.visit(rule.rule_body().return_result().expr())
                                } else {
                                    rule.rule_body().return_result().state().ID().text
                                }
                                return workflowResult(rule, ctx, ruleSet, exprResult, warnings)
                            }
                        } catch (ex: PropertyNotFoundException) {
                            logger.warn(ex.message)
                            warnings.add(ex.message!!)
                        } catch (ex: Exception) {
                            logger.error(
                                "Error while evaluating rule ${ctx.workflow_name().text} ${rule.name().text}",
                                ex
                            )
                            warnings.add(ex.message ?: "Unexpected Exception at ${rule.text}")
                        }
                    }
            }

        return resolveDefaultResult(ctx, warnings, visitor)
    }

    private fun resolveDefaultResult(
        ctx: ANAParser.WorkflowContext,
        warnings: MutableSet<String>,
        evaluator: Visitor
    ): WorkflowResult {
        val (actionsList, actionsMap) = if (ctx.default_clause().actions() != null) {
            resolveActions(ctx.default_clause().actions())
        } else {
            Pair(listOf(), mapOf())
        }

        if (ctx.default_clause().return_result().expr() != null) {


            val solvedExpr = evaluator.visit(ctx.default_clause().return_result().expr())

            return WorkflowResult(
                workflow = ctx.workflow_name().text.removeSingleQuote(),
                ruleSet = "default",
                rule = "default",
                result = solvedExpr.toString(),
                actions = actionsMap.keys,
                actionsWithParams = actionsMap,
                actionsList = actionsList,
                warnings = warnings
            )
        } else if(ctx.default_clause().return_result().state() != null) {
            return WorkflowResult(
                workflow = ctx.workflow_name().text.removeSingleQuote(),
                ruleSet = "default",
                rule = "default",
                result = ctx.default_clause().return_result().state().ID().text,
                warnings = warnings,
                actions = actionsMap.keys,
                actionsWithParams = actionsMap,
                actionsList = actionsList,
            )
        } else {
            throw RuntimeException("No default result found")
        }
    }



    private fun workflowResult(
        rule: ANAParser.RulesContext,
        ctx: ANAParser.WorkflowContext,
        ruleSet: ANAParser.RulesetsContext,
        expr: Any?,
        warnings: MutableSet<String>
    ): WorkflowResult {
        val result =  WorkflowResult(
            workflow = ctx.workflow_name().text.removeSingleQuote(),
            ruleSet = ruleSet.name().text.removeSingleQuote(),
            rule = rule.name().text.removeSingleQuote(),
            result = expr.toString(),
            warnings = warnings
        )
        return if (rule.rule_body().actions() == null) {
            result
        } else {
            val (actionsList, actionsMap) = resolveActions(rule.rule_body().actions())
            result.copy(
                actions = actionsMap.keys,
                actionsWithParams = actionsMap,
                actionsList = actionsList
            )
        }
    }

    private fun resolveActions(rule: ANAParser.ActionsContext): Pair<List<Action>, Map<String, Map<String, String>>> {
        val actions = ActionsVisitor().visit(rule)
        val actionsList = actions.map { action -> Action(action.first, action.second) }
        val actionsMap = actions.toMap()
        return Pair(actionsList, actionsMap)
    }
}