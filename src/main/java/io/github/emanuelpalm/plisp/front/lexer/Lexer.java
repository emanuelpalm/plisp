package io.github.emanuelpalm.plisp.front.lexer;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

/**
 * Lexical analyzer, producing tokens from some input source.
 * <p>
 * The read source is expected to only contain valid UTF-8 characters. Performs not input validation.
 */
public class Lexer {
    private static final Predicate<Byte> IS_ATOM_CHARACTER = (b) -> b >= '!' && b <= '~' && b != '(' && b != ')' || b < '\0';
    private static final Predicate<Byte> IS_INVISIBLE_CHARACTER = (b) -> b > 0 && b <= ' ' || b == 0x7f;

    private final TokenReader tokenReader;

    /** Creates new lexer, producing tokens via given token reader. */
    public Lexer(final TokenReader r) {
        tokenReader = r;
    }

    /** Creates new lexer, producing tokens from contents of given file. */
    public static Lexer fromFile(final File f) throws IOException {
        return new Lexer(TokenReader.of(f));
    }

    /** Creates new lexer, producing tokens from contents of given string. */
    public static Lexer fromString(final String s) {
        return new Lexer(TokenReader.of(s));
    }

    /** Produces another token. Returns tokens of class {@link TokenClass#END} if the end has been reached. */
    public Token next() {
        final byte b = tokenReader.read();
        switch (b) {
            case '\0':
                return Token.END;

            case '\'':
                return tokenReader.consume(TokenClass.QUO);

            case '(':
                return tokenReader.consume(TokenClass.PAL);

            case ')':
                return tokenReader.consume(TokenClass.PAR);
        }
        if (IS_INVISIBLE_CHARACTER.test(b)) {
            tokenReader.consume();
            return next();
        }
        if (IS_ATOM_CHARACTER.test(b)) {
            tokenReader.readWhile(IS_ATOM_CHARACTER);
            return tokenReader.consume(TokenClass.ATM);
        }
        return tokenReader.consume(TokenClass.ERR);
    }
}
