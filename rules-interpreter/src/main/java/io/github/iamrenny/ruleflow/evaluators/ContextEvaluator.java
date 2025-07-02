package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.errors.UnexpectedSymbolException;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Interface for context evaluators in the RuleFlow engine.
 * Implementations should provide logic for evaluating specific parse tree contexts.
 */
public interface ContextEvaluator<T extends ParserRuleContext> {
    Object evaluate(T ctx, Visitor visitor)
        throws PropertyNotFoundException, UnexpectedSymbolException;
}