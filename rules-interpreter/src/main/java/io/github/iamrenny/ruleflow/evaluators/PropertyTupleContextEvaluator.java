package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.PropertyTupleContext;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.ValidPropertyContext;
import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import java.util.ArrayList;
import java.util.List;

public class PropertyTupleContextEvaluator implements ContextEvaluator<PropertyTupleContext> {

  @Override
  public Object evaluate(PropertyTupleContext ctx, Visitor visitor)
      throws PropertyNotFoundException {
    List<String> propertyValues = new ArrayList<>();
    for (ValidPropertyContext validPropertContext : ctx.validProperty()){
      propertyValues.add((String) visitor.visit(validPropertContext));
    }
    return propertyValues;
  }
}
