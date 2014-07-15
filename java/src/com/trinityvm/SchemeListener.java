package com.trinityvm;// Generated from /Users/terence/Dropbox/temp/mm/antlr/Scheme.g4 by ANTLR 4.x
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SchemeParser}.
 */
public interface SchemeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SchemeParser#sexprBoolean}.
	 * @param ctx the parse tree
	 */
	void enterSexprBoolean(@NotNull SchemeParser.SexprBooleanContext ctx);
	/**
	 * Exit a parse tree produced by {@link SchemeParser#sexprBoolean}.
	 * @param ctx the parse tree
	 */
	void exitSexprBoolean(@NotNull SchemeParser.SexprBooleanContext ctx);

	/**
	 * Enter a parse tree produced by {@link SchemeParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(@NotNull SchemeParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link SchemeParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(@NotNull SchemeParser.ProgContext ctx);

	/**
	 * Enter a parse tree produced by {@link SchemeParser#sexprlist}.
	 * @param ctx the parse tree
	 */
	void enterSexprlist(@NotNull SchemeParser.SexprlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link SchemeParser#sexprlist}.
	 * @param ctx the parse tree
	 */
	void exitSexprlist(@NotNull SchemeParser.SexprlistContext ctx);

	/**
	 * Enter a parse tree produced by {@link SchemeParser#sexpr}.
	 * @param ctx the parse tree
	 */
	void enterSexpr(@NotNull SchemeParser.SexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SchemeParser#sexpr}.
	 * @param ctx the parse tree
	 */
	void exitSexpr(@NotNull SchemeParser.SexprContext ctx);

	/**
	 * Enter a parse tree produced by {@link SchemeParser#sexprNumber}.
	 * @param ctx the parse tree
	 */
	void enterSexprNumber(@NotNull SchemeParser.SexprNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SchemeParser#sexprNumber}.
	 * @param ctx the parse tree
	 */
	void exitSexprNumber(@NotNull SchemeParser.SexprNumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link SchemeParser#sexprIdent}.
	 * @param ctx the parse tree
	 */
	void enterSexprIdent(@NotNull SchemeParser.SexprIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SchemeParser#sexprIdent}.
	 * @param ctx the parse tree
	 */
	void exitSexprIdent(@NotNull SchemeParser.SexprIdentContext ctx);
}