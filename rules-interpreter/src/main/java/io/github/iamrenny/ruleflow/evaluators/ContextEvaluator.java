package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;

public interface ContextEvaluator<T extends ParserRuleContext> {
    Object evaluate(T ctx, Visitor visitor) throws PropertyNotFoundException;
}