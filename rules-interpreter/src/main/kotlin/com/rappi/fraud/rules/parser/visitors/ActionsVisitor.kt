package com.rappi.fraud.rules.parser.visitors

import com.rappi.fraud.analang.ANAParser
import kotlin.streams.toList

class ActionsVisitor {
    fun visit(context: ANAParser.ActionsContext): Map<String, Map<String, String>> {
        return context.action()
            .stream()
            .map { action ->
                val name = when {
                    action.K_ACTION() != null -> action.param_value.text.replace("'", "")
                    action.action_id.text == "manual_review" -> action.action_id.text.replace("'", "")
                    action.action_id.text == "apply_restriction" -> action.action_id.text.replace("'", "")
                    else -> error("Cannot find action name or identifier in ${action.text}")
                }
                Pair(name, ActionVisitor().visit(action))
            }
            .toList()
            .toMap()

    }
}

class ActionVisitor {

    fun visit(action: ANAParser.ActionContext): Map<String, String> {

        return if (action.action_params() != null)
            action.action_params().string_literal().asSequence().withIndex()
                .map { (i, node) -> IndexedValue<String>(i, node.text) }
                .map { (i, name) ->  IndexedValue(i, name.replace("'","")) }
                .map { (i, name) ->
                    Pair(name, action.action_params().validValue(i).text.replace("'", ""))
                }
                .toMap()
        else mapOf()
    }
}
