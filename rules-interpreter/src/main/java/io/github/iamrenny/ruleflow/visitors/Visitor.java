package io.github.iamrenny.ruleflow.visitors;


import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.evaluators.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Map;
import java.util.Set;

public class Visitor extends RuleFlowLanguageBaseVisitor<Object> {
    private final Map<String, ?> data;
    private final Map<String, Set<String>> lists;
    private final Map<String, ?> root;

    public Visitor(Map<String, ?> data, Map<String, Set<String>> lists, Map<String, ?> root) {
        this.data = data;
        this.lists = lists != null ? lists : Map.of();
        this.root = root;
    }

    @Override
    public Object visit(ParseTree tree) {
        ParserRuleContext ctx = (ParserRuleContext) tree;

        try {
            if (ctx instanceof RuleFlowLanguageParser.ComparatorContext) {
                return new ComparatorContextEvaluator().evaluate((RuleFlowLanguageParser.ComparatorContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.AggregationContext) {
                return new AggregationContextEvaluator().evaluate((RuleFlowLanguageParser.AggregationContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.MathMulContext) {
                return new MathMulContextEvaluator().evaluate((RuleFlowLanguageParser.MathMulContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.MathAddContext) {
                return new MathAddContextEvaluator().evaluate((RuleFlowLanguageParser.MathAddContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.ParenthesisContext) {
                return new ParenthesisContextEvaluator().evaluate((RuleFlowLanguageParser.ParenthesisContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.ValueContext) {
                return new ValueContextEvaluator().evaluate((RuleFlowLanguageParser.ValueContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.PropertyContext) {
                return new PropertyContextEvaluator().evaluate((RuleFlowLanguageParser.PropertyContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.ValidPropertyContext) {
                return new ValidPropertyContextEvaluator().evaluate((RuleFlowLanguageParser.ValidPropertyContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.DateDiffContext) {
                return new DateDiffContextEvaluator().evaluate((RuleFlowLanguageParser.DateDiffContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.ListContext) {
                return new ListContextEvaluator().evaluate((RuleFlowLanguageParser.ListContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.UnaryContext) {
                return new UnaryContextEvaluator().evaluate((RuleFlowLanguageParser.UnaryContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.BinaryAndContext) {
                return new BinaryAndContextEvaluator().evaluate((RuleFlowLanguageParser.BinaryAndContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.BinaryOrContext) {
                return new BinaryOrContextEvaluator().evaluate((RuleFlowLanguageParser.BinaryOrContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.DayOfWeekContext) {
                return new DayOfWeekContextEvaluator().evaluate((RuleFlowLanguageParser.DayOfWeekContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.RegexlikeContext) {
                return new RegexContextEvaluator().evaluate((RuleFlowLanguageParser.RegexlikeContext) ctx, this);
            } else {
                throw new IllegalArgumentException("Operation not supported: " + ctx.getClass());
            }
        } catch (PropertyNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, ?> getData() {
        return data;
    }

    public Map<String, Set<String>> getLists() {
        return lists;
    }

    public Map<String, ?> getRoot() {
        return root;
    }
}