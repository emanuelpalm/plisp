package io.github.emanuelpalm.plisp.lexer;

/**
 * Identifies a lexeme, its class and its origin.
 */
public class Token {
    private final TokenOrigin origin;
    private final TokenClass type;
    private final String lexeme;

    /** Creates new token with given origin identifier, class and lexeme. */
    public Token(final TokenOrigin origin, final TokenClass type, final String lexeme) {
        this.origin = origin;
        this.type = type;
        this.lexeme = lexeme;
    }

    /** Origin. */
    public TokenOrigin origin() {
        return origin;
    }

    /** Class. */
    public TokenClass type() {
        return type;
    }

    /** Lexeme. */
    public String lexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return origin + ": (" + type.name() + ") \"" + lexeme + "\"";
    }

    @Override
    public boolean equals(final Object that) {
        return that != null && that instanceof Token
                && this.origin().equals(((Token) that).origin)
                && this.type() == ((Token) that).type()
                && this.lexeme().equals(((Token) that).lexeme());
    }
}
