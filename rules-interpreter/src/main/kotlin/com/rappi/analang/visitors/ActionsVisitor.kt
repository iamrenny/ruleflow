package com.rappi.analang.visitors

import com.rappi.fraud.analang.ANAParser
import kotlin.streams.toList

class ActionsVisitor {
    fun visit(context: ANAParser.ActionsContext): List<Pair<String, Map<String, String>>> {
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
    }
}

class ActionVisitor {

    fun visit(action: ANAParser.ActionContext): Map<String, String> {

        return if (action.action_params() != null)
            action.action_params().param_pairs().param_pair()
                .map { a ->
                    Pair(a.field_name.text.replace("'",""), a.field_value.text.replace("'",""))
                }
                .toMap()
        else mapOf()
    }
}
