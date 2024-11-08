package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DateDiffContext;
import io.github.iamrenny.ruleflow.visitors.Visitor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateDiffContextEvaluator implements ContextEvaluator<DateDiffContext> {

    @Override
    public Object evaluate(DateDiffContext ctx, Visitor visitor) {
        Object valLeft = visitor.visit(ctx.left);
        Object valRight = visitor.visit(ctx.right);
        LocalDateTime left = LocalDateTime.parse(valLeft.toString());
        LocalDateTime right = LocalDateTime.parse(valRight.toString());

        long result;

        if (ctx.MINUTE() != null) {
            result = Duration.between(left, right).toMinutes();
        } else if (ctx.HOUR() != null) {
            result = Duration.between(left, right).toHours();
        } else if (ctx.DAY() != null) {
            result = Duration.between(left, right.truncatedTo(ChronoUnit.DAYS)).toDays();
        } else {
            throw new RuntimeException("Interval not supported in " + ctx.getText());
        }

        return Math.abs(result);
    }
}