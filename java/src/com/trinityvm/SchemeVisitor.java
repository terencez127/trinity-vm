package com.trinityvm;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SchemeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SchemeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SchemeParser#sexprBoolean}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexprBoolean(@NotNull SchemeParser.SexprBooleanContext ctx);

	/**
	 * Visit a parse tree produced by {@link SchemeParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull SchemeParser.ProgContext ctx);

	/**
	 * Visit a parse tree produced by {@link SchemeParser#sexprlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexprlist(@NotNull SchemeParser.SexprlistContext ctx);

	/**
	 * Visit a parse tree produced by {@link SchemeParser#sexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexpr(@NotNull SchemeParser.SexprContext ctx);

	/**
	 * Visit a parse tree produced by {@link SchemeParser#sexprNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexprNumber(@NotNull SchemeParser.SexprNumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link SchemeParser#sexprIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSexprIdent(@NotNull SchemeParser.SexprIdentContext ctx);
}