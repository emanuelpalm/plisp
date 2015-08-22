package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.Token;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;
import io.github.emanuelpalm.plisp.front.lexer.TokenOrigin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Symbolic expression.
 */
public interface SExpr {
    /** Global nul value. */
    Nul NUL = new Nul();

    /** Token representing s-expression origin. Only present if generated from code. */
    default Optional<Token> token() {
        return Optional.empty();
    }

    /**
     * Nothing.
     */
    class Nul implements SExpr {
        private Nul() {}

        @Override
        public String toString() {
            return "NUL";
        }
    }

    /**
     * A unit of least significance.
     */
    class Atom implements SExpr {
        private final Token token;

        /** Constructs atom from given token. */
        public Atom(final Token t) {
            token = t;
        }

        /** Constructs atom from given string name. */
        public Atom(final String name) {
            token = new Token(TokenClass.ATM, name);
        }

        /** Atom name. */
        public String name() {
            return token.lexeme();
        }

        @Override
        public Optional<Token> token() {
            return Optional.ofNullable(token.origin() != TokenOrigin.OTHER
                ? token
                : null);
        }

        @Override
        public String toString() {
            return token.lexeme();
        }

        @Override
        public boolean equals(final Object o) {
            return o != null && o instanceof Atom
                    && ((Atom) o).name().equals(name());
        }
    }

    /**
     * Memory cell with two registers for holding s-expressions.
     */
    class Cons implements SExpr {
        private final Optional<Token> token;
        private final SExpr car, cdr;

        /** Constructs cell from given two s-expressions. */
        public Cons(final SExpr car, final SExpr cdr) {
            this.token = car.token();
            this.car = car;
            this.cdr = cdr;
        }

        /** Constructs cons list from given array of s-expressions. */
        public static SExpr of(final SExpr... ss) {
            return of(Arrays.asList(ss));
        }

        /** Constructs cons list from given list of s-expressions. */
        public static SExpr of(final List<SExpr> ss) {
            SExpr c = NUL;
            for (int i = ss.size(); i-- != 0; ) {
                c = new Cons(ss.get(i), c);
            }
            return c;
        }

        /** Contents of address register. */
        public SExpr car() {
            return car;
        }

        /** Contents of decrement register. */
        public SExpr cdr() {
            return cdr;
        }

        /** Contents of address register's address register. */
        public SExpr caar() {
            return ((Cons) car()).car();
        }

        /** Contents of address register's decrement register. */
        public SExpr cadr() {
            return ((Cons) car()).cdr();
        }

        /** Contents of decrement register's decrement register. */
        public SExpr cddr() {
            return ((Cons) cdr()).cdr();
        }

        /** Contents of decrement register's address register. */
        public SExpr cdar() {
            return ((Cons) cdr()).car();
        }

        @Override
        public Optional<Token> token() {
            return token;
        }

        @Override
        public String toString() {
            return "(" + car + " . " + cdr + ")";
        }

        @Override
        public boolean equals(final Object o) {
            return o != null && o instanceof Cons
                    && ((Cons) o).car().equals(car())
                    && ((Cons) o).cdr().equals(cdr());
        }
    }
}
