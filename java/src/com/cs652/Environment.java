package com.cs652;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    HashMap<String, ParseTree> env;

    // Closure name and list of parameters.
    Map<String, Closure> closures;

    Environment parent = null;

    public Environment() {
        env = new HashMap<>();
        closures = new HashMap<>();
    }

    public void bind(String name, ParseTree node) {
        env.put(name, node);
    }

    public ParseTree lookup(String name) {
        ParseTree node = env.get(name);
        if (node == null && parent != null) {
            node = parent.lookup(name);
        }

        return node;
    }

    public void setParent(Environment p) {
        parent = p;
    }

    public Environment getParent() {
        return parent;
    }

    public void addClosure(String name, Closure closure) {
        closures.put(name, closure);
    }

    public Closure getClosure(String name) {
        Closure closure = closures.get(name);
        if (closure == null && parent != null) {
            closure = parent.getClosure(name);
        }

        return closure;
    }

}
