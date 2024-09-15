package com.github.iamrenny.ruleflow.evaluators;

import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser.ComparatorContext;
import com.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import com.github.iamrenny.ruleflow.visitors.Visitor;
import org.antlr.v4.runtime.Token;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class ComparatorContextEvaluator implements ContextEvaluator<ComparatorContext> {

    @Override
    public  Boolean evaluate(ComparatorContext ctx, Visitor visitor) throws PropertyNotFoundException  {
        Object left = visitor.visit(ctx.left);
        Object right = visitor.visit(ctx.right);

        if (left instanceof Number && right instanceof Number) {
            return compareComparable(
                ctx.op,
                new BigDecimal(left.toString()).setScale(10, RoundingMode.DOWN),
                new BigDecimal(right.toString()).setScale(10, RoundingMode.DOWN)
            );
        } else if (left instanceof String && right instanceof String) {
            return compareComparable(ctx.op, (String) left, (String) right);
        } else if (ctx.left.start.getType() == RuleFlowLanguageParser.K_NULL || ctx.right.start.getType() == RuleFlowLanguageParser.K_NULL) {
            return compareNull(ctx.op, left, right);
        } else if (left instanceof Boolean && List.of("true", "false").contains(((String) right).toLowerCase())) {
            return compareComparable(ctx.op, (Boolean) left, Boolean.parseBoolean((String) right));
        } else if (left instanceof Comparable<?> && right instanceof Comparable<?>) {
            return compareComparable(ctx.op, left, right);
        } else {
            return false;
        }
    }

    private <T extends Comparable<Object>> Boolean compareComparable(Token operator, Object left, Object right) {
        if(right instanceof Comparable<?> && left instanceof Comparable<?>)
            (Comparable<Object>)left.compareTo(right);
        return switch (operator.getType()) {
            case RuleFlowLanguageParser.EQ -> left.equals(right);
            case RuleFlowLanguageParser.EQ_IC -> left.toString().compareToIgnoreCase(right.toString()) == 0;
            case RuleFlowLanguageParser.NOT_EQ -> Objects.compare(left, right, ) != 0;
            case RuleFlowLanguageParser.LT -> left.compareTo(right) < 0;
            case RuleFlowLanguageParser.LT_EQ -> left.compareTo(right) <= 0;
            case RuleFlowLanguageParser.GT -> left.compareTo(right) > 0;
            case RuleFlowLanguageParser.GT_EQ -> left.compareTo(right) >= 0;
            default -> throw new RuntimeException("Invalid condition " + operator.getText());
        };
    }

    private Boolean compareNull(Token operator, Object left, Object right) {
        switch (operator.getType()) {
            case RuleFlowLanguageParser.EQ:
                return left == right;
            case RuleFlowLanguageParser.EQ_IC:
                return (left == null ? "" : left.toString()).compareToIgnoreCase(right == null ? "" : right.toString()) == 0;
            case RuleFlowLanguageParser.NOT_EQ:
                return left != right;
            default:
                return false;
        }
    }
}