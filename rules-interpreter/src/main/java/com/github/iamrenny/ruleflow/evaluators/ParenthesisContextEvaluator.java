package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageParser.ParenthesisContext;
import com.github.iamrenny.ruleflow.visitors.Visitor;

public class ParenthesisContextEvaluator implements ContextEvaluator<ParenthesisContext> {

    @Override
    public Object evaluate(ParenthesisContext ctx, Visitor visitor) {
        return visitor.visit(ctx.expr());
    }
}