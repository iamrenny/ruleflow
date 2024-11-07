package io.github.iamrenny.ruleflow.util;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionsVisitor {

    public List<Pair<String, Map<String, String>>> visit(RuleFlowLanguageParser.ActionsContext context) {
        return context.action().stream()
            .map(action -> {
            String name;
            if (action.K_ACTION() != null) {
                name = action.param_value.getText().replace("'", "");
            } else if ("manual_review".equals(action.action_id.getText())) {
                name = action.action_id.getText().replace("'", "");
            } else if ("apply_restriction".equals(action.action_id.getText())) {
                name = action.action_id.getText().replace("'", "");
            } else if (action.action_id.getText() != null) {
                name = action.action_id.getText().replace("'", "");
            } else {
                throw new IllegalArgumentException("Cannot find action name or identifier in " + action.getText());
            }
            return new Pair<>(name, new ActionVisitor().visit(action));
        })
        .collect(Collectors.toList());
    }
}

class ActionVisitor {

    public Map<String, String> visit(RuleFlowLanguageParser.ActionContext action) {
        if (action.action_params() != null) {
            return action.action_params().param_pairs().param_pair().stream()
                .collect(Collectors.toMap(
                    a -> a.field_name.getText().replace("'", ""),
            a -> a.field_value.getText().replace("'", "")
            ));
        } else {
            return Map.of();
        }
    }
}