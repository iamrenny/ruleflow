package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageLexer;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.MathAddContext;
import io.github.iamrenny.ruleflow.visitors.Visitor;

import java.math.RoundingMode;

public class MathAddContextEvaluator implements ContextEvaluator<MathAddContext> {

    @Override
    public Object evaluate(MathAddContext ctx, Visitor visitor) {
        Object leftVal = visitor.visit(ctx.left);
        Object rightVal = visitor.visit(ctx.right);

        Double left = Double.valueOf(leftVal.toString());
        Double right = Double.valueOf(rightVal.toString());

        switch (ctx.op.getType()) {
            case RuleFlowLanguageLexer.ADD:
                return left + right;
            case RuleFlowLanguageLexer.MINUS:
                return left - right;
            default:
                throw new IllegalArgumentException("Operation not supported: " + ctx.op.getText());
        }
    }
}