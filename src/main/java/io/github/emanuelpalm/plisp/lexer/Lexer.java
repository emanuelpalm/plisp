package io.github.emanuelpalm.plisp.lexer;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

/**
 * Lexical analyzer, producing tokens from some input source.
 * <p>
 * The read source is expected to only contain valid UTF-8 characters. No input validation will be performed.
 */
public class Lexer {
    private static final Predicate<Byte> IS_DIGIT = (b) -> b >= '0' && b <= '9';
    private static final Predicate<Byte> IS_SYMBOL_CHARACTER = (b) -> b >= '!' && b <= '~' && b != '(' && b != ')' && b != ':' && b != '[' && b != ']' || b < '\0';

    private final TokenReader tokenReader;

    /** Produces new lexer, using given file as input. */
    public Lexer(final File f) throws IOException {
        tokenReader = new TokenReaderFile(f);
    }

    /** Produces another token. Returns tokens of class {@link TokenClass#END} if the end has been reached. */
    public Token next() {
        final byte b = tokenReader.read();
        switch (b) {
            case '\0':
                return tokenReader.consume(TokenClass.END);

            case ' ':
            case '\n':
            case '\r':
            case '\t':
                tokenReader.consume();
                return next();

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

            case ':':
                return tokenReader.consume(TokenClass.COL);

            case '[':
                return tokenReader.consume(TokenClass.BRL);

            case ']':
                return tokenReader.consume(TokenClass.BRR);
        }
        if (IS_SYMBOL_CHARACTER.test(b)) {
            tokenReader.readWhile(IS_SYMBOL_CHARACTER);
            return tokenReader.consume(TokenClass.SYM);
        }
        return tokenReader.consume(TokenClass.ERR);
    }
}
