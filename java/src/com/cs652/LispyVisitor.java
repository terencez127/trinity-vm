package com.cs652;// Generated from /Users/terence/Dropbox/temp/mm/antlr/Lispy.g4 by ANTLR 4.x
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LispyParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LispyVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LispyParser#sexprBoolean}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexprBoolean(@NotNull LispyParser.SexprBooleanContext ctx);

	/**
	 * Visit a parse tree produced by {@link LispyParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull LispyParser.ProgContext ctx);

	/**
	 * Visit a parse tree produced by {@link LispyParser#sexprlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexprlist(@NotNull LispyParser.SexprlistContext ctx);

	/**
	 * Visit a parse tree produced by {@link LispyParser#sexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexpr(@NotNull LispyParser.SexprContext ctx);

	/**
	 * Visit a parse tree produced by {@link LispyParser#sexprNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexprNumber(@NotNull LispyParser.SexprNumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link LispyParser#sexprIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexprIdent(@NotNull LispyParser.SexprIdentContext ctx);
}