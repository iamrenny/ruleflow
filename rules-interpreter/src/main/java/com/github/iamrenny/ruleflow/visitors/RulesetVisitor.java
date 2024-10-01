package com.github.iamrenny.ruleflow.visitors;

import com.github.iamrenny.ruleflow.RuleFlowLanguageBaseVisitor;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import com.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import com.github.iamrenny.ruleflow.util.ActionsVisitor;
import com.github.iamrenny.ruleflow.utils.Pair;
import com.github.iamrenny.ruleflow.vo.Action;
import com.github.iamrenny.ruleflow.vo.WorkflowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class RulesetVisitor extends RuleFlowLanguageBaseVisitor<WorkflowResult> {
    private static final Logger logger = LoggerFactory.getLogger(RulesetVisitor.class);
    private final Map<String, ?> data;
    private final Map<String, Set<String>> lists;

    public RulesetVisitor(Map<String, ?> data, Map<String, Set<String>> lists) {
        this.data = data;
        this.lists = lists;
    }

    @Override
    public WorkflowResult visitParse(RuleFlowLanguageParser.ParseContext ctx) {
        return visitWorkflow(ctx.workflow());
    }

    @Override
    public WorkflowResult visitWorkflow(RuleFlowLanguageParser.WorkflowContext ctx) {
        Visitor visitor = new Visitor(data, lists, data);
        Set<String> warnings = new HashSet<>();

        for (RuleFlowLanguageParser.RulesetsContext ruleSet : ctx.rulesets()) {
            for (RuleFlowLanguageParser.RulesContext rule : ruleSet.rules()) {
                try {
                    Object visitedRule = visitor.visit(rule.rule_body().expr());
                    if (visitedRule instanceof Boolean && (Boolean) visitedRule) {
                        Object exprResult;
                        if (rule.rule_body().return_result().expr() != null) {
                            exprResult = visitor.visit(rule.rule_body().return_result().expr());
                        } else {
                            exprResult = rule.rule_body().return_result().state().ID().getText();
                        }
                        return workflowResult(rule, ctx, ruleSet, exprResult, warnings);
                    }
                } catch (Exception ex) {
                    logger.error("Error while evaluating rule {} {}", ctx.workflow_name().getText(), rule.name().getText(), ex);
                    warnings.add(ex.getMessage() != null ? ex.getMessage() : "Unexpected Exception at " + rule.getText());
                }
            }
        }

        return resolveDefaultResult(ctx, warnings, visitor);
    }

    private WorkflowResult resolveDefaultResult(
        RuleFlowLanguageParser.WorkflowContext ctx,
        Set<String> warnings,
        Visitor evaluator) {
        List<Action> actionsList = new ArrayList<>();
        Map<String, Map<String, String>> actionsMap = new HashMap<>();

        if (ctx.default_clause().actions() != null) {
            Pair<List<Action>, Map<String, Map<String, String>>> resolvedActions = resolveActions(ctx.default_clause().actions());
            actionsList = resolvedActions.getKey();
            actionsMap = resolvedActions.getValue();
        }

        if (ctx.default_clause().return_result().expr() != null) {
            Object solvedExpr = evaluator.visit(ctx.default_clause().return_result().expr());
            return new WorkflowResult(
                ctx.workflow_name().getText().replace("'", ""),
                "default",
                "default",
                solvedExpr.toString(),
                actionsMap.keySet(),
                warnings,
                actionsMap,
                null,
                actionsList,
                false
            );
        } else if (ctx.default_clause().return_result().state() != null) {
            return new WorkflowResult(
                removeSingleQuote(ctx.workflow_name().getText()),
                "default",
                "default",
                ctx.default_clause().return_result().state().ID().getText(),
                actionsMap.keySet(),
                warnings,
                actionsMap,
                null,
                actionsList,
                true
            );
        } else {
            throw new RuntimeException("No default result found");
        }
    }

    private WorkflowResult workflowResult(
        RuleFlowLanguageParser.RulesContext rule,
        RuleFlowLanguageParser.WorkflowContext ctx,
        RuleFlowLanguageParser.RulesetsContext ruleSet,
        Object expr,
        Set<String> warnings) {
        WorkflowResult result = new WorkflowResult(
            removeSingleQuote(ctx.workflow_name().getText()),
            removeSingleQuote(ruleSet.name().getText()),
            removeSingleQuote(rule.name().getText()),
            expr.toString(),
            warnings
        );
        if (rule.rule_body().actions() == null) {
            return result;
        } else {
            Pair<List<Action>, Map<String, Map<String, String>>> resolvedActions = resolveActions(rule.rule_body().actions());
            result.setActionsWithParams(resolvedActions.getValue());
            return result;
        }
    }

    private Pair<List<Action>, Map<String, Map<String, String>>> resolveActions(RuleFlowLanguageParser.ActionsContext rule) {
        List<com.github.iamrenny.ruleflow.utils.Pair<String, Map<String, String>>> actions = new ActionsVisitor().visit(rule);
        List<Action> actionsList = actions.stream().map(action -> new Action(action.getKey(), action.getValue())).collect(Collectors.toList());
        Map<String, Map<String, String>> actionsMap = actions.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (existing, replacement) -> {
                    existing.putAll(replacement); // merging the two maps
                    return existing;
                }));
        return new Pair<>(actionsList, actionsMap);
    }

    private String removeSingleQuote(String text) {
        return text.replace("'", "");
    }
}