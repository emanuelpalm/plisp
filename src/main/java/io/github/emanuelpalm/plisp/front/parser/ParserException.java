package io.github.emanuelpalm.plisp.front.parser;

/**
 * Provides information about some relevant parsing error.
 */
public class ParserException extends RuntimeException {
    private final ParserError error;

    /** Creates new parser exception wrapping given error. */
    public ParserException(final ParserError e) {
        super(e.description());
        error = e;
    }

    /** Error causing exception to be generated. */
    public ParserError error() {
        return error;
    }
}
