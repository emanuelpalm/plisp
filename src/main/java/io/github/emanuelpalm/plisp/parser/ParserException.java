package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.lexer.Token;

import java.util.Optional;

/**
 * Provides information about some relevant parsing error.
 */
public class ParserException extends RuntimeException {
    /** Creates new parser exception wrapping given offending token and error. */
    public ParserException(final String message, final Optional<Token> t) {
        super((t.isPresent() ? t.get().origin().toString() + " " : "") + message);
    }

    /**
     * An opening parenthesis without a matching closing parenthesis was encountered.
     */
    public static class UnbalancedOpeningParenthesis extends ParserException {
        /** Creates new parser exception wrapping given offending token and error. */
        public UnbalancedOpeningParenthesis(final Optional<Token> t) {
            super("Unbalanced opening parenthesis.", t);
        }
    }

    /**
     * A closing parenthesis without a matching opening parenthesis was encountered.
     */
    public static class UnbalancedClosingParenthesis extends ParserException {
        /** Creates new parser exception wrapping given offending token and error. */
        public UnbalancedClosingParenthesis(final Optional<Token> t) {
            super("Unbalanced closing parenthesis.", t);
        }
    }

    /**
     * A quote without an associated expression was encountered.
     */
    public static class DanglingQuote extends ParserException {
        /** Creates new parser exception wrapping given offending token and error. */
        public DanglingQuote(final Optional<Token> t) {
            super("Dangling quote.", t);
        }
    }

    /**
     * An atom outside any relevant expression was encountered.
     */
    public static class DanglingAtom extends ParserException {
        /** Creates new parser exception wrapping given offending token and error. */
        public DanglingAtom(final Optional<Token> t) {
            super("Dangling atom.", t);
        }
    }
}
