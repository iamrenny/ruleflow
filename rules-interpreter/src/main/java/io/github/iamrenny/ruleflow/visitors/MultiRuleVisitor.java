package com.github.iamrenny.ruleflow.visitors;

import com.github.iamrenny.ruleflow.RuleFlowLanguageBaseVisitor;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.vo.WorkflowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MultiRuleVisitor extends RuleFlowLanguageBaseVisitor<List<io.github.iamrenny.ruleflow.vo.WorkflowResult.WorkflowResult>> {
    private static final Logger logger = LoggerFactory.getLogger(MultiRuleVisitor.class);
    private final Map<String, ?> data;
    private final Map<String, Set<String>> lists;

    public MultiRuleVisitor(Map<String, ?> data, Map<String, Set<String>> lists) {
        this.data = data;
        this.lists = lists;
    }

    @Override
    public List<io.github.iamrenny.ruleflow.vo.WorkflowResult.WorkflowResult> visitWorkflow(RuleFlowLanguageParser.WorkflowContext ctx) {
        Visitor visitor = new Visitor(data, lists, data);
        List<io.github.iamrenny.ruleflow.vo.WorkflowResult.WorkflowResult> matchedRules = new ArrayList<>();
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
                        matchedRules.add(workflowResult(rule, ctx, ruleSet, exprResult, warnings));
                    }
                } catch (Exception ex) {
                    logger.error("Error while evaluating rule {} {}", ctx.workflow_name().getText(), rule.name().getText(), ex);
                    warnings.add(ex.getMessage() != null ? ex.getMessage() : "Unexpected Exception at " + rule.getText());
                }
            }
        }

        return matchedRules.isEmpty() ? List.of(resolveDefaultResult(ctx, warnings, visitor)) : matchedRules;
    }

    private io.github.iamrenny.ruleflow.vo.WorkflowResult.WorkflowResult resolveDefaultResult(
        RuleFlowLanguageParser.WorkflowContext ctx,
        Set<String> warnings,
        Visitor evaluator) {
        String defaultDecision = ctx.default_clause().return_result().state() != null
            ? ctx.default_clause().return_result().state().ID().getText()
            : evaluator.visit(ctx.default_clause().return_result().expr()).toString();
        return new io.github.iamrenny.ruleflow.vo.WorkflowResult.WorkflowResult(
            removeSingleQuote(ctx.workflow_name().getText()),
            "default",
            "default",
            defaultDecision,
            warnings
        );
    }

    private io.github.iamrenny.ruleflow.vo.WorkflowResult.WorkflowResult workflowResult(
        RuleFlowLanguageParser.RulesContext rule,
        RuleFlowLanguageParser.WorkflowContext ctx,
        RuleFlowLanguageParser.RulesetsContext ruleSet,
        Object expr,
        Set<String> warnings) {
        return new WorkflowResult.WorkflowResult(
            removeSingleQuote(ctx.workflow_name().getText()),
            removeSingleQuote(ruleSet.name().getText()),
            removeSingleQuote(rule.name().getText()),
            expr.toString(),
            warnings
        );
    }

    private String removeSingleQuote(String text) {
        return text.replace("'", "");
    }
}