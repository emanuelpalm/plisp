package io.github.emanuelpalm.plisp.front;

import java.util.ArrayList;

/**
 * Buffers lexer tokens, allowing them to be consumed non-linearly.
 */
public class BufferedLexer {
    private final ArrayList<Token> tokens;
    private int offset = 0;

    /** Creates new buffered lexer. */
    public BufferedLexer(final Lexer l) {
        tokens = new ArrayList<>(512);
        Token t;
        do {
            tokens.add(t = l.next());
        } while (t.type() != TokenClass.END);
    }

    BufferedLexer(final ArrayList<Token> ts) {
        tokens = ts;
    }

    /** Gets current buffer state. The returned value may later be used to restore the buffer to its current state. */
    public int state() {
        return offset;
    }

    /** Restores buffer to given state, effectively changing what tokens will be read subsequently. */
    public void restore(final int state) {
        offset = state;
    }

    /** Produces another token. Returns tokens of class {@link TokenClass#END} if the end has been reached. */
    public Token next() {
        return (offset < tokens.size())
                ? tokens.get(offset++)
                : Token.END;
    }
}
