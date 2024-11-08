package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.ParenthesisContext;
import io.github.iamrenny.ruleflow.visitors.Visitor;

public class ParenthesisContextEvaluator implements ContextEvaluator<ParenthesisContext> {

    @Override
    public Object evaluate(ParenthesisContext ctx, Visitor visitor) {
        return visitor.visit(ctx.expr());
    }
}