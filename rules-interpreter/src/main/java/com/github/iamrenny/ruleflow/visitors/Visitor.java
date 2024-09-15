package com.github.iamrenny.ruleflow.visitors;



import com.github.iamrenny.ruleflow.RuleFlowLanguageBaseVisitor;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import com.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import com.github.iamrenny.ruleflow.evaluators.*;
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

        ContextEvaluator<?> condition;

        if (ctx instanceof RuleFlowLanguageParser.ComparatorContext) {
            condition = new ComparatorContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.AggregationContext) {
            condition = new AggregationContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.MathMulContext) {
            condition = new MathMulContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.MathAddContext) {
            condition = new MathAddContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.ParenthesisContext) {
            condition = new ParenthesisContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.ValueContext) {
            condition = new ValueContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.PropertyContext) {
            condition = new PropertyContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.ValidPropertyContext) {
            condition = new ValidPropertyContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.DateDiffContext) {
            condition = new DateDiffContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.ListContext) {
            condition = new ListContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.UnaryContext) {
            condition = new UnaryContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.BinaryAndContext) {
            condition = new BinaryAndContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.BinaryOrContext) {
            condition = new BinaryOrContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.DayOfWeekContext) {
            condition = new DayOfWeekContextEvaluator();
        } else if (ctx instanceof RuleFlowLanguageParser.RegexlikeContext) {
            condition = new RegexContextEvaluator();
        } else {
            throw new IllegalArgumentException("Operation not supported: " + ctx.getClass());
        }

        try {
            return condition.evaluate(ctx, this);
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