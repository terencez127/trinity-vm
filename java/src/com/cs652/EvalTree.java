package com.cs652;


import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public class EvalTree {

    private static class Calculate extends Closure {
        public enum mathType {
            PLUS,
            MINUS,
            MULTIPLY,
            DIVIDE
        }

        protected mathType type;

        public Calculate(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            ParseTree n1 = paramList.sexpr().get(1);
            ParseTree n2 = paramList.sexpr().get(2);
            if (n1 != null && n2 != null) {

                Environment preEnv = visitor.swapCurEnv(outEnv);
                n1 = visitor.visit(n1);
                n2 = visitor.visit(n2);
                visitor.swapCurEnv(preEnv);
                int type1 = ((TerminalNode)n1).getSymbol().getType();
                int type2 = ((TerminalNode)n2).getSymbol().getType();
                if (type1 == LispyLexer.NUMBER && type2 == LispyLexer.NUMBER) {
                    TerminalNode ret;
                    Integer val = 0;
                    switch (type) {
                        case PLUS:
                            val = Integer.parseInt(n1.getText()) + Integer.parseInt(n2.getText());
                            ret = new TerminalNodeImpl(new CommonToken(LispyLexer.NUMBER, val.toString()));
                            break;
                        case MINUS:
                            val = Integer.parseInt(n1.getText()) - Integer.parseInt(n2.getText());
                            ret = new TerminalNodeImpl(new CommonToken(LispyLexer.NUMBER, val.toString()));
                            break;
                        case MULTIPLY:
                            val = Integer.parseInt(n1.getText()) * Integer.parseInt(n2.getText());
                            ret = new TerminalNodeImpl(new CommonToken(LispyLexer.NUMBER, val.toString()));
                            break;
                        case DIVIDE:
                            val = Integer.parseInt(n1.getText()) / Integer.parseInt(n2.getText());
                            ret = new TerminalNodeImpl(new CommonToken(LispyLexer.NUMBER, val.toString()));
                            break;
                        default:
                            throw new EvalError(paramList);
                    }
                    return ret;
                }
            }
            throw new EvalError(paramList);
        }
    }

    public static class Plus extends Calculate {
        public Plus(final Environment environment) {
            super(environment);
            type = mathType.PLUS;
        }
    }

    public static class Minus extends Calculate {
        public Minus(final Environment environment) {
            super(environment);
            type = mathType.MINUS;
        }
    }

    public static class Multiply extends Calculate {
        public Multiply(final Environment environment) {
            super(environment);
            type = mathType.MULTIPLY;
        }
    }

    public static class Divide extends Calculate {
        public Divide(final Environment environment) {
            super(environment);
            type = mathType.DIVIDE;
        }
    }


    private static class Compare extends Closure {
        public enum Operator {
            GREATER_THAN,
            EQUALS,
            LESS_THAN,
            GREATER_EQUAL,
            LESS_EQUAL
        }

        protected Operator type;

        public Compare(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            Environment tmpEnv = new Environment();
            tmpEnv.setParent(paramEnv.getParent());
            ParseTree n1 = paramList.sexpr().get(1);
            ParseTree n2 = paramList.sexpr().get(2);

            if (n1 != null && n2 != null) {
                Environment preEnv = visitor.swapCurEnv(outEnv);
                n1 = visitor.visit(n1);
                tmpEnv.bind(getParams().get(0), n1);

                n2 = visitor.visit(n2);
                tmpEnv.bind(getParams().get(1), n2);
                visitor.swapCurEnv(preEnv);

                int type1 = ((TerminalNode)n1).getSymbol().getType();
                int type2 = ((TerminalNode)n2).getSymbol().getType();
                if (type1 == LispyLexer.NUMBER && type2 == LispyLexer.NUMBER) {
                        TerminalNode ret;
                        Boolean tmp;
                        switch (type) {
                            case GREATER_THAN:
                                tmp = Integer.parseInt(n1.getText()) > Integer.parseInt(n2
                                        .getText());
                                break;
                            case LESS_THAN:
                                tmp = Integer.parseInt(n1.getText()) < Integer.parseInt(n2
                                        .getText());
                                break;
                            case EQUALS:
                                tmp = Integer.parseInt(n1.getText()) == Integer.parseInt(n2
                                        .getText());
                                break;
                            case GREATER_EQUAL:
                                tmp = Integer.parseInt(n1.getText()) >= Integer.parseInt(n2
                                        .getText());
                                break;
                            case LESS_EQUAL:
                                tmp = Integer.parseInt(n1.getText()) <= Integer.parseInt(n2
                                        .getText());
                                break;
                            default:
                                throw new EvalError(paramList);
                        }
                        ret = getBooleanNode(tmp);
                        return ret;
                }
            }
            throw new EvalError(paramList);
        }

        private TerminalNode getBooleanNode(boolean bValue) {
            String node;
            if (bValue) {
                node = LispyLexer.tokenNames[LispyLexer.TRUE];
            } else {
                node = LispyLexer.tokenNames[LispyLexer.FALSE];
            }

            return new TerminalNodeImpl(new CommonToken(LispyLexer.BOOLEAN, node));
        }
    }

    public static class Greater extends Compare {
        public Greater(final Environment environment) {
            super(environment);
            type = Operator.GREATER_THAN;
        }
    }

    public static class Less extends Compare {
        public Less(final Environment environment) {
            super(environment);
            type = Operator.LESS_THAN;
        }
    }

    public static class Equal extends Compare {
        public Equal(final Environment environment) {
            super(environment);
            type = Operator.EQUALS;
        }
    }

    public static class GreaterEqual extends Compare {
        public GreaterEqual(final Environment environment) {
            super(environment);
            type = Operator.GREATER_EQUAL;
        }
    }

    public static class LessEqual extends Compare {
        public LessEqual(final Environment environment) {
            super(environment);
            type = Operator.LESS_EQUAL;
        }
    }

    public static class Quote extends Closure {
        public Quote(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            return paramList.sexpr().get(1);
        }
    }

    public static class Define extends Closure {
        public Define(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            LispyParser.SexprContext n1 = paramList.sexpr().get(1);
            LispyParser.SexprContext n2 = paramList.sexpr().get(2);


            if (n1.sexprIdent() != null) {
                Environment preEnv = visitor.swapCurEnv(outEnv);
                ParseTree value = visitor.visit(n2);
                if (value instanceof TerminalNode) {
                    value = visitor.visit(value);
                }

                visitor.swapCurEnv(preEnv);
                paramEnv.getParent().bind(n1.getText(), value);

                return null;
            }
            throw new EvalError(paramList);
        }
    }

    public static class Conditional extends Closure {
        public Conditional(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            ParseTree test = paramList.sexpr().get(1);
            ParseTree conseq = paramList.sexpr().get(2);

            ParseTree alt = null;
            if (getParams().size() > 2) {
                alt = paramList.sexpr().get(3);
            }

            if (test != null && conseq != null) {
                Environment preEnv = visitor.swapCurEnv(outEnv);
                test = visitor.visit(test);
                int typeTest = ((TerminalNode)test).getSymbol().getType();
                if (typeTest == LispyLexer.BOOLEAN) {
                    ParseTree ret = null;
                    if (((TerminalNode) test).getSymbol().getType() == LispyLexer.TRUE) {
                        ret = visitor.visit(conseq);
                    } else if (alt != null) {
                        ret = visitor.visit(alt);
                    }
                    visitor.swapCurEnv(preEnv);
                    return ret;
                }
            }

            throw new EvalError(paramList);
        }
    }

    public static class Lambda extends Closure {
        public Lambda(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            ParseTree n1 = paramList.sexpr().get(1);
            ParseTree n2 = paramList.sexpr().get(2);

            if (n1.getChild(0) instanceof LispyParser.SexprlistContext
                    || ((TerminalNode)n1).getSymbol().getType() == LispyLexer.IDENT) {
                Environment closureEnv = new Environment();
                closureEnv.setParent(outEnv);
                Closure closure = new Closure(closureEnv) {
                    @Override
                    public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                            final LispyParser.SexprlistContext paramList) throws EvalError {
                        Environment tmpEnv = new Environment();
                        tmpEnv.setParent(paramEnv.getParent());
                        int i = 1;
                        java.util.List<String> params = getParams();
                        Environment preEnv = visitor.swapCurEnv(outEnv);
                        for (String param : params) {
                            tmpEnv.bind(param, visitor.visit(paramList.sexpr().get(i)));
                            i++;
                        }
                        visitor.swapCurEnv(tmpEnv);
                        ParseTree ret = visitor.visit(body);
                        visitor.swapCurEnv(preEnv);
                        return ret;
                    }
                };

                if (n2.getChild(0) instanceof LispyParser.SexprlistContext) {
                    closure.body = n2;
                    if (n1.getChild(0) instanceof LispyParser.SexprlistContext) {

                        for (LispyParser.SexprContext sexpr : ((LispyParser.SexprlistContext) n1.getChild(0)).sexpr()) {
                            String name = sexpr.getText();
                            closure.addParam(name);
                        }
                    } else {
                        closure.addParam(n1.getText());
                    }

                    // Use hash value of the class as closure name.
                    String name = String.valueOf(closure.hashCode());
                    outEnv.bind(name, n2);
                    outEnv.addClosure(name, closure);

                    return new NonTerminalNode(new CommonToken(LispyLexer.IDENT, name));
                }
            }
            throw new EvalError(paramList);
        }
    }

    public static class Display extends Closure {
        public Display(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            System.out.println(paramList.sexpr().get(1).getText());
            return null;
        }
    }

    public static class List extends Closure {
        public List(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            LispyParser.SexprlistContext list = new LispyParser.SexprlistContext(null,
                    -1);
            for (int i = 0; i < paramList.getChildCount(); i++) {
                if (i != 1) {
                    ParseTree child = paramList.getChild(i);
                    if (child instanceof TerminalNode) {
                        list.addChild((TerminalNode) child);
                    } else {
                        list.addChild((org.antlr.v4.runtime.RuleContext) child);
                    }
                }
            }

            return list;
        }
    }

    public static class Begin extends Closure {
        public Begin(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            int size = paramList.sexpr().size();
            if (size > 0) {

                Environment preEnv = visitor.swapCurEnv(outEnv);
                for (int i = 1; i < size - 1; i++) {
                    visitor.visit(paramList.sexpr().get(i));
                }

                ParseTree ret;
                ret = visitor.visit(paramList.sexpr().get(size - 1));

                visitor.swapCurEnv(preEnv);
                return ret;
            }
            throw new EvalError(paramList);
        }
    }

    public static class Car extends Closure {
        public Car(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            LispyParser.SexprContext n1 = paramList.sexpr().get(1);
            ParseTree list = null;
            if (n1.sexprIdent() != null) {
                list = outEnv.lookup(n1.getText());
                if (!(list instanceof LispyParser.SexprlistContext)) {
                    throw new EvalError(paramList);
                }
            } else if (n1.sexprlist() != null) {
                list = n1.sexprlist();
                if (list.getParent() != null) {
                    list = visitor.visit(list);
                    if (!(list instanceof LispyParser.SexprlistContext)) {
                        throw new EvalError(paramList);
                    }
                }
            }

            if (list != null) {
                LispyParser.SexprContext context = ((LispyParser.SexprlistContext) list).sexpr()
                        .get(0);
                if (context.sexprBoolean() != null) {
                    return context.sexprBoolean();
                } else if (context.sexprNumber() != null) {
                    return context.sexprNumber();
                } else if (context.sexprIdent() != null) {
                    return context.sexprIdent();
                } else {
                    return context.sexprlist();
                }
            }

            throw new EvalError(paramList);
        }
    }

    public static class Cdr extends Closure {
        public Cdr(final Environment environment) {
            super(environment);
        }

        @Override
        public ParseTree eval(final SimpleLispyVisitor visitor, final Environment outEnv,
                final LispyParser.SexprlistContext paramList) throws EvalError {
            LispyParser.SexprContext n1 = paramList.sexpr().get(1);
            ParseTree list = null;
            if (n1.sexprIdent() != null) {
                list = outEnv.lookup(n1.getText());
                if (!(list instanceof LispyParser.SexprlistContext)) {
                    throw new EvalError(paramList);
                }
            } else if (n1.sexprlist() != null) {
                list = n1.sexprlist();
                if (list.getParent() != null) {
                    list = visitor.visit(list);
                    if (!(list instanceof LispyParser.SexprlistContext)) {
                        throw new EvalError(paramList);
                    }
                }
            }

            if (list != null) {
                LispyParser.SexprlistContext l = new LispyParser.SexprlistContext(null, -1);
                int count = list.getChildCount();
                for (int i = 0; i < count; i++) {
                    if (i != 1) {
                        ParseTree child = list.getChild(i);
                        if (child instanceof TerminalNode) {
                            l.addChild((TerminalNode) child);
                        } else {
                            l.addChild((org.antlr.v4.runtime.RuleContext) child);
                        }
                    }
                }

                return l;
            }

            throw new EvalError(paramList);
        }
    }
}
