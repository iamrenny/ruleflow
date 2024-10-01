package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import com.github.iamrenny.ruleflow.visitors.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;

public interface ContextEvaluator<T extends ParserRuleContext> {
    Object evaluate(T ctx, Visitor visitor) throws PropertyNotFoundException;
}