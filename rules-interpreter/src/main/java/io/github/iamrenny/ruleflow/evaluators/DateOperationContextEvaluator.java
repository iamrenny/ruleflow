package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DateOperationContext;
import io.github.iamrenny.ruleflow.errors.PropertyNotFoundException;
import io.github.iamrenny.ruleflow.errors.UnexpectedSymbolException;
import io.github.iamrenny.ruleflow.visitors.Visitor;

public class DateOperationContextEvaluator implements ContextEvaluator<io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DateOperationContext> {

  @Override
  public Object evaluate(DateOperationContext ctx, Visitor visitor)
      throws PropertyNotFoundException, UnexpectedSymbolException {
     if (ctx.dateExpr() != null) {
         return visitor.visit(ctx.dateExpr());
     } else {
         throw new UnexpectedSymbolException("Unexpected symbol " + ctx);
     }
  }
}
