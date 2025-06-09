package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DateParseExprContext;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import io.github.iamrenny.ruleflow.utils.DateTimeUtils;
import java.time.ZonedDateTime;

public class DateParseExprContextEvaluator implements ContextEvaluator<DateParseExprContext> {
    public Object evaluate(RuleFlowLanguageParser.DateParseExprContext ctx, Visitor visitor) {
        if(ctx.dateParse().K_DATE()!= null) {
            if(ctx.dateParse().dateValue().K_NOW() != null) {
                return ZonedDateTime.now().toLocalDate();
            } else {
                Object value = visitor.visit(ctx.dateParse().dateValue());
                return ZonedDateTime.parse(value.toString()).toLocalDate();
            }
        } else if(ctx.dateParse().K_DATETIME()!= null) {
            if(ctx.dateParse().dateValue().K_NOW() != null) {
                return ZonedDateTime.now();
            } else {
                Object value = visitor.visit(ctx.dateParse().dateValue());
                return ZonedDateTime.parse(value.toString());
            }
        } else {
            throw new IllegalArgumentException("Date not supported: " + ctx.getText());
        }
    }
} 