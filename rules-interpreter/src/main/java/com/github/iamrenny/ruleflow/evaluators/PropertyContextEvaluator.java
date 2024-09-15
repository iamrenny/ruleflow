package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageParser.PropertyContext;
import com.github.iamrenny.ruleflow.visitors.Visitor;

public class PropertyContextEvaluator implements ContextEvaluator<PropertyContext> {

    @Override
    public Object evaluate(PropertyContext ctx, Visitor visitor) {
        ValidPropertyContextEvaluator validPropertyCondition = new ValidPropertyContextEvaluator();
        return validPropertyCondition.evaluate(ctx.validProperty(), visitor);
    }
}