package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageLexer;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser.MathAddContext;
import com.github.iamrenny.ruleflow.visitors.Visitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathAddContextEvaluator implements ContextEvaluator<MathAddContext> {

    @Override
    public Object evaluate(MathAddContext ctx, Visitor visitor) {
        Object leftVal = visitor.visit(ctx.left);
        Object rightVal = visitor.visit(ctx.right);

        BigDecimal left = new BigDecimal(leftVal.toString()).setScale(2, RoundingMode.DOWN);
        BigDecimal right = new BigDecimal(rightVal.toString()).setScale(2, RoundingMode.DOWN);

        switch (ctx.op.getType()) {
            case RuleFlowLanguageLexer.ADD:
                return left.add(right);
            case RuleFlowLanguageLexer.MINUS:
                return left.subtract(right);
            default:
                throw new IllegalArgumentException("Operation not supported: " + ctx.op.getText());
        }
    }
}