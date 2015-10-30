package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.lexer.TokenBuffer;
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
    Optional<SExpr> apply(final TokenBuffer b);

    /** Transforms rule into new rule using given transformer function. */
    default Rule transform(final Transformer t) {
        return (buffer) -> {
            final Optional<SExpr> s = apply(buffer);
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
        return (buffer) -> {
            final int state = buffer.state();
            final Token next = buffer.next();
            if (next.type().equals(c)) {
                return Optional.of(new SExpr.Atom(next));
            }
            buffer.restore(state);
            return Optional.empty();
        };
    }

    /** Requires any one of the given rules to succeed. */
    static Rule anyOf(final Rule... rs) {
        return (buffer) -> {
            final int state = buffer.state();
            for (final Rule r : rs) {
                final Optional<SExpr> s = r.apply(buffer);
                if (s.isPresent()) {
                    return Optional.of(s.get());
                }
            }
            buffer.restore(state);
            return Optional.empty();
        };
    }

    /** Requires all the the given rules to succeed. */
    static Rule allOf(final Rule... rs) {
        return (buffer) -> {
            final int state = buffer.state();
            final ArrayList<SExpr> ss = new ArrayList<>(rs.length);
            for (final Rule r : rs) {
                final Optional<SExpr> s = r.apply(buffer);
                if (s.isPresent()) {
                    ss.add(s.get());

                } else {
                    buffer.restore(state);
                    return Optional.empty();
                }
            }
            return Optional.of(SExpr.Cons.of(ss));
        };
    }

    /** Wraps all possible successful applications of the provided rule. Always succeeds. */
    static Rule manyOf(final Rule r) {
        return (buffer) -> {
            final ArrayList<SExpr> ss = new ArrayList<>();
            while (true) {
                final Optional<SExpr> s = r.apply(buffer);
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
