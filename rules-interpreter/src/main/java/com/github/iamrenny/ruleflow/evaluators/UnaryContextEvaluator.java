package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageLexer;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser.UnaryContext;
import com.github.iamrenny.ruleflow.visitors.Visitor;

public class UnaryContextEvaluator implements ContextEvaluator<UnaryContext> {

    @Override
    public Object evaluate(UnaryContext ctx, Visitor visitor) {
        Object valLeft = visitor.visit(ctx.left);
        Double left = toDoubleOrNull(valLeft.toString());

        if (left == null) {
            throw new IllegalArgumentException("Parameter value not supported: " + valLeft);
        }

        switch (ctx.op.getType()) {
            case RuleFlowLanguageLexer.ABS:
                return Math.abs(left);
            default:
                throw new IllegalArgumentException("Operation not supported: " + ctx.op.getText());
        }
    }

    private Double toDoubleOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}