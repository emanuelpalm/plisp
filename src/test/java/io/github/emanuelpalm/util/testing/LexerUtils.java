package io.github.emanuelpalm.util.testing;

import io.github.emanuelpalm.plisp.lexer.TokenBuffer;
import io.github.emanuelpalm.plisp.lexer.Token;

import java.util.Arrays;

/**
 * Various lexer utilities relevant for testing purposes.
 */
public class LexerUtils {
    /** Creates lexer containing given tokens. */
    public static TokenBuffer lexerOf(final Token... ts) {
        return new TokenBuffer(Arrays.asList(ts));
    }
}
