package io.github.emanuelpalm.plisp.lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Buffers lexer tokens, allowing them to be consumed non-linearly.
 *
 * @see Lexer
 */
public class BufferedLexer {
    private final List<Token> tokens;
    private int offset = 0;

    /** Creates new buffered lexer. */
    public BufferedLexer(final Lexer l) {
        tokens = new ArrayList<>(512);
        Token t;
        do {
            tokens.add(t = l.next());
        } while (t.type() != TokenClass.END);
    }

    /** Creates new buffered lexer out of given list of tokens. */
    public BufferedLexer(final List<Token> ts) {
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

    @Override public String toString() {
        final StringBuilder b = new StringBuilder(tokens.size() * 8);
        b.append("[ ");
        for (final Token t : tokens) {
            b.append(t.toString());
            b.append(',');
        }
        b.insert(b.length(), ']');
        return tokens.toString();
    }
}
