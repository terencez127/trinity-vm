package com.cs652;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.List;

public class SimpleLispyVisitor extends LispyBaseVisitor<ParseTree> {
    private Environment global;
    private Environment curEnv;

    public SimpleLispyVisitor(Environment environment) {
        global = environment;
        curEnv = global;
    }

    public Environment swapCurEnv(Environment env) {
        Environment tmp = curEnv;
        curEnv = env;
        return tmp;
    }

    @Override
    public ParseTree visitSexprBoolean(@NotNull final LispyParser.SexprBooleanContext ctx) {
        return ctx.BOOLEAN();
    }

    @Override
    public ParseTree visitProg(@NotNull final LispyParser.ProgContext ctx) {
//        List<ParseTree> ret = new ArrayList<>();
        StringBuilder ret = new StringBuilder();
        List<LispyParser.SexprContext> sexprs = ctx.sexpr();
        ParseTree n;

        for (LispyParser.SexprContext sexpr : sexprs) {
            n = visit(sexpr);
            if (n != null) {
                ret.append(n.getText());
            }
        }

        return new TerminalNodeImpl(new CommonToken(LispyLexer.IDENT, ret.toString()));
    }

    @Override
    public ParseTree visitSexprlist(@NotNull final LispyParser.SexprlistContext ctx) {
        try {
            Environment env = curEnv;
            List<LispyParser.SexprContext> list = ctx.sexpr();
            if (list.size() > 0) {
                LispyParser.SexprContext firstSexpr = list.get(0);
                if (firstSexpr.sexprIdent() != null) {
                    ParseTree first = visitSexprIdent(firstSexpr.sexprIdent());
                    int type = ((TerminalNode)first).getSymbol().getType();

                    if (type == LispyLexer.BOOLEAN || type == LispyLexer.NUMBER) {
                        return first;
                    } else {
                        first = revokeIdent((TerminalNode)first);
                        if (first instanceof TerminalNode) {
                            type = ((TerminalNode)first).getSymbol().getType();
                            switch (type) {
                                case LispyLexer.BOOLEAN:
                                case LispyLexer.NUMBER:
                                    return first;
                            }
                        }
                    }


                    ParseTree valueTree = env.lookup(first.getText());
                    if (valueTree != null) {
                        String name = null;
                        if (valueTree instanceof NonTerminalNode) {
                            name = visitNonTerminal((NonTerminalNode) valueTree).getText();
                        } else if (valueTree instanceof  TerminalNode) {

                        } else {
                            name = first.getText();
                        }

                        Closure closure = env.getClosure(name);
                        if (closure != null) {   // Closure
                            return closure.eval(this, curEnv, ctx);
                        } else {  // Variable
                            Environment preEnv = swapCurEnv(curEnv);

                            ParseTree node = visit(valueTree);
                            swapCurEnv(preEnv);
                            return node;
                        }
                    } else {    // Undefined variable.
                        throw new EvalError(firstSexpr);
                    }
                } else if (firstSexpr.sexprBoolean() != null) {
                    return visitSexprBoolean(firstSexpr.sexprBoolean());
                } else if (firstSexpr.sexprNumber() != null) {
                    return visitSexprNumber(firstSexpr.sexprNumber());
                } else if (firstSexpr.sexprlist() != null) {
                    return visitSexprlist(firstSexpr.sexprlist());
                } else {
                    throw new EvalError(firstSexpr);
                }
            } else {
                throw new EvalError(ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ParseTree visitSexpr(@NotNull final LispyParser.SexprContext ctx)  {
        Environment env = curEnv;
        if (ctx.sexprBoolean() != null) {
            return visitSexprBoolean(ctx.sexprBoolean());
        } else if (ctx.sexprNumber() != null) {
            return visitSexprNumber(ctx.sexprNumber());
        } else if (ctx.sexprlist() != null) {
            return visitSexprlist(ctx.sexprlist());
        } else if (ctx.sexprIdent() != null) {
            String name = ctx.sexprIdent().IDENT().getText();
            ParseTree valueNode = env.lookup(name);
            if (valueNode != null) {
                if (env.getClosure(name) != null) {
                    return ctx.sexprIdent().IDENT();
                } else {
                    if (valueNode instanceof LispyParser.SexprNumberContext) {
                        return ((LispyParser.SexprNumberContext) valueNode).NUMBER();
                    } else if (valueNode instanceof LispyParser.SexprBooleanContext) {
                        return ((LispyParser.SexprBooleanContext) valueNode).BOOLEAN();
                    } else if (valueNode instanceof TerminalNode) {
                        return valueNode;
                    } else if (valueNode instanceof LispyParser.SexprlistContext) {
                        return valueNode;
                    } else {
                        new EvalError(ctx.sexprIdent());
                    }
                }
            } else {
                new EvalError(ctx.sexprIdent()).printStackTrace();
                System.exit(1);
            }
        } else {
            new EvalError(ctx).printStackTrace();
            System.exit(1);
        }

        return null;
    }

    @Override
    public ParseTree visitSexprNumber(@NotNull final LispyParser.SexprNumberContext ctx) {
        return ctx.NUMBER();
    }

    @Override
    public ParseTree visitSexprIdent(@NotNull final LispyParser.SexprIdentContext ctx) {
        return ctx.IDENT();
    }

    @Override
    public ParseTree visit(@NotNull final ParseTree tree) {
        if (tree instanceof NonTerminalNode) {
            ParseTree ret = visitNonTerminal((NonTerminalNode) tree);
            if (ret != null) {
                return ret;
            } else {
                System.out.println("error");
                System.exit(1);
                return null;
            }
        } else if (tree instanceof TerminalNode) {
            return tree;
        } else {
            return super.visit(tree);
        }
    }

    public ParseTree revokeIdent(TerminalNode node) {
        int type = node.getSymbol().getType();
        if (type == LispyLexer.IDENT) {
            ParseTree ret = curEnv.lookup(node.getText());
            if (ret instanceof TerminalNode) {
                if (((TerminalNode) ret).getSymbol().getType() == LispyLexer.IDENT) {
                    return revokeIdent((TerminalNode) ret);
                } else {
                    return ret;
                }
            } else {
               return node;
            }
        }

        return node;
    }

    public ParseTree visitNonTerminal(@NotNull final NonTerminalNode tree) {
        ParseTree ret = curEnv.lookup(tree.getText());
        if (ret == null) {
            System.out.println("error");
            System.exit(1);
            return null;
        } else if (ret instanceof NonTerminalNode) {
            return visitNonTerminal((NonTerminalNode) ret);
        } else {
            return tree;
        }
    }
}
