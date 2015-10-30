package io.github.emanuelpalm.plisp.runtime;

/**
 * Thrown to indicate that some exceptional condition involving some {@link SExpr} has occurred.
 */
public class SExprException extends RuntimeException {
    private final SExpr expr;

    /** Creates new s-expression exception, containing given message and offending expression. */
    public SExprException(final String message, final SExpr expr) {
        super((expr.token().isPresent() ? expr.token().get().origin().toString() + " " : "") + message);
        this.expr = expr;
    }

    /** The offending expression. */
    public SExpr expr() {
        return expr;
    }
}
