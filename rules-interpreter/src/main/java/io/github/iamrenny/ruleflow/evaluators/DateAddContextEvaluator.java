package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.visitors.Visitor;
import io.github.iamrenny.ruleflow.utils.DateTimeUtils;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateAddContextEvaluator implements ContextEvaluator<io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DateAddContext> {
    private static final Logger logger = LoggerFactory.getLogger(DateAddContextEvaluator.class);
    @Override
    public Object evaluate(io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DateAddContext ctx, Visitor visitor) {
        java.time.ZonedDateTime zdt = (ZonedDateTime) new DateValueContextEvaluator().evaluate(ctx.date, visitor);
        Object unitObj = ctx.unit.getText();
        long amount = ((Double) visitor.visit(ctx.amount)).longValue();
        String unit = unitObj.toString();
        switch (unit.toLowerCase()) {
            case "day":
                return zdt.plusDays(amount);
            case "hour":
                return zdt.plusHours(amount);
            case "minute":
                return zdt.plusMinutes(amount);
            default:
                throw new IllegalArgumentException("Unsupported time unit: " + unit);
        }
    }
} 