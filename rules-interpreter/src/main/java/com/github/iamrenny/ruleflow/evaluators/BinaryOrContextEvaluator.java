package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import com.github.iamrenny.ruleflow.visitors.Visitor;

public class BinaryOrContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.BinaryOrContext> {

    @Override
    public Boolean evaluate(RuleFlowLanguageParser.BinaryOrContext ctx, Visitor visitor) {
        return (Boolean) visitor.visit(ctx.left) || (Boolean) visitor.visit(ctx.right);
    }
}