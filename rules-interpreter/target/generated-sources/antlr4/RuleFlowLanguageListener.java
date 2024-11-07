// Generated from RuleFlowLanguage.g4 by ANTLR 4.9.2

package com.github.iamrenny.ruleflow;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RuleFlowLanguageParser}.
 */
public interface RuleFlowLanguageListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(RuleFlowLanguageParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(RuleFlowLanguageParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#error}.
	 * @param ctx the parse tree
	 */
	void enterError(RuleFlowLanguageParser.ErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#error}.
	 * @param ctx the parse tree
	 */
	void exitError(RuleFlowLanguageParser.ErrorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#workflow}.
	 * @param ctx the parse tree
	 */
	void enterWorkflow(RuleFlowLanguageParser.WorkflowContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#workflow}.
	 * @param ctx the parse tree
	 */
	void exitWorkflow(RuleFlowLanguageParser.WorkflowContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#workflow_name}.
	 * @param ctx the parse tree
	 */
	void enterWorkflow_name(RuleFlowLanguageParser.Workflow_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#workflow_name}.
	 * @param ctx the parse tree
	 */
	void exitWorkflow_name(RuleFlowLanguageParser.Workflow_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#string_literal}.
	 * @param ctx the parse tree
	 */
	void enterString_literal(RuleFlowLanguageParser.String_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#string_literal}.
	 * @param ctx the parse tree
	 */
	void exitString_literal(RuleFlowLanguageParser.String_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#rulesets}.
	 * @param ctx the parse tree
	 */
	void enterRulesets(RuleFlowLanguageParser.RulesetsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#rulesets}.
	 * @param ctx the parse tree
	 */
	void exitRulesets(RuleFlowLanguageParser.RulesetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#rules}.
	 * @param ctx the parse tree
	 */
	void enterRules(RuleFlowLanguageParser.RulesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#rules}.
	 * @param ctx the parse tree
	 */
	void exitRules(RuleFlowLanguageParser.RulesContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#rule_body}.
	 * @param ctx the parse tree
	 */
	void enterRule_body(RuleFlowLanguageParser.Rule_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#rule_body}.
	 * @param ctx the parse tree
	 */
	void exitRule_body(RuleFlowLanguageParser.Rule_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(RuleFlowLanguageParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(RuleFlowLanguageParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#default_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_clause(RuleFlowLanguageParser.Default_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#default_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_clause(RuleFlowLanguageParser.Default_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#return_result}.
	 * @param ctx the parse tree
	 */
	void enterReturn_result(RuleFlowLanguageParser.Return_resultContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#return_result}.
	 * @param ctx the parse tree
	 */
	void exitReturn_result(RuleFlowLanguageParser.Return_resultContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#state}.
	 * @param ctx the parse tree
	 */
	void enterState(RuleFlowLanguageParser.StateContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#state}.
	 * @param ctx the parse tree
	 */
	void exitState(RuleFlowLanguageParser.StateContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#actions}.
	 * @param ctx the parse tree
	 */
	void enterActions(RuleFlowLanguageParser.ActionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#actions}.
	 * @param ctx the parse tree
	 */
	void exitActions(RuleFlowLanguageParser.ActionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(RuleFlowLanguageParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(RuleFlowLanguageParser.ActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#action_params}.
	 * @param ctx the parse tree
	 */
	void enterAction_params(RuleFlowLanguageParser.Action_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#action_params}.
	 * @param ctx the parse tree
	 */
	void exitAction_params(RuleFlowLanguageParser.Action_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#param_pairs}.
	 * @param ctx the parse tree
	 */
	void enterParam_pairs(RuleFlowLanguageParser.Param_pairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#param_pairs}.
	 * @param ctx the parse tree
	 */
	void exitParam_pairs(RuleFlowLanguageParser.Param_pairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#param_pair}.
	 * @param ctx the parse tree
	 */
	void enterParam_pair(RuleFlowLanguageParser.Param_pairContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#param_pair}.
	 * @param ctx the parse tree
	 */
	void exitParam_pair(RuleFlowLanguageParser.Param_pairContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dateDiff}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDateDiff(RuleFlowLanguageParser.DateDiffContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dateDiff}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDateDiff(RuleFlowLanguageParser.DateDiffContext ctx);
	/**
	 * Enter a parse tree produced by the {@code regexlike}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRegexlike(RuleFlowLanguageParser.RegexlikeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code regexlike}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRegexlike(RuleFlowLanguageParser.RegexlikeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryOr}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOr(RuleFlowLanguageParser.BinaryOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryOr}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOr(RuleFlowLanguageParser.BinaryOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryAnd}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryAnd(RuleFlowLanguageParser.BinaryAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryAnd}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryAnd(RuleFlowLanguageParser.BinaryAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aggregation}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAggregation(RuleFlowLanguageParser.AggregationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aggregation}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAggregation(RuleFlowLanguageParser.AggregationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unary}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnary(RuleFlowLanguageParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unary}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnary(RuleFlowLanguageParser.UnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code list}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterList(RuleFlowLanguageParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code list}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitList(RuleFlowLanguageParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenthesis(RuleFlowLanguageParser.ParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenthesis(RuleFlowLanguageParser.ParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparator}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterComparator(RuleFlowLanguageParser.ComparatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparator}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitComparator(RuleFlowLanguageParser.ComparatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dayOfWeek}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDayOfWeek(RuleFlowLanguageParser.DayOfWeekContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dayOfWeek}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDayOfWeek(RuleFlowLanguageParser.DayOfWeekContext ctx);
	/**
	 * Enter a parse tree produced by the {@code property}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterProperty(RuleFlowLanguageParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code property}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitProperty(RuleFlowLanguageParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mathAdd}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMathAdd(RuleFlowLanguageParser.MathAddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mathAdd}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMathAdd(RuleFlowLanguageParser.MathAddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mathMul}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMathMul(RuleFlowLanguageParser.MathMulContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mathMul}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMathMul(RuleFlowLanguageParser.MathMulContext ctx);
	/**
	 * Enter a parse tree produced by the {@code value}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterValue(RuleFlowLanguageParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code value}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitValue(RuleFlowLanguageParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#listElems}.
	 * @param ctx the parse tree
	 */
	void enterListElems(RuleFlowLanguageParser.ListElemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#listElems}.
	 * @param ctx the parse tree
	 */
	void exitListElems(RuleFlowLanguageParser.ListElemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#validValue}.
	 * @param ctx the parse tree
	 */
	void enterValidValue(RuleFlowLanguageParser.ValidValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#validValue}.
	 * @param ctx the parse tree
	 */
	void exitValidValue(RuleFlowLanguageParser.ValidValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RuleFlowLanguageParser#validProperty}.
	 * @param ctx the parse tree
	 */
	void enterValidProperty(RuleFlowLanguageParser.ValidPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleFlowLanguageParser#validProperty}.
	 * @param ctx the parse tree
	 */
	void exitValidProperty(RuleFlowLanguageParser.ValidPropertyContext ctx);
}