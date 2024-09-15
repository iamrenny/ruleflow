package com.github.iamrenny.ruleflow.visitors;

import com.github.iamrenny.ruleflow.RuleFlowLanguageBaseVisitor;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;

public class GrammarVisitor extends RuleFlowLanguageBaseVisitor<String> {

    @Override
    public String visitParse(RuleFlowLanguageParser.ParseContext ctx) {
        return removeSingleQuote(ctx.workflow().workflow_name().getText());
    }

    private String removeSingleQuote(String text) {
        return text.replace("'", "");
    }
}