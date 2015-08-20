package io.github.emanuelpalm.util.testing;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.Token;

import java.util.Arrays;

/**
 * Various lexer utilities relevant for testing purposes.
 */
public class LexerUtils {
    /** Creates lexer containing given tokens. */
    public static BufferedLexer lexerOf(final Token... ts) {
        return new BufferedLexer(Arrays.asList(ts));
    }
}
