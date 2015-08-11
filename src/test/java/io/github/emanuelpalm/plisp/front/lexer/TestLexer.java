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
    public void shouldTokenizeColons() throws Throwable {
        test("::",
                new Token(TokenClass.COL, ":"),
                new Token(TokenClass.COL, ":"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeBrackets() throws Throwable {
        test("[]",
                new Token(TokenClass.BRL, "["),
                new Token(TokenClass.BRR, "]"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeNumbers() throws Throwable {
        test("123 456.789",
                new Token(TokenClass.NUM, "123"),
                new Token(TokenClass.NUM, "456.789"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeNumbersWithInfixOperators() throws Throwable {
        test("+12 -3.45",
                new Token(TokenClass.NUM, "+12"),
                new Token(TokenClass.NUM, "-3.45"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeSymbols() throws Throwable {
        test("word & || \t &! \n + - ++ -- åäö",
                new Token(TokenClass.SYM, "word"),
                new Token(TokenClass.SYM, "&"),
                new Token(TokenClass.SYM, "||"),
                new Token(TokenClass.SYM, "&!"),
                new Token(TokenClass.SYM, "+"),
                new Token(TokenClass.SYM, "-"),
                new Token(TokenClass.SYM, "++"),
                new Token(TokenClass.SYM, "--"),
                new Token(TokenClass.SYM, "åäö")
        );
    }

    @Test
    public void shouldTokenizeExpression() throws Throwable {
        test("(+ 10 (int (foldl * 1. [-72.2 -5.1])))",
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.SYM, "+"),
                new Token(TokenClass.NUM, "10"),
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.SYM, "int"),
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.SYM, "foldl"),
                new Token(TokenClass.SYM, "*"),
                new Token(TokenClass.NUM, "1."),
                new Token(TokenClass.BRL, "["),
                new Token(TokenClass.NUM, "-72.2"),
                new Token(TokenClass.NUM, "-5.1"),
                new Token(TokenClass.BRR, "]"),
                new Token(TokenClass.PAR, ")"),
                new Token(TokenClass.PAR, ")"),
                new Token(TokenClass.PAR, ")")
        );
    }
}
