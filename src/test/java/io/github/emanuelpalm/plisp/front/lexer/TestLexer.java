package io.github.emanuelpalm.plisp.front.lexer;

import io.github.emanuelpalm.util.testing.FileUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestLexer {
    @Test
    public void shouldTokenizeParentheses() throws Throwable {
        test("()",
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.PAR, ")"),
                Token.END
        );
    }

    private void test(final String input, final Token... expectedOutputs) throws Throwable {
        final Lexer l = Lexer.fromFile(FileUtils.fromString(input));
        for (final Token expectedOutput : expectedOutputs) {
            final Token actualOutput = l.next();
            assertEquals(actualOutput.type(), expectedOutput.type(), actualOutput.toString());
            assertEquals(actualOutput.lexeme(), expectedOutput.lexeme(), actualOutput.toString());
        }
    }

    @Test
    public void shouldTokenizeSymbols() throws Throwable {
        test("word & || \t &! \n + - ++ -- åäö",
                new Token(TokenClass.ATM, "word"),
                new Token(TokenClass.ATM, "&"),
                new Token(TokenClass.ATM, "||"),
                new Token(TokenClass.ATM, "&!"),
                new Token(TokenClass.ATM, "+"),
                new Token(TokenClass.ATM, "-"),
                new Token(TokenClass.ATM, "++"),
                new Token(TokenClass.ATM, "--"),
                new Token(TokenClass.ATM, "åäö")
        );
    }

    @Test
    public void shouldTokenizeExpression() throws Throwable {
        test("(+ 10 (int (foldl '* 1. '(-72.2 -5.1))))",
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.ATM, "+"),
                new Token(TokenClass.ATM, "10"),
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.ATM, "int"),
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.ATM, "foldl"),
                new Token(TokenClass.QUO, "'"),
                new Token(TokenClass.ATM, "*"),
                new Token(TokenClass.ATM, "1."),
                new Token(TokenClass.QUO, "'"),
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.ATM, "-72.2"),
                new Token(TokenClass.ATM, "-5.1"),
                new Token(TokenClass.PAR, ")"),
                new Token(TokenClass.PAR, ")"),
                new Token(TokenClass.PAR, ")"),
                new Token(TokenClass.PAR, ")")
        );
    }
}
