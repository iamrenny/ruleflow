// Generated from /Users/ivanvercinsky/Documents/rappi/fraud-rules-engine/rules-interpreter/src/main/antlr/com/rappi/fraud/analang/ANA.g4 by ANTLR 4.7.2

 package com.rappi.fraud.analang;
 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ANAParser}.
 */
public interface ANAListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ANAParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(ANAParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(ANAParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#error}.
	 * @param ctx the parse tree
	 */
	void enterError(ANAParser.ErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#error}.
	 * @param ctx the parse tree
	 */
	void exitError(ANAParser.ErrorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#workflow}.
	 * @param ctx the parse tree
	 */
	void enterWorkflow(ANAParser.WorkflowContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#workflow}.
	 * @param ctx the parse tree
	 */
	void exitWorkflow(ANAParser.WorkflowContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#stmt_list}.
	 * @param ctx the parse tree
	 */
	void enterStmt_list(ANAParser.Stmt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#stmt_list}.
	 * @param ctx the parse tree
	 */
	void exitStmt_list(ANAParser.Stmt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(ANAParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(ANAParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#default_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDefault_stmt(ANAParser.Default_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#default_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDefault_stmt(ANAParser.Default_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(ANAParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(ANAParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#list_op}.
	 * @param ctx the parse tree
	 */
	void enterList_op(ANAParser.List_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#list_op}.
	 * @param ctx the parse tree
	 */
	void exitList_op(ANAParser.List_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#count}.
	 * @param ctx the parse tree
	 */
	void enterCount(ANAParser.CountContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#count}.
	 * @param ctx the parse tree
	 */
	void exitCount(ANAParser.CountContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#result_value}.
	 * @param ctx the parse tree
	 */
	void enterResult_value(ANAParser.Result_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#result_value}.
	 * @param ctx the parse tree
	 */
	void exitResult_value(ANAParser.Result_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#any_name}.
	 * @param ctx the parse tree
	 */
	void enterAny_name(ANAParser.Any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#any_name}.
	 * @param ctx the parse tree
	 */
	void exitAny_name(ANAParser.Any_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ANAParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_value(ANAParser.Literal_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ANAParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_value(ANAParser.Literal_valueContext ctx);
}