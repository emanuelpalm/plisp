package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.lexer.Token;
import io.github.emanuelpalm.plisp.lexer.TokenClass;
import io.github.emanuelpalm.plisp.runtime.SExpr;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Parser rule.
 */
public interface Rule {
    /** Applies rule, returning {@link SExpr} only if successful. */
    Optional<SExpr> apply(final BufferedLexer l);

    /** Transforms rule into new rule using given transformer function. */
    default Rule transform(final Transformer t) {
        return (l) -> {
            final Optional<SExpr> s = apply(l);
            if (s.isPresent()) {
                return Optional.of(t.onTransform(s.get()));
            }
            return Optional.empty();
        };
    }

    /**
     * Applies transformation to s-expression.
     */
    interface Transformer {
        SExpr onTransform(final SExpr s);
    }

    /** Requires one {@link Token} of given class. */
    static Rule oneOf(final TokenClass c) {
        return (l) -> {
            final int state = l.state();
            final Token next = l.next();
            if (next.type().equals(c)) {
                return Optional.of(new SExpr.Atom(next));
            }
            l.restore(state);
            return Optional.empty();
        };
    }

    /** Requires any one of the given rules to succeed. */
    static Rule anyOf(final Rule... rs) {
        return (l) -> {
            final int state = l.state();
            for (final Rule r : rs) {
                final Optional<SExpr> s = r.apply(l);
                if (s.isPresent()) {
                    return Optional.of(s.get());
                }
            }
            l.restore(state);
            return Optional.empty();
        };
    }

    /** Requires all the the given rules to succeed. */
    static Rule allOf(final Rule... rs) {
        return (l) -> {
            final int state = l.state();
            final ArrayList<SExpr> ss = new ArrayList<>(rs.length);
            for (final Rule r : rs) {
                final Optional<SExpr> s = r.apply(l);
                if (s.isPresent()) {
                    ss.add(s.get());

                } else {
                    l.restore(state);
                    return Optional.empty();
                }
            }
            return Optional.of(SExpr.Cons.of(ss));
        };
    }

    /** Wraps all possible successful applications of the provided rule. Always succeeds. */
    static Rule manyOf(final Rule r) {
        return (l) -> {
            final ArrayList<SExpr> ss = new ArrayList<>();
            while (true) {
                final Optional<SExpr> s = r.apply(l);
                if (s.isPresent()) {
                    ss.add(s.get());

                } else {
                    break;
                }
            }
            return Optional.of(SExpr.Cons.of(ss));
        };
    }
}
