// Generated from /Users/ivanvercinsky/Documents/rappi/fraud-rules-engine/rules-interpreter/src/main/antlr/com/rappi/fraud/analang/ANA.g4 by ANTLR 4.7.2

 package com.rappi.fraud.analang;
 
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ANAParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ANAVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ANAParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(ANAParser.ParseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#error}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError(ANAParser.ErrorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#workflow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWorkflow(ANAParser.WorkflowContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#stmt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt_list(ANAParser.Stmt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(ANAParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#default_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_stmt(ANAParser.Default_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(ANAParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#list_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_op(ANAParser.List_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#count}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCount(ANAParser.CountContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#result_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_value(ANAParser.Result_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_name(ANAParser.Any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ANAParser#literal_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral_value(ANAParser.Literal_valueContext ctx);
}