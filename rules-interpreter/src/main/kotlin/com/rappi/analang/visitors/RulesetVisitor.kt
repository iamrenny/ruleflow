package com.rappi.analang.visitors

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.errors.PropertyNotFoundException
import com.rappi.analang.removeSingleQuote
import com.rappi.analang.vo.Action
import com.rappi.analang.vo.WorkflowResult
import org.slf4j.LoggerFactory

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
                            if (visitedRule  is Boolean && visitedRule) {
                                val res = if(rule.return_result().expr() != null) {
                                    ruleEvaluator.visit(rule.return_result().expr())
                                } else {
                                    rule.return_result().state().ID().text
                                }

                                val result = WorkflowResult(
                                    workflow = ctx.workflow_name().text.removeSingleQuote(),
                                    ruleSet = ruleSet.name().text.removeSingleQuote(),
                                    rule = rule.name().text.removeSingleQuote(),
                                    result = res.toString(),
                                    warnings = warnings
                                )

                                return if (rule.actions() == null) {
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

    private fun visitActions(rule: ANAParser.RulesContext): Pair<List<Action>, Map<String, Map<String, String>>> {
        val actions = ActionsVisitor().visit(rule.actions())
        val actionsList = actions.map { action -> Action(action.first, action.second) }
        val actionsMap = actions.toMap()
        return Pair(actionsList, actionsMap)
    }
}