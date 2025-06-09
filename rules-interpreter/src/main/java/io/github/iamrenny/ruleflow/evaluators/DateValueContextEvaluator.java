package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DateValueContext;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import io.github.iamrenny.ruleflow.utils.DateTimeUtils;
import java.time.ZonedDateTime;

public class DateValueContextEvaluator implements ContextEvaluator<DateValueContext> {
    public Object evaluate(RuleFlowLanguageParser.DateValueContext ctx, Visitor visitor) {
        if(ctx.string_literal() != null) {
            return DateTimeUtils.toZonedDateTime(ctx.string_literal().getText().replace("'",""));
        } else if(ctx.validProperty() != null) {
            String value = (String) new ValidPropertyContextEvaluator().evaluate(
                ctx.validProperty(), visitor);
            return DateTimeUtils.toZonedDateTime(value);
        } else {
            throw new IllegalArgumentException("Date not supported: " + ctx.getText());
        }
    }
} 