package com.trinityvm;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class NonTerminalNode implements TerminalNode {
    public org.antlr.v4.runtime.Token symbol;

    public NonTerminalNode(org.antlr.v4.runtime.Token s) {
        symbol = s;
    }

    @Override
    public org.antlr.v4.runtime.Token getSymbol() {
        return symbol;
    }

    @Override
    public ParseTree getParent() {
        return null;
    }

    @Override
    public Object getPayload() {
        return null;
    }

    @Override
    public ParseTree getChild(final int i) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String toStringTree() {
        return null;
    }

    @Override
    public <T> T accept(final ParseTreeVisitor<? extends T> parseTreeVisitor) {
        return null;
    }

    @Override
    public String getText() {
        return symbol.getText();
    }

    @Override
    public String toStringTree(final Parser parser) {
        return null;
    }

    @Override
    public Interval getSourceInterval() {
        return null;
    }
}
