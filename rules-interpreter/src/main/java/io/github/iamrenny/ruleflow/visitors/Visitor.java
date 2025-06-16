package io.github.iamrenny.ruleflow.visitors;

import io.github.iamrenny.ruleflow.RuleFlowLanguageBaseVisitor;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.PropertyTupleContext;
import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.errors.UnexpectedSymbolException;
import io.github.iamrenny.ruleflow.evaluators.AggregationContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.BinaryAndContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.BinaryOrContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.ComparatorContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.DateDiffContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.DateOperationContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.DateParseExprContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.DateValueContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.DayOfWeekContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.ListContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.MathAddContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.MathMulContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.ParenthesisContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.PropertyContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.PropertyTupleContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.RegexContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.TupleListContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.UnaryContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.ValidPropertyContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.ValueContextEvaluator;
import io.github.iamrenny.ruleflow.evaluators.StringDistanceContextEvaluator;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class Visitor extends RuleFlowLanguageBaseVisitor<Object> {
    private final Map<String, ?> data;
    private final Map<String, List<?>> lists;
    private final Map<String, ?> root;

    public Visitor(Map<String, ?> data, Map<String, List<?>> lists, Map<String, ?> root) {
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
            } else if (ctx instanceof PropertyTupleContext) {
                return new PropertyTupleContextEvaluator().evaluate((PropertyTupleContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.DateDiffContext) {
                return new DateDiffContextEvaluator().evaluate((RuleFlowLanguageParser.DateDiffContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.DateAddContext) {
                return new io.github.iamrenny.ruleflow.evaluators.DateAddContextEvaluator().evaluate((RuleFlowLanguageParser.DateAddContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.DateSubtractContext) {
                return new io.github.iamrenny.ruleflow.evaluators.DateSubtractContextEvaluator().evaluate((RuleFlowLanguageParser.DateSubtractContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.ListContext) {
                return new ListContextEvaluator().evaluate((RuleFlowLanguageParser.ListContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.TupleListContext) {
                return new TupleListContextEvaluator().evaluate((RuleFlowLanguageParser.TupleListContext) ctx, this);
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
            } else if (ctx instanceof RuleFlowLanguageParser.DateValueContext) {
                return new DateValueContextEvaluator().evaluate((RuleFlowLanguageParser.DateValueContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.DateParseExprContext) {
                return new DateParseExprContextEvaluator().evaluate((RuleFlowLanguageParser.DateParseExprContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.DateOperationContext) {
                return new DateOperationContextEvaluator().evaluate((RuleFlowLanguageParser.DateOperationContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.NowContext) {
                return new io.github.iamrenny.ruleflow.evaluators.NowContextEvaluator().evaluate((RuleFlowLanguageParser.NowContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.StringDistanceContext) {
                return new StringDistanceContextEvaluator().evaluateStringDistance((RuleFlowLanguageParser.StringDistanceContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.PartialRatioContext) {
                return new StringDistanceContextEvaluator().evaluatePartialRatio((RuleFlowLanguageParser.PartialRatioContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.TokenSortRatioContext) {
                return new StringDistanceContextEvaluator().evaluateTokenSortRatio((RuleFlowLanguageParser.TokenSortRatioContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.TokenSetRatioContext) {
                return new StringDistanceContextEvaluator().evaluateTokenSetRatio((RuleFlowLanguageParser.TokenSetRatioContext) ctx, this);
            } else if (ctx instanceof RuleFlowLanguageParser.StringSimilarityScoreContext) {
                return new StringDistanceContextEvaluator().evaluateStringSimilarityScore((RuleFlowLanguageParser.StringSimilarityScoreContext) ctx, this);
            } else {
                throw new IllegalArgumentException("Operation not supported: " + ctx.getClass());
            }
        } catch (PropertyNotFoundException | UnexpectedSymbolException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, ?> getData() {
        return data;
    }

    public Map<String, List<?>> getLists() {
        return lists;
    }

    public Map<String, ?> getRoot() {
        return root;
    }
}