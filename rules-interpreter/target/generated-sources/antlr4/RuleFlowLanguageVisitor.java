// Generated from RuleFlowLanguage.g4 by ANTLR 4.9.2

package com.github.iamrenny.ruleflow;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RuleFlowLanguageParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RuleFlowLanguageVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(RuleFlowLanguageParser.ParseContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#error}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError(RuleFlowLanguageParser.ErrorContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#workflow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWorkflow(RuleFlowLanguageParser.WorkflowContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#workflow_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWorkflow_name(RuleFlowLanguageParser.Workflow_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#string_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_literal(RuleFlowLanguageParser.String_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#rulesets}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRulesets(RuleFlowLanguageParser.RulesetsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#rules}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRules(RuleFlowLanguageParser.RulesContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#rule_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRule_body(RuleFlowLanguageParser.Rule_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(RuleFlowLanguageParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#default_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_clause(RuleFlowLanguageParser.Default_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#return_result}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_result(RuleFlowLanguageParser.Return_resultContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState(RuleFlowLanguageParser.StateContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActions(RuleFlowLanguageParser.ActionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(RuleFlowLanguageParser.ActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#action_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction_params(RuleFlowLanguageParser.Action_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#param_pairs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_pairs(RuleFlowLanguageParser.Param_pairsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#param_pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_pair(RuleFlowLanguageParser.Param_pairContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dateDiff}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateDiff(RuleFlowLanguageParser.DateDiffContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regexlike}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegexlike(RuleFlowLanguageParser.RegexlikeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryOr}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOr(RuleFlowLanguageParser.BinaryOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryAnd}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryAnd(RuleFlowLanguageParser.BinaryAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aggregation}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregation(RuleFlowLanguageParser.AggregationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unary}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(RuleFlowLanguageParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code list}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(RuleFlowLanguageParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesis(RuleFlowLanguageParser.ParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comparator}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparator(RuleFlowLanguageParser.ComparatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dayOfWeek}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDayOfWeek(RuleFlowLanguageParser.DayOfWeekContext ctx);
	/**
	 * Visit a parse tree produced by the {@code property}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(RuleFlowLanguageParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mathAdd}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathAdd(RuleFlowLanguageParser.MathAddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mathMul}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathMul(RuleFlowLanguageParser.MathMulContext ctx);
	/**
	 * Visit a parse tree produced by the {@code value}
	 * labeled alternative in {@link RuleFlowLanguageParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(RuleFlowLanguageParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#listElems}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListElems(RuleFlowLanguageParser.ListElemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#validValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidValue(RuleFlowLanguageParser.ValidValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RuleFlowLanguageParser#validProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidProperty(RuleFlowLanguageParser.ValidPropertyContext ctx);
}