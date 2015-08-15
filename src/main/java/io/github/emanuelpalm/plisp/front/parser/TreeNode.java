package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.Token;

import java.util.stream.Collectors;

/**
 * A node part of an abstract syntax tree.
 *
 * @see Void
 * @see Number
 * @see Symbol
 * @see List
 */
public interface TreeNode {
    /** Token. */
    Token token();

    /** Evaluates node using given symbol table. */
    default TreeNode evaluate(final TreeSymbolTable t) { return this; }

    /**
     * Optional base class for tree node types.
     */
    abstract class Base implements TreeNode {
        private final Token token;

        protected Base(final Token t) { token = t; }

        @Override
        public Token token() { return token; }

        @Override
        public boolean equals(final Object that) {
            return that != null && that instanceof TreeNode
                    && this.token().equals(((TreeNode) that).token());
        }

        @Override
        public String toString() { return token().lexeme(); }
    }

    /**
     * Nothing.
     */
    class Void extends Base {
        private Void(final Token t) { super(t); }

        /** Creates void node. */
        public static TreeNode.Void of(final Token t) { return new Void(t); }

        @Override
        public boolean equals(final Object that) {
            return that == null || that instanceof Void;
        }
    }

    /**
     * A number.
     */
    class Number extends Base {
        private Number(final Token t) { super(t); }

        /** Creates number node. */
        public static TreeNode.Number of(final Token t) { return new Number(t); }

        /** Converts number node into a Java {@code double}. */
        public double toDouble() { return Double.parseDouble(token().lexeme()); }
    }

    /**
     * A symbol.
     */
    class Symbol extends Base {
        private Symbol(final Token t) { super(t); }

        /** Creates symbol node. */
        public static TreeNode.Symbol of(final Token t) { return new Symbol(t); }

        /**
         * Evaluates symbol into the {@link TreeNode} it represents.
         * <p>
         * For evaluation to succeed, the symbol name must be present in the given symbol table.
         */
        @Override
        public TreeNode evaluate(final TreeSymbolTable t) { return t.search(name()).get().evaluate(t); }

        /** Acquires symbol name. */
        public String name() { return token().lexeme(); }
    }

    /**
     * A list of arbitrary {@link TreeNode}s.
     */
    class List extends Base {
        private final java.util.List<TreeNode> nodes;

        protected List(final Token t, final java.util.List<TreeNode> ns) {
            super(t);
            nodes = ns;
        }

        /** Creates node using given token and node list. */
        public static TreeNode of(final Token t, final List l) {
            return (l.nodes().size() > 0)
                    ? new List(t, l.nodes())
                    : new Void(t);
        }

        /** Creates anonymous list node containing given nodes. */
        public static TreeNode.List of(final java.util.List<TreeNode> ns) { return new List(Token.NIL, ns); }

        /** Acquires list elements. */
        public java.util.List<TreeNode> nodes() { return nodes; }

        @Override
        public boolean equals(final Object that) {
            return that != null && that instanceof List
                    && nodes().equals(((List) that).nodes());
        }

        @Override
        public String toString() {
            return "(" + nodes().stream().map(Object::toString).collect(Collectors.joining(" ")) + ")";
        }
    }
}
