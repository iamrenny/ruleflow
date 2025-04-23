package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageLexer;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.ValidPropertyContext;
import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiValuesListContextEvaluator implements ContextEvaluator<RuleFlowLanguageParser.MultiValueslistContext> {

    @Override
    public Object evaluate(RuleFlowLanguageParser.MultiValueslistContext ctx, Visitor visitor) throws PropertyNotFoundException {
        if (ctx.not == null && ctx.op.getType() == RuleFlowLanguageLexer.K_CONTAINS) {
            return evalContains(ctx, visitor);
        } else if (ctx.not != null && ctx.op.getType() == RuleFlowLanguageLexer.K_CONTAINS) {
            return !(Boolean) evalContains(ctx, visitor);
        } else if (ctx.not == null && ctx.op.getType() == RuleFlowLanguageLexer.K_IN) {
            return evalIn(ctx, visitor);
        } else if (ctx.not != null && ctx.op.getType() == RuleFlowLanguageLexer.K_IN) {
            return !(Boolean) evalIn(ctx, visitor);
        } else if (ctx.not == null && ctx.op.getType() == RuleFlowLanguageLexer.K_STARTS_WITH) {
            return evalStartsWith(ctx, visitor);
        } else if (ctx.not != null && ctx.op.getType() == RuleFlowLanguageLexer.K_STARTS_WITH) {
            return !(Boolean) evalStartsWith(ctx, visitor);
        } else {
            throw new RuntimeException("Unexpected token near " + ctx.values.getText());
        }
    }

    private Object evalIn(RuleFlowLanguageParser.MultiValueslistContext ctx, Visitor visitor) throws PropertyNotFoundException {

        if (ctx.values.literalMultiList != null) {
            List<String> propertiesValues = (List<String>) visitor.visit(ctx.value);
            List<String> literals = ctx.values.string_literal().stream()
                .map(literal -> literal.getText().replace("'", ""))
                .collect(Collectors.toList());

            int tupleSize = propertiesValues.size(); // or any other size based on your rule

            List<List<String>> tuples = new ArrayList<>();
            for (int i = 0; i < literals.size(); i += tupleSize) {
                int end = Math.min(i + tupleSize, literals.size());
                tuples.add(new ArrayList<>(literals.subList(i, end)));
            }

            boolean res = tuples.stream()
                .anyMatch(tuple -> {
                    if(tuple.size() != tupleSize) {
                        throw new RuntimeException("Tuple size mismatch: expected " + tupleSize + " but got " + tuple.size());
                    }
                    for (int i = 0; i < tuple.size(); i++) {
                        if (!propertiesValues.get(i).equals(tuple.get(i))) {
                            return false;
                        }
                    }
                    return true;
                });
            return res;
        } else if (ctx.values.storedList != null) {
            String listKey = ctx.values.string_literal(0).getText().replace("'", "");
            List<?> list = visitor.getLists().get(listKey);

            if (list != null && list.stream().allMatch(item -> item instanceof List<?>)) {
                for (Object listItem : list) {
                    if (listItem instanceof List<?>) {
                        List<?> tuple = (List<?>) listItem;
                        if(ctx.propertyTuple().validProperty().size() != tuple.size()) {
                            throw new RuntimeException("Tuple size mismatch: expected " + ctx.propertyTuple().validProperty().size() + " but got " + tuple.size());
                        }

                        boolean matches = true;
                        for(int j = 0; j < tuple.size(); j++) {
                            Object item = tuple.get(j);
                            if (item instanceof String) {
                                String stringItem = (String) item;
                                String validPropertyContext = (String) visitor.visit(ctx.propertyTuple().validProperty(j));
                                if (!stringItem.equals(validPropertyContext)) {
                                    matches = false;
                                    break;
                                }
                            } else {
                                throw new RuntimeException("Invalid type in tuple: " + item.getClass());
                            }
                        }

                        if (matches) {
                            return true;
                        }
                    }
                }
                return false;
            } else if (list != null && list.stream().anyMatch(item -> item instanceof String)) {

                boolean contains = list.contains(null);
                return contains;
            } else {
                return false;
            }

        } else if (ctx.values.validProperty() != null) {
            List<?> validPropertyList = (List<?>) visitor.visit(ctx.values.validProperty());
            return validPropertyList.contains(null);
        } else {
            throw new RuntimeException("Cannot find symbol " + ctx.values);
        }
    }

    private Object evalContains(RuleFlowLanguageParser.MultiValueslistContext ctx, Visitor visitor) {
        Object value = visitor.visit(ctx);

        if (ctx.values.literalList != null) {
            List<String> literals = ctx.values.string_literal().stream()
                .map(literal -> literal.getText().replace("'", ""))
                .collect(Collectors.toList());
            return literals.stream().anyMatch(literal -> value.toString().contains(literal));
        } else if (ctx.values.storedList != null) {
            String listKey = ctx.values.string_literal(0).getText().replace("'", "");
            List<?> list =  visitor.getLists().get(listKey);
            return list != null && list.stream().anyMatch(item -> value.toString().contains(item.toString()));
        } else {
            throw new RuntimeException("Cannot find symbol");
        }
    }

    private Object evalStartsWith(RuleFlowLanguageParser.MultiValueslistContext ctx, Visitor visitor) throws PropertyNotFoundException {
        Object value = visitor.visit(ctx.values);

        if (ctx.values.literalList != null) {
            List<String> literals = ctx.values.string_literal().stream()
                .map(literal -> literal.getText().replace("'", ""))
                .collect(Collectors.toList());
            return literals.stream().anyMatch(literal -> value.toString().startsWith(literal));
        } else if (ctx.values.storedList != null) {
            String listKey = ctx.values.string_literal(0).getText().replace("'", "");
            List<?> list = visitor.getLists().get(listKey);
            return list != null && list.stream().anyMatch(item -> value.toString().startsWith(item.toString()));
        } else {
            throw new RuntimeException("Cannot find symbol");
        }
    }
}