package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.PropertyContext;
import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyContextEvaluator implements ContextEvaluator<PropertyContext> {
    private static final Logger logger = LoggerFactory.getLogger(PropertyContextEvaluator.class);

    @Override
    public Object evaluate(PropertyContext ctx, Visitor visitor) throws PropertyNotFoundException {
        ValidPropertyContextEvaluator validPropertyCondition = new ValidPropertyContextEvaluator();
        return validPropertyCondition.evaluate(ctx.validProperty(), visitor);
    }
}