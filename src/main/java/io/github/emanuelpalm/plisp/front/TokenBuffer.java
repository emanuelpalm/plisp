package io.github.emanuelpalm.plisp.front;

import java.util.ArrayList;

/**
 * A buffer of {@link Token}s, allowing them to be consumed non-linearly.
 */
public class TokenBuffer {
    private final ArrayList<Token> tokens;
    private int offset = 0;


    /** Creates new token buffer containing given array of tokens. */
    public TokenBuffer(final ArrayList<Token> ts) {
        tokens = ts;
    }

    /** Creates new token buffer from all tokens returned by given lexer. */
    public static TokenBuffer fromLexer(final Lexer l) {
        final ArrayList<Token> ts = new ArrayList<>(512);
        Token t;
        do {
            ts.add(t = l.next());
        } while (t.type() != TokenClass.END);

        return new TokenBuffer(ts);
    }

    /** Gets current buffer state. The returned value may later be used to restore the buffer to its current state. */
    public int state() {
        return offset;
    }

    /** Restores buffer to given state, effectively changing what tokens will be read subsequently. */
    public void restore(final int state) {
        offset = state;
    }

    /** Reads another token and forwards read pointer. */
    public Token read() {
        return (offset < tokens.size())
                ? tokens.get(offset++)
                : Token.END;
    }

}
