package com.cs652;// Generated from /Users/terence/Dropbox/temp/mm/antlr/Lispy.g4 by ANTLR 4.x
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LispyParser}.
 */
public interface LispyListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LispyParser#sexprBoolean}.
	 * @param ctx the parse tree
	 */
	void enterSexprBoolean(@NotNull LispyParser.SexprBooleanContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispyParser#sexprBoolean}.
	 * @param ctx the parse tree
	 */
	void exitSexprBoolean(@NotNull LispyParser.SexprBooleanContext ctx);

	/**
	 * Enter a parse tree produced by {@link LispyParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(@NotNull LispyParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispyParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(@NotNull LispyParser.ProgContext ctx);

	/**
	 * Enter a parse tree produced by {@link LispyParser#sexprlist}.
	 * @param ctx the parse tree
	 */
	void enterSexprlist(@NotNull LispyParser.SexprlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispyParser#sexprlist}.
	 * @param ctx the parse tree
	 */
	void exitSexprlist(@NotNull LispyParser.SexprlistContext ctx);

	/**
	 * Enter a parse tree produced by {@link LispyParser#sexpr}.
	 * @param ctx the parse tree
	 */
	void enterSexpr(@NotNull LispyParser.SexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispyParser#sexpr}.
	 * @param ctx the parse tree
	 */
	void exitSexpr(@NotNull LispyParser.SexprContext ctx);

	/**
	 * Enter a parse tree produced by {@link LispyParser#sexprNumber}.
	 * @param ctx the parse tree
	 */
	void enterSexprNumber(@NotNull LispyParser.SexprNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispyParser#sexprNumber}.
	 * @param ctx the parse tree
	 */
	void exitSexprNumber(@NotNull LispyParser.SexprNumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link LispyParser#sexprIdent}.
	 * @param ctx the parse tree
	 */
	void enterSexprIdent(@NotNull LispyParser.SexprIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispyParser#sexprIdent}.
	 * @param ctx the parse tree
	 */
	void exitSexprIdent(@NotNull LispyParser.SexprIdentContext ctx);
}