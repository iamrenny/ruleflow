package io.github.iamrenny.ruleflow.visitors;


import io.github.iamrenny.ruleflow.RuleFlowLanguageBaseVisitor;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;

public class GrammarVisitor extends RuleFlowLanguageBaseVisitor<String> {

    @Override
    public String visitParse(RuleFlowLanguageParser.ParseContext ctx) {
        return removeSingleQuote(ctx.workflow().workflow_name().getText());
    }

    private String removeSingleQuote(String text) {
        return text.replace("'", "");
    }
}