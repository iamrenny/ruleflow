package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageLexer;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import com.github.iamrenny.ruleflow.visitors.Visitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathMulContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.MathMulContext> {

    @Override
    public Object evaluate(RuleFlowLanguageParser.MathMulContext ctx, Visitor visitor) {
        Object leftVal = visitor.visit(ctx.left);
        Object rightVal = visitor.visit(ctx.right);

        BigDecimal left = new BigDecimal(leftVal.toString()).setScale(2, RoundingMode.DOWN);
        BigDecimal right = new BigDecimal(rightVal.toString()).setScale(2, RoundingMode.DOWN);

        switch (ctx.op.getType()) {
            case RuleFlowLanguageLexer.MULTIPLY:
                return left.multiply(right);
            case RuleFlowLanguageLexer.DIVIDE:
                if (right.compareTo(BigDecimal.ZERO) == 0) {
                    return BigDecimal.ZERO;
                }
                return left.divide(right, 2, RoundingMode.DOWN);
            case RuleFlowLanguageLexer.MODULO:
                return left.remainder(right);
            default:
                throw new IllegalArgumentException("Operation not supported: " + ctx.op.getText());
        }
    }
}