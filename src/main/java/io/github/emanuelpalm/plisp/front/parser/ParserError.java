package io.github.emanuelpalm.plisp.front.parser;

/**
 * A kind of parser error.
 */
public enum ParserError {
    UNBALANCED_PAL("Unbalanced opening parenthesis."),
    UNBALANCED_PAR("Unbalanced closing parenthesis."),
    DANGLING_QUOTE("Dangling quote."),
    DANGLING_ATOM("Dangling atom.");

    private final String description;

    ParserError(final String s) {
        description = s;
    }

    /** Provides error description. */
    public String description() {
        return description;
    }
}
