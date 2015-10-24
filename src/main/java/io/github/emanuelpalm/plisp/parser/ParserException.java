package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.lexer.Token;

import java.util.Optional;

/**
 * Provides information about some relevant parsing error.
 */
public class ParserException extends RuntimeException {
    private final ParserError error;

    /** Creates new parser exception wrapping given offending token and error. */
    public ParserException(final Optional<Token> t, final ParserError e) {
        super((t.isPresent() ? t.get().origin().toString() + " " : "") + e.description());
        error = e;
    }

    /** Error causing exception to be generated. */
    public ParserError error() {
        return error;
    }
}
