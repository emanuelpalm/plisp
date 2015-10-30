package io.github.emanuelpalm.plisp.runtime;

/**
 * Thrown to indicate that some exceptional condition involving some {@link SExpr} has occurred.
 */
public class SExprException extends RuntimeException {
    /** Creates new s-expression exception, containing given message and offending expression. */
    public SExprException(final String message, final SExpr expr) {
        super((expr.token().isPresent() ? expr.token().get().origin().toString() + " " : "") + message);
    }

    /**
     * An attempt to evaluate some atom into an entry in the environment was made, but didn't succeed.
     */
    public static class AtomNotFound extends SExprException {
        /** Creates new s-expression exception, containing given message and offending expression. */
        public AtomNotFound(final SExpr expr) {
            super("Atom '" + expr + "' not in environment.", expr);
        }
    }

    /**
     * No successful condition was found when evaluating a cond expression.
     */
    public static class CondExhausted extends SExprException {
        /** Creates new s-expression exception, containing given message and offending expression. */
        public CondExhausted(final SExpr expr) {
            super("No successful condition.", expr);
        }
    }
}
