package io.github.emanuelpalm.plisp.anaylzer;

import io.github.emanuelpalm.plisp.runtime.SExpr;

/**
 * Carries information about some anomaly discovered by an {@link Analyzer}.
 */
public class AnalyzerException extends Exception {
    /** Creates new s-expression exception, containing given message and offending expression. */
    public AnalyzerException(final String message, final SExpr e) {
        super((e.token().isPresent() ? e.token().get().origin().toString() + " " : "") + message);
    }

    public static class NotDefined extends AnalyzerException {
        /** Creates new s-expression exception, containing given offending expression. */
        public NotDefined(final SExpr e) {
            super("The atom '" + e + "' is not defined.", e);
        }
    }

    public static class PrototypeMismatch extends AnalyzerException {
        /** Creates new s-expression exception, containing given expression and prototype. */
        public PrototypeMismatch(final SExpr e, final Prototype p) {
            super("'" + e + "' doesn't match its prototype " + p, e);
        }
    }

    public static class LambdaMisuse extends AnalyzerException {
        /** Creates new s-expression exception, containing given offending expression. */
        public LambdaMisuse(final SExpr e) {
            super("Lambda '" + e + "' definition and argument mismatch.", e);
        }
    }
}
