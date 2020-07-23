package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANAParser
import kotlin.streams.toList

class ActionsEvaluator {
    fun evaluate(context: ANAParser.ActionsContext): Map<String, Map<String, String>> {
        return context.action()
            .stream()
            .map { action ->
                val name = when {
                    action.K_ACTION() != null -> action.param_value.text.replace("'", "")
                    action.action_id.text == "manual_review" -> action.action_id.text.replace("'", "")
                    action.action_id.text == "apply_restriction" -> action.action_id.text.replace("'", "")
                    else -> error("Cannot find action name or identifier in ${action.text}")
                }
                Pair(name, ActionEvaluator().evaluate(action))
            }
            .toList()
            .toMap()

    }
}

class ActionEvaluator {

    fun evaluate(action: ANAParser.ActionContext): Map<String, String> {

        return if (action.action_params() != null)
            action.action_params().STRING_LITERAL().asSequence().withIndex()
                .map { (i, node) -> IndexedValue<String>(i, node.text) }
                .map { (i, name) ->  IndexedValue(i, name.replace("'","")) }
                .map { (i, name) ->
                    Pair(name, action.action_params().validValue(i).text.replace("'", ""))
                }
                .toMap()
        else mapOf()
    }
}
