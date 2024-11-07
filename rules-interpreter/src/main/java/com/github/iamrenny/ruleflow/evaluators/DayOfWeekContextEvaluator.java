package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageLexer;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import com.github.iamrenny.ruleflow.visitors.Visitor;

import java.time.LocalDateTime;

public class DayOfWeekContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.DayOfWeekContext> {

    @Override
    public Object evaluate(RuleFlowLanguageParser.DayOfWeekContext ctx, Visitor visitor) {
        Object valLeft = visitor.visit(ctx.left);
        LocalDateTime left = LocalDateTime.parse(valLeft.toString());

        if (left == null) {
            throw new IllegalArgumentException("Parameter value not supported: " + valLeft);
        }

        switch (ctx.op.getType()) {
            case RuleFlowLanguageLexer.DAY_OF_WEEK:
                return left.getDayOfWeek();
            default:
                throw new IllegalArgumentException("Operation not supported: " + ctx.op.getText());
        }
    }
}