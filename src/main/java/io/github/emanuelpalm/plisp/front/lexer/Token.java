package io.github.emanuelpalm.plisp.front.lexer;

/**
 * Identifies a lexeme, its class and its origin.
 */
public class Token {
    /** Default end token. Signals the end of some stream of tokens. */
    public static final Token END = new Token(new TokenOrigin("", 0, 0), TokenClass.END, "");

    private final TokenOrigin origin;
    private final TokenClass type;
    private final String lexeme;

    /**
     * Creates new token with only class and lexeme.
     * <p>
     * This constructor should only by used by tokens not produced from source code.
     */
    public Token(final TokenClass type, final String lexeme) {
        this.origin = TokenOrigin.OTHER;
        this.type = type;
        this.lexeme = lexeme;
    }

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
                && this.type() == ((Token) that).type()
                && this.lexeme().equals(((Token) that).lexeme());
    }
}
