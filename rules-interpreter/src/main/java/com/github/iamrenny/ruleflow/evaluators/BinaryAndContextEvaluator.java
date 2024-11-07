package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.visitors.Visitor;

public class BinaryAndContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.BinaryAndContext> {

    @Override
    public Boolean evaluate(RuleFlowLanguageParser.BinaryAndContext ctx, Visitor visitor) {
        return (Boolean) visitor.visit(ctx.left) && (Boolean) visitor.visit(ctx.right);
    }
}