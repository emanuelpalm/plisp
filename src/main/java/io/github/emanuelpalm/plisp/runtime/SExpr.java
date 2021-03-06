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
    /** Global nil value. */
    Nil NIL = new Nil();

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
        return NIL;
    }

    /** Creates new cons list with given expression at the end. */
    default SExpr concat(final SExpr s) {
        return s;
    }

    /**
     * Pairs up this expression's elements with that of given, if both are cons lists.
     * <p>
     * Will continue zipping as long as there are element in this cons list. If the given cons list would be shorter
     * {@code Nil} values are used.
     */
    default SExpr zip(final SExpr e) {
        return NIL;
    }

    /** Amount of cons elements, if the current expression is a cons. */
    default int size() {
        return 1;
    }

    /**
     * An empty list.
     */
    class Nil implements SExpr {
        private Nil() {}

        @Override
        public String toString() {
            return "NIL";
        }

        @Override
        public int size() {
            return 0;
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
        public SExpr concat(final SExpr e) {
            return new Cons(this, e);
        }

        @Override
        public SExpr zip(final SExpr e) {
            return new Cons(
                    new Cons(this, e.car()),
                    NIL
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
        public static SExpr of(final SExpr... es) {
            return of(Arrays.asList(es));
        }

        /** Constructs cons list from given list of s-expressions. */
        public static SExpr of(final List<SExpr> es) {
            SExpr e = NIL;
            for (int i = es.size(); i-- != 0; ) {
                e = new Cons(es.get(i), e);
            }
            return e;
        }

        @Override
        public Optional<Token> token() {
            if (token.isPresent()) {
                return token;
            }
            final Optional<Token> carToken = car().token();
            if (carToken.isPresent()) {
                return carToken;
            }
            return cdr().token();
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
        public SExpr concat(final SExpr e) {
            return new Cons(car(), cdr().concat(e));
        }

        @Override
        public SExpr zip(final SExpr e) {
            return new Cons(
                    new Cons(car(), e.car()),
                    cdr().zip(e.cdr())
            );
        }

        @Override
        public int size() {
            return 1 + cdr().size();
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("(").append(car());

            final SExpr cdr = cdr();
            if (cdr instanceof Nil) {
                builder.append(")");

            } else if (cdr instanceof Atom) {
                builder.append(" . ").append(cdr);

            } else {
                builder.append(" ").append(cdr.toString().substring(1));
            }
            return builder.toString();
        }

        @Override
        public boolean equals(final Object o) {
            return o != null && o instanceof Cons
                    && ((Cons) o).car().equals(car())
                    && ((Cons) o).cdr().equals(cdr());
        }
    }
}
