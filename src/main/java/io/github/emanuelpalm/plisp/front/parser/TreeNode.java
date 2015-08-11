package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.Token;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * A node part of an abstract syntax {@link Tree}.
 *
 * @see Void
 * @see Number
 * @see Symbol
 * @see List
 * @see Call
 * @see Meta
 * @see Root
 * @see Function
 * @see Definition
 */
public interface TreeNode {
    /** Default void node. */
    Void VOID = new Void(Token.NIL);

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
        public static TreeNode of(final Token t, final java.util.List<TreeNode> ns) {
            return (ns.size() > 0)
                    ? new List(t, ns)
                    : new Void(t);
        }

        /** Creates node using given token and node list. */
        public static TreeNode of(final Token t, final List l) { return of(t, l.nodes()); }

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
            return "[" + nodes().stream().map(Object::toString).collect(Collectors.joining(" ")) + "]";
        }
    }

    /**
     * An instruction to execute some {@link Function} with provided arguments.
     */
    class Call extends Base {
        private final TreeNode function;
        private final List arguments;

        private Call(final Token t, final TreeNode f, final List args) {
            super(t);
            function = f;
            arguments = args;
        }

        /** Creates call node out of token and provided list of arguments. */
        public static TreeNode of(final Token t, final List args) {
            final java.util.List<TreeNode> ts = args.nodes();
            return (ts.size() > 0)
                    ? new Call(t, ts.get(0), new List(t, ts.subList(1, ts.size())))
                    : new Void(t);
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return ((Function) function().evaluate(t)).evaluate(t, arguments());
        }

        /** Call function node. */
        public TreeNode function() { return function; }

        /** Call arguments. */
        public List arguments() { return arguments; }

        @Override
        public boolean equals(final Object that) {
            return that != null && that instanceof Call
                    && this.function().equals(((Call) that).function())
                    && this.arguments().equals(((Call) that).arguments());
        }

        @Override
        public String toString() { return "(" + function() + " " + arguments() + ")"; }
    }

    /**
     * Associates a value with some origin.
     */
    class Meta extends Base {
        private final TreeNode origin, meta;

        private Meta(final Token t, final TreeNode origin, final TreeNode meta) {
            super(t);
            this.origin = origin;
            this.meta = meta;
        }

        /** Creates meta node from given nodes. */
        public static TreeNode of(final Token t, final TreeNode origin, final TreeNode meta) {
            return (!(meta instanceof Void))
                    ? new Meta(t, origin, meta)
                    : origin;
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) { return origin().evaluate(t); }

        /** Origin node. */
        public TreeNode origin() { return origin; }

        /** Meta value. */
        public TreeNode meta() { return meta; }

        @Override
        public boolean equals(final Object that) {
            return that != null && that instanceof TreeNode.Meta
                    && origin().equals(((Meta) that).origin())
                    && meta().equals(((Meta) that).meta());
        }

        @Override
        public String toString() { return origin() + ":" + meta(); }
    }

    /**
     * Serves as abstract syntax {@link Tree} root.
     */
    class Root extends List {
        private Root(final TreeNode.List l) { super(Token.NIL, l.nodes()); }

        /** Creates new root node containing given nodes. */
        public static TreeNode of(final TreeNode list) {
            return new Root((TreeNode.List) list);
        }

        @Override
        public TreeNode evaluate(final TreeSymbolTable t) {
            return evaluate(t, new List(Token.NIL, Collections.emptyList()));
        }

        /**
         * Evaluates root node with given table and arguments.
         * <p>
         * For evaluation to succeed, all top-level child nodes have to implement {@link Definition}, and one of them
         * must declare the symbol {@code"main"}, which must refer to a {@link Function}.
         */
        public TreeNode evaluate(final TreeSymbolTable t, final List args) {
            for (final TreeNode n : nodes()) {
                ((Definition) n).addSymbolTo(t);
            }
            return ((Function) t.search("main").get())
                    .evaluate(t, (List) List.of(Token.NIL, Collections.singletonList(args)));
        }
    }

    /**
     * Node callable via a {@link Call} node.
     */
    interface Function extends TreeNode {
        /** Function parameters */
        java.util.List<TreeNode> parameters();

        /** Function body. */
        TreeNode body();

        /** Evaluates callable node with given table and arguments. */
        default TreeNode evaluate(final TreeSymbolTable t0, final TreeNode.List args) {
            final java.util.List<TreeNode> ps = parameters();
            final java.util.List<TreeNode> as = args.nodes();
            TreeSymbolTable t1 = t0;
            for (int i = ps.size(); i-- != 0;) {
                t1 = t1.insertLocal(((Symbol) ps.get(i).evaluate(t1)).name(), as.get(i));
            }
            return body().evaluate(t1);
        }
    }

    /**
     * Node useful for adding symbol to some symbol table.
     */
    interface Definition extends TreeNode {
        /** Adds symbol to given symbol table. */
        void addSymbolTo(final TreeSymbolTable t);
    }
}
