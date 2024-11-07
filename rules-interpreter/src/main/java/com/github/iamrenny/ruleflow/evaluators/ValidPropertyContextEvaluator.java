package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.ValidPropertyContext;
import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.visitors.Visitor;

import java.util.Map;

public class ValidPropertyContextEvaluator implements ContextEvaluator<ValidPropertyContext> {

    @Override
    public Object evaluate(ValidPropertyContext ctx, Visitor visitor) throws PropertyNotFoundException {
        if (ctx.property != null) {
            Object value = visitor.getData().get(ctx.ID(0).getText());
            if (value == null) {
                throw new PropertyNotFoundException(ctx.ID(0).getText() + " field cannot be found");
            }
            return value;
        } else if (ctx.nestedProperty != null) {
            Map<String, ?> data = (ctx.root != null) ? visitor.getRoot() : visitor.getData();
            return getNestedValue(ctx, data);
        } else {
            throw new PropertyNotFoundException(ctx.getText() + " field cannot be found");
        }
    }

    @SuppressWarnings("unchecked")
    private Object getNestedValue(ValidPropertyContext ctx, Map<String, ?> data) throws PropertyNotFoundException {
        Map<String, ?> currentData = data;
        for (var id : ctx.ID()) {
            Object value = currentData.get(id.getText());
            if (value instanceof Map<?, ?>) {
                currentData = (Map<String, ?>) value;
            } else {
                if (value == null) {
                    throw new PropertyNotFoundException(id.getText() + " field cannot be found");
                }
                return value;
            }
        }
        throw new PropertyNotFoundException(ctx.getText() + " cannot be found");
    }
}