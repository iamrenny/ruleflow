package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageLexer;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.visitors.Visitor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class DayOfWeekContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.DayOfWeekContext> {

    @Override
    public Object evaluate(RuleFlowLanguageParser.DayOfWeekContext ctx, Visitor visitor) {
        ZonedDateTime valLeft = (ZonedDateTime) new DateValueContextEvaluator().evaluate(ctx.dateValue(), visitor);

        if (valLeft == null) {
            throw new IllegalArgumentException("Parameter value not supported: " + ctx.dateValue().getText());
        }

        switch (ctx.op.getType()) {
            case RuleFlowLanguageLexer.DAY_OF_WEEK:
                return valLeft.getDayOfWeek().toString();
            default:
                throw new IllegalArgumentException("Operation not supported: " + ctx.op.getText());
        }
    }
}