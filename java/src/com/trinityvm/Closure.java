package com.trinityvm;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public abstract class Closure {
    // Environment of the closure;
    public final Environment paramEnv;

    // List of parameters
    private List<String> params = new ArrayList<String>();

    // body of the closure
    public ParseTree body = null;

    public abstract ParseTree eval(final SimpleSchemeVisitor visitor, final Environment outEnv,
            final SchemeParser.SexprlistContext paramList) throws EvalError;

    public Closure(Environment environment) {
        paramEnv = new Environment();
        paramEnv.setParent(environment);
    }

    private Closure() {
        paramEnv = null;}

    public List<String> getParams() {
        return params;
    }

    public void addParam(String param) {
        params.add(param);
    }

}
