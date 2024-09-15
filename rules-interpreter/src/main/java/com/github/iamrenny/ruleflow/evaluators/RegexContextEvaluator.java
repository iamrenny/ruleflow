package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageParser.RegexlikeContext;
import com.github.iamrenny.ruleflow.visitors.Visitor;

import java.util.regex.Pattern;

public class RegexContextEvaluator implements ContextEvaluator<RegexlikeContext> {

    @Override
    public Object evaluate(RegexlikeContext ctx, Visitor visitor) {
        Object value = visitor.visit(ctx.value);

        String regexPattern = ctx.regex.getText().replace("'", "");
        String input = value.toString();

        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(input).replaceAll("");
    }
}