package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import org.antlr.v4.runtime.Token;

import java.util.function.BiFunction;

public class ComparatorContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.ComparatorContext> {

    public Boolean evaluate(RuleFlowLanguageParser.ComparatorContext ctx, Visitor visitor) throws PropertyNotFoundException {
        Object left = visitor.visit(ctx.left);
        Object right = visitor.visit(ctx.right);

        if (left == null || right == null) {
            return compareNull(ctx.op, left, right);
        }

        // Explicit order of comparator checks
        if (left instanceof Number && right instanceof Number) {
            return compareNumbers(ctx.op, left, right);
        }

        if (left instanceof String && right instanceof String) {
            return compareStrings(ctx.op, (String) left, (String) right);
        }

        if (left instanceof Boolean && right instanceof Boolean) {
            return compareBooleans(ctx.op, (Boolean) left, (Boolean) right);
        }

        if (left instanceof java.time.ZonedDateTime && right instanceof java.time.ZonedDateTime) {
            return compareZonedDateTimes(ctx.op, (java.time.ZonedDateTime) left, (java.time.ZonedDateTime) right);
        }

        if (left instanceof Comparable<?> && right instanceof Comparable<?>) {
            return compareComparables(ctx.op, (Comparable<?>) left, (Comparable<?>) right);
        }


        // If no comparator applies
        return false;
    }

    private Boolean compareNull(Token operator, Object left, Object right) {
        return switch (operator.getType()) {
            case RuleFlowLanguageParser.EQ -> left == right;
            case RuleFlowLanguageParser.EQ_IC -> (left == null ? "" : left.toString()).equalsIgnoreCase(right == null ? "" : right.toString());
            case RuleFlowLanguageParser.NOT_EQ -> left != right;
            default -> false;
        };
    }

    // Number comparison using BigDecimal for consistent numeric comparison
    private Boolean compareNumbers(Token operator, Object left, Object right) {
        // Convert both to Double for comparison
        Double leftNum = ((Number) left).doubleValue();
        Double rightNum = ((Number) right).doubleValue();
        return compareValues(operator, Double::compareTo, leftNum, rightNum);
    }

    private Boolean compareStrings(Token operator, String left, String right) {
        return compareValues(operator, String::compareTo, left, right);
    }

    private Boolean compareBooleans(Token operator, Boolean left, Boolean right) {
        return compareValues(operator, Boolean::compareTo, left, right);
    }

    @SuppressWarnings("unchecked")
    private Boolean compareComparables(Token operator, Comparable<?> left, Comparable<?> right) {
        return compareValues(operator, Comparable::compareTo, (Comparable<Object>) left, (Comparable<Object>) right);
    }

    private <T> Boolean compareValues(Token operator, BiFunction<T, T, Integer> compareFunc, T left, T right) {
        int comparisonResult = compareFunc.apply(left, right);
        return switch (operator.getType()) {
            case RuleFlowLanguageParser.EQ -> comparisonResult == 0;
            case RuleFlowLanguageParser.EQ_IC -> left.toString().equalsIgnoreCase(right.toString());
            case RuleFlowLanguageParser.NOT_EQ -> comparisonResult != 0;
            case RuleFlowLanguageParser.LT -> comparisonResult < 0;
            case RuleFlowLanguageParser.LT_EQ -> comparisonResult <= 0;
            case RuleFlowLanguageParser.GT -> comparisonResult > 0;
            case RuleFlowLanguageParser.GT_EQ -> comparisonResult >= 0;
            default -> throw new RuntimeException("Invalid condition " + operator.getText());
        };
    }

    private Boolean compareZonedDateTimes(Token operator, java.time.ZonedDateTime left, java.time.ZonedDateTime right) {
        int comparisonResult = left.compareTo(right);
        return switch (operator.getType()) {
            case RuleFlowLanguageParser.EQ -> comparisonResult == 0;
            case RuleFlowLanguageParser.EQ_IC -> comparisonResult == 0;
            case RuleFlowLanguageParser.NOT_EQ -> comparisonResult != 0;
            case RuleFlowLanguageParser.LT -> comparisonResult < 0;
            case RuleFlowLanguageParser.LT_EQ -> comparisonResult <= 0;
            case RuleFlowLanguageParser.GT -> comparisonResult > 0;
            case RuleFlowLanguageParser.GT_EQ -> comparisonResult >= 0;
            default -> throw new RuntimeException("Invalid condition " + operator.getText());
        };
    }
}