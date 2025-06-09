package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import java.time.ZonedDateTime;

public class NowContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.NowContext> {
    @Override
    public Object evaluate(RuleFlowLanguageParser.NowContext ctx, Visitor visitor) {
        return ZonedDateTime.now();
    }
} 