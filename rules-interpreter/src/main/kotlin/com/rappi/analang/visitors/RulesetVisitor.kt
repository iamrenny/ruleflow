package com.rappi.analang.visitors

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.errors.PropertyNotFoundException
import com.rappi.analang.removeSingleQuote
import com.rappi.analang.vo.Action
import com.rappi.analang.vo.WorkflowResult
import org.slf4j.LoggerFactory

class RulesetVisitor(private val data: Map<String, *>, private val lists:  Map<String, Set<String>>) : ANABaseVisitor<WorkflowResult>() {
    val logger = LoggerFactory.getLogger(RulesetVisitor::class.java)

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
                            val visitedRule = ruleEvaluator.visit(rule.rule_body().expr())
                            if (visitedRule  is Boolean && visitedRule) {
                                val exprResult = resolveExpr(rule, ruleEvaluator)

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

        return WorkflowResult(
            workflow = ctx.workflow_name().text.removeSingleQuote(),
            ruleSet = "default",
            rule = "default",
            result = ctx.default_clause().result.text,
            warnings = warnings
        )
    }

    private fun RulesetVisitor.workflowResult(
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
            val (actionsList, actionsMap) = visitActions(rule)
            result.copy(
                actions = actionsMap.keys,
                actionsWithParams = actionsMap,
                actionsList = actionsList
            )
        }
    }

    private fun resolveExpr(
        rule: ANAParser.RulesContext,
        ruleEvaluator: Visitor
    ): Any? = if (rule.rule_body().return_result().expr() != null) {
        ruleEvaluator.visit(rule.rule_body().return_result().expr())
    } else {
        rule.rule_body().return_result().state().ID().text
    }

    private fun visitActions(rule: ANAParser.RulesContext): Pair<List<Action>, Map<String, Map<String, String>>> {
        val actions = ActionsVisitor().visit(rule.rule_body().actions())
        val actionsList = actions.map { action -> Action(action.first, action.second) }
        val actionsMap = actions.toMap()
        return Pair(actionsList, actionsMap)
    }
}