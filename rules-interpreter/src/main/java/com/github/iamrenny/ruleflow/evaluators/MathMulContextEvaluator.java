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

        Double left = Double.valueOf(leftVal.toString());
        Double right = Double.valueOf(rightVal.toString());

        switch (ctx.op.getType()) {
            case RuleFlowLanguageLexer.MULTIPLY:
                return left * right;
            case RuleFlowLanguageLexer.DIVIDE:
                if (right  == 0) {
                    return 0.00d;
                }
                return left / right;
            case RuleFlowLanguageLexer.MODULO:
                return left % right;
            default:
                throw new IllegalArgumentException("Operation not supported: " + ctx.op.getText());
        }
    }
}