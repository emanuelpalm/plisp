package io.github.emanuelpalm.plisp.runtime;

import io.github.emanuelpalm.plisp.lexer.Token;
import io.github.emanuelpalm.plisp.lexer.TokenClass;
import io.github.emanuelpalm.plisp.lexer.TokenOrigin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Symbolic expression.
 */
public interface SExpr {
    /** Global nul value. */
    Nul NUL = new Nul();

    /** Token representing s-expression origin, if expression originated from explicit code. */
    default Optional<Token> token() {
        return Optional.empty();
    }

    /** Contents of address register, if the current expression is a cons. */
    default SExpr car() {
        return this;
    }

    /** Contents of decrement register, if the current expression is a cons. */
    default SExpr cdr() {
        return NUL;
    }

    /** Creates new cons list with given expression at the end. */
    default SExpr concat(final SExpr s) {
        return s;
    }

    /**
     * Pairs up this expression's elements with that of given, if both are cons lists.
     * <p>
     * Will continue zipping as long as there are element in this cons list. If the given cons list would be shorter
     * {@code Nul} values are used.
     */
    default SExpr zip(final SExpr s) {
        return NUL;
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
     * <p>
     * Behaves as a single element list if used with some list operation, such as zip or concat.
     */
    class Atom implements SExpr {
        private final Token token;

        /** Constructs atom from given token. */
        public Atom(final Token t) {
            token = t;
        }

        /** Constructs atom from given string name. */
        public static SExpr.Atom of(final String name) {
            return new Atom(new Token(TokenClass.ATM, name));
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
        public SExpr concat(final SExpr s) {
            return new Cons(this, s);
        }

        @Override
        public SExpr zip(final SExpr s) {
            return new Cons(
                    new Cons(this, s.car()),
                    NUL
            );
        }

        @Override
        public String toString() {
            return name();
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

        @Override
        public Optional<Token> token() {
            return token;
        }

        @Override
        public SExpr car() {
            return car;
        }

        @Override
        public SExpr cdr() {
            return cdr;
        }

        @Override
        public SExpr concat(final SExpr s) {
            return new Cons(car(), cdr().concat(s));
        }

        @Override
        public SExpr zip(final SExpr s) {
            return new Cons(
                    new Cons(car(), s.car()),
                    cdr().zip(s.cdr())
            );
        }

        @Override
        public String toString() {
            return "(" + car() + " . " + cdr() + ")";
        }

        @Override
        public boolean equals(final Object o) {
            return o != null && o instanceof Cons
                    && ((Cons) o).car().equals(car())
                    && ((Cons) o).cdr().equals(cdr());
        }
    }

    /**
     * Some callable expression.
     */
    interface Callable extends SExpr {
        /** Evaluates some expression using provided arguments and environment. */
        SExpr call(final SExpr args, final SExpr env);
    }
}
