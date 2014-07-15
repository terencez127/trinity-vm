package com.trinityvm;

import org.antlr.v4.runtime.RuleContext;

public class EvalError extends Exception {
    RuleContext node;

    public EvalError(RuleContext node) {
        this.node = node;
    }

    public String toString() {
        return "Eval Error at subtree: " + node.toString();
    }
}
