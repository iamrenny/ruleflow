package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageParser.ValueContext;
import com.github.iamrenny.ruleflow.visitors.Visitor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class ValueContextEvaluator implements ContextEvaluator<ValueContext> {

    @Override
    public Object evaluate(ValueContext ctx, Visitor visitor) {
        if (ctx.validValue().string != null) {
            return (ctx.validValue().string.getText()).replace("'", "");
        } else if (ctx.validValue().number != null) {
            return Double.valueOf(ctx.validValue().number.getText());
        } else if (ctx.validValue().nullValue != null) {
            return ctx.validValue().nullValue;
        } else if (ctx.validValue().currentDate != null) {
            return LocalDateTime.now();
        } else if (ctx.validValue().booleanLiteral != null) {
            return Boolean.parseBoolean(ctx.validValue().booleanLiteral.getText());
        } else {
            throw new IllegalArgumentException("Value not supported: " + ctx.getText());
        }
    }
}