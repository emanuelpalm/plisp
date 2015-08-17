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
    private static final Predicate<Byte> IS_DIGIT = (b) -> b >= '0' && b <= '9';
    private static final Predicate<Byte> IS_SYMBOL_CHARACTER = (b) -> b >= '!' && b <= '~' && b != '(' && b != ')' || b < '\0';

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

            case ' ':
            case '\n':
            case '\r':
            case '\t':
                tokenReader.consume();
                return next();

            case '\'':
                return tokenReader.consume(TokenClass.QUO);

            case '(':
                return tokenReader.consume(TokenClass.PAL);

            case ')':
                return tokenReader.consume(TokenClass.PAR);

            case '+':
            case '-':
                if (!tokenReader.readIf(IS_DIGIT)) break;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                tokenReader.readWhile(IS_DIGIT);
                if (tokenReader.readIf((b0) -> b0 == '.')) {
                    tokenReader.readWhile(IS_DIGIT);
                }
                return tokenReader.consume(TokenClass.NUM);
        }
        if (IS_SYMBOL_CHARACTER.test(b)) {
            tokenReader.readWhile(IS_SYMBOL_CHARACTER);
            return tokenReader.consume(TokenClass.ATM);
        }
        return tokenReader.consume(TokenClass.ERR);
    }
}
