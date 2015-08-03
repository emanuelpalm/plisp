package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.Token;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;
import io.github.emanuelpalm.plisp.front.lexer.TokenOrigin;

/**
 * A node part of an abstract syntax {@link Tree}.
 * <p>
 * The inner classes of {@link TreeNode} constitute the base constructs of the language. They do not, however, provide
 * enough functionality for the language to be actually useful. It is the task of a compiler back-end to provide
 * additional node types for standard functions such as {@code "let"} or {@code "+"} to the symbol table used when
 * evaluating a program.
 */
public abstract class TreeNode {
    private final Token token;

    /** Creates new AST node holding given token. */
    protected TreeNode(final Token t) {
        token = t;
    }

    /** Node token. */
    public Token token() {
        return token;
    }

    /** Evaluates node. */
    public abstract TreeNode evaluate(final TreeSymbolTable t);

    @Override
    public boolean equals(final Object that) {
        return that != null
                && that instanceof TreeNode
                && that.getClass().isAssignableFrom(this.getClass())
                && this.token().equals(((TreeNode) that).token());
    }

    @Override
    public String toString() {
        return "{token: " + token() + " }";
    }

    /**
     * Void node.
     */
    public static class Void extends TreeNode {
        /** Creates void node. */
        protected Void(final Token t) {
            super(t);
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return this;
        }
    }

    /**
     * Integer node.
     */
    public static class Integer extends TreeNode {
        /** Creates integer node. */
        public Integer(final Token t) {
            super(t);
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return this;
        }

        /** Converts integer node into a Java {@code long}. */
        public Long asLong() {
            return Long.parseLong(token().lexeme());
        }
    }

    /**
     * Number node.
     */
    public static class Number extends TreeNode {
        /** Creates number node. */
        public Number(final Token t) {
            super(t);
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return this;
        }

        /** Converts number node into a Java {@code double}. */
        public Double asDouble() {
            return Double.parseDouble(token().lexeme());
        }
    }

    /**
     * Symbol node.
     */
    public static class Symbol extends TreeNode {
        /** Creates symbol node. */
        public Symbol(final Token t) {
            super(t);
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return t.find(name())
                    .orElseThrow(() -> new RuntimeException(token() + " isn't defined."));
        }

        /** Acquires symbol name. */
        public String name() {
            return token().lexeme();
        }
    }

    /**
     * List node.
     */
    public static class List extends TreeNode {
        private final TreeNode[] children;

        /** Creates list node from given list start token and given child nodes. */
        public List(final Token t, final TreeNode... children) {
            super(t);
            this.children = children;
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return this;
        }

        /** List node children. */
        public TreeNode[] children() {
            return children;
        }
    }

    /**
     * Call node.
     * <p>
     * An instruction to execute some callable with some provided arguments.
     */
    public static class Call extends TreeNode {
        private final Callable callable;
        private final TreeNode[] arguments;

        /** Creates call node out of token, callable and provided arguments. */
        public Call(final Token t, final Callable c, final TreeNode... args) {
            super(t);
            callable = c;
            arguments = args;
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return callable.evaluate(t, arguments);
        }
    }

    /**
     * A callable node.
     */
    public interface Callable {
        /** Evaluates callable node with given table and arguments. */
        TreeNode evaluate(final TreeSymbolTable t, final TreeNode... args);
    }

    /**
     * Meta node.
     * <p>
     * A meta node can be thought of as an appendage to some origin node. Evaluating the meta node simply causes the
     * origin node to be evaluated with no modification. The meta node carries, however, a meta value, of arbitrary node
     * type, which may be used to associate data with the origin node.
     */
    public static class Meta extends TreeNode {
        private final TreeNode origin, meta;

        /** Creates meta node. */
        public Meta(final Token t, final TreeNode origin, final TreeNode meta) {
            super(t);
            this.origin = origin;
            this.meta = meta;
        }

        /** Meta value. */
        public TreeNode meta() {
            return meta;
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return origin.evaluate(t);
        }
    }

    /**
     * Root node.
     * <p>
     * Evaluates all global definitions and then calls the program main function.
     */
    public static class Root extends TreeNode {
        private final Definition[] children;
        private final Number[] arguments;

        /** Creates root node containing given children and program arguments. */
        protected Root(final Definition[] children, final Number[] args) {
            super(new Token(new TokenOrigin("<root>", 0, 0), TokenClass.ERR, "<root>"));
            this.children = children;
            this.arguments = args;
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t0) {
            TreeSymbolTable t1 = t0;
            for (final Definition c : children) {
                t1 = c.addTo(t1);
            }
            final TreeNode m = t1.find("main")
                    .orElseThrow(() -> new RuntimeException("No 'main' function declared."))
                    .evaluate(t1);

            if (!(m instanceof Callable)) throw new RuntimeException("Declared 'main' not callable.");

            return ((Callable) m).evaluate(t1, arguments);
        }
    }

    /**
     * Definition node.
     * <p>
     * Adds additional symbols to some given symbol table.
     */
    public interface Definition {
        TreeSymbolTable addTo(final TreeSymbolTable t);
    }
}
