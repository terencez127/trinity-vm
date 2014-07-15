package com.trinityvm;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    static Environment globalEnv = new Environment();

    public static void main(String[] args) {
        try {
            init(globalEnv);
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }

        if (args.length > 0) { // read file
            try {
                java.util.Scanner sc = new java.util.Scanner(new FileReader(args[0]));
                while (sc.hasNextLine()) {
                    eval(sc.nextLine());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {    // repl
            while (true) {
                System.out.print("input# ");
                java.util.Scanner in = new java.util.Scanner(System.in);
                eval(in.nextLine());
            }
        }
        System.exit(0);
    }

    private static void eval(String content) {
        // create a CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(content);

        // create a lexer that feeds off of input CharStream
        SchemeLexer lexer = new SchemeLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        SchemeParser parser = new SchemeParser(tokens);
        parser.setBuildParseTree(true);

        ParseTree tree = parser.prog();
        SimpleSchemeVisitor visitor = new SimpleSchemeVisitor(globalEnv);
        ParseTree ret = visitor.visit(tree);
        if (ret != null) {
            System.out.println(ret.getText());
        }

    }

    private static void addClosure(Environment environment, String name, Closure closure,
            int paramCount) {
        String closureHash = String.valueOf(closure.hashCode());
        SchemeParser.SexprlistContext list = new SchemeParser.SexprlistContext(null, 0);
        for (int i = 0; i < paramCount; i++) {
            String pName = closureHash + String.valueOf(i);
            closure.addParam(pName);
            list.addChild(new CommonToken(SchemeLexer.IDENT, pName));
        }
        environment.addClosure(name, closure);

        environment.bind(name, list);
    }

    private static void init(Environment env) throws EvalError {
        Closure plus = new EvalTree.Plus(env);
        addClosure(env, "+", plus, 2);

        Closure minus = new EvalTree.Minus(env);
        addClosure(env, "-", minus, 2);

        Closure multiply = new EvalTree.Multiply(env);
        addClosure(env, "*", multiply, 2);

        Closure divide = new EvalTree.Divide(env);
        addClosure(env, "/", divide, 2);

        Closure greater = new EvalTree.Greater(env);
        addClosure(env, ">", greater, 2);

        Closure less = new EvalTree.Less(env);
        addClosure(env, "<", less, 2);

        Closure equal = new EvalTree.Equal(env);
        addClosure(env, "equal?", equal, 2);

        Closure greaterEqual = new EvalTree.GreaterEqual(env);
        addClosure(env, ">=", greaterEqual, 2);

        Closure lessEqual = new EvalTree.LessEqual(env);
        addClosure(env, "<=", lessEqual, 2);

        Closure conditional = new EvalTree.Conditional(env);
        addClosure(env, "if", conditional, 3);

        Closure define = new EvalTree.Define(env);
        addClosure(env, "define", define, 2);

        Closure quote = new EvalTree.Quote(env);
        addClosure(env, "quote", quote, 1);

        Closure closureList = new EvalTree.List(env);
        addClosure(env, "list", closureList, 1);

        Closure begin = new EvalTree.Begin(env);
        addClosure(env, "begin", begin, 1);

        Closure lambda = new EvalTree.Lambda(env);
        addClosure(env, "lambda", lambda, 2);

        Closure display = new EvalTree.Display(env);
        addClosure(env, "display", display, 1);

        Closure car = new EvalTree.Car(env);
        addClosure(env, "car", car, 1);

        Closure cdr = new EvalTree.Cdr(env);
        addClosure(env, "cdr", cdr, 1);
    }

}
