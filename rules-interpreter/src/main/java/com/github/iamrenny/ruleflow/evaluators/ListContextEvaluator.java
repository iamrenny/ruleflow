package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageLexer;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import com.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import com.github.iamrenny.ruleflow.visitors.Visitor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.ListContext> {

    @Override
    public Object evaluate(RuleFlowLanguageParser.ListContext ctx, Visitor visitor) throws PropertyNotFoundException {
        if (ctx.not == null && ctx.op.getType() == RuleFlowLanguageLexer.K_CONTAINS) {
            return evalContains(ctx, visitor);
        } else if (ctx.not != null && ctx.op.getType() == RuleFlowLanguageLexer.K_CONTAINS) {
            return !(Boolean) evalContains(ctx, visitor);
        } else if (ctx.not == null && ctx.op.getType() == RuleFlowLanguageLexer.K_IN) {
            return evalIn(ctx, visitor);
        } else if (ctx.not != null && ctx.op.getType() == RuleFlowLanguageLexer.K_IN) {
            return !(Boolean) evalIn(ctx, visitor);
        } else if (ctx.not == null && ctx.op.getType() == RuleFlowLanguageLexer.K_STARTS_WITH) {
            return evalStartsWith(ctx, visitor);
        } else if (ctx.not != null && ctx.op.getType() == RuleFlowLanguageLexer.K_STARTS_WITH) {
            return !(Boolean) evalStartsWith(ctx, visitor);
        } else {
            throw new RuntimeException("Unexpected token near " + ctx.value.getText());
        }
    }

    private Object evalIn(RuleFlowLanguageParser.ListContext ctx, Visitor visitor) throws PropertyNotFoundException {
        Object value = visitor.visit(ctx.value);

        if (ctx.values.literalList != null) {
            List<String> literals = ctx.values.string_literal().stream()
                .map(literal -> literal.getText().replace("'", ""))
                .toList();
            return literals.contains(value);
        } else if (ctx.values.storedList != null) {
            String listKey = ctx.values.string_literal(0).getText().replace("'", "");
            Set<String> list = visitor.getLists().get(listKey);
            return list != null && list.contains(value.toString());
        } else if (ctx.values.validProperty() != null) {
            List<?> validPropertyList = (List<?>) new ValidPropertyContextEvaluator().evaluate(ctx.values.validProperty(), visitor);
            return validPropertyList.contains(value);
        } else {
            throw new RuntimeException("Cannot find symbol " + ctx.values);
        }
    }

    private Object evalContains(RuleFlowLanguageParser.ListContext ctx, Visitor visitor) {
        Object value = visitor.visit(ctx.value);

        if (ctx.values.literalList != null) {
            List<String> literals = ctx.values.string_literal().stream()
                .map(literal -> literal.getText().replace("'", ""))
                .collect(Collectors.toList());
            return literals.stream().anyMatch(literal -> value.toString().contains(literal));
        } else if (ctx.values.storedList != null) {
            String listKey = ctx.values.string_literal(0).getText().replace("'", "");
            Set<String> list = visitor.getLists().get(listKey);
            return list != null && list.stream().anyMatch(item -> value.toString().contains(item));
        } else {
            throw new RuntimeException("Cannot find symbol");
        }
    }

    private Object evalStartsWith(RuleFlowLanguageParser.ListContext ctx, Visitor visitor) throws PropertyNotFoundException {
        String value = visitor.visit(ctx.value).toString();
        List<?> list;

        if (ctx.values.literalList != null) {
            list = ctx.values.string_literal().stream()
                .map(literal -> literal.getText().replace("'", ""))
                .collect(Collectors.toList());
        } else if (ctx.values.storedList != null) {
            String listKey = ctx.values.string_literal(0).getText().replace("'", "");
            list = visitor.getLists().get(listKey).stream().collect(Collectors.toList());
        } else if (ctx.values.validProperty() != null) {
            list = (List<?>) new ValidPropertyContextEvaluator().evaluate(ctx.values.validProperty(), visitor);
        } else {
            throw new RuntimeException("Unexpected symbol " + ctx.values);
        }

        return list.stream()
            .anyMatch(elem -> value.startsWith(elem.toString()));
    }
}