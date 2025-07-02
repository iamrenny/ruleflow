package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.PropertyTupleContext;
import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.ValidPropertyContext;
import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.visitors.Visitor;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyTupleContextEvaluator implements ContextEvaluator<PropertyTupleContext> {
    private static final Logger logger = LoggerFactory.getLogger(PropertyTupleContextEvaluator.class);

  @Override
  public Object evaluate(PropertyTupleContext ctx, Visitor visitor)
      throws PropertyNotFoundException {
    List<String> propertyValues = new ArrayList<>();
    for (ValidPropertyContext validPropertContext : ctx.validProperty()){
      String visit = (String) visitor.visit(validPropertContext);
      logger.debug("PropertyTuple: propertyName={}", visit);
      propertyValues.add(visit);
    }
    return propertyValues;
  }
}
