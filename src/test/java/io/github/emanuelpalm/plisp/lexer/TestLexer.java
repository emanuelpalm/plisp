package io.github.emanuelpalm.plisp.lexer;

import io.github.emanuelpalm.plisp.testing.FileUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestLexer {
    @Test
    public void shouldTokenizeParentheses() throws Throwable {
        test("()",
                new Token(null, TokenClass.PAL, "("),
                new Token(null, TokenClass.PAR, ")"),
                new Token(null, TokenClass.END, "")
        );
    }

    private void test(final String input, final Token... expectedOutputs) throws Throwable {
        final Lexer l = new Lexer(FileUtils.fromString(input));
        for (final Token expectedOutput : expectedOutputs) {
            final Token actualOutput = l.next();
            assertEquals(expectedOutput.type(), actualOutput.type(), actualOutput.toString());
            assertEquals(expectedOutput.lexeme(), actualOutput.lexeme(), actualOutput.toString());
        }
    }

    @Test
    public void shouldTokenizeColons() throws Throwable {
        test("::",
                new Token(null, TokenClass.COL, ":"),
                new Token(null, TokenClass.COL, ":"),
                new Token(null, TokenClass.END, "")
        );
    }

    @Test
    public void shouldTokenizeBrackets() throws Throwable {
        test("[]",
                new Token(null, TokenClass.BRL, "["),
                new Token(null, TokenClass.BRR, "]"),
                new Token(null, TokenClass.END, "")
        );
    }

    @Test
    public void shouldTokenizeInteger() throws Throwable {
        test("1234",
                new Token(null, TokenClass.INT, "1234"),
                new Token(null, TokenClass.END, "")
        );
    }

    @Test
    public void shouldTokenizeIntegersWithInfixOperators() throws Throwable {
        test("+123 -456",
                new Token(null, TokenClass.INT, "+123"),
                new Token(null, TokenClass.INT, "-456"),
                new Token(null, TokenClass.END, "")
        );
    }

    @Test
    public void shouldTokenizeNumber() throws Throwable {
        test("123.456",
                new Token(null, TokenClass.NUM, "123.456"),
                new Token(null, TokenClass.END, "")
        );
    }

    @Test
    public void shouldTokenizeNumbersWithInfixOperators() throws Throwable {
        test("+12.3 -4.56",
                new Token(null, TokenClass.NUM, "+12.3"),
                new Token(null, TokenClass.NUM, "-4.56"),
                new Token(null, TokenClass.END, "")
        );
    }

    @Test
    public void shouldTokenizeSymbols() throws Throwable {
        test("word & || \t &! \n + - ++ -- åäö",
                new Token(null, TokenClass.SYM, "word"),
                new Token(null, TokenClass.SYM, "&"),
                new Token(null, TokenClass.SYM, "||"),
                new Token(null, TokenClass.SYM, "&!"),
                new Token(null, TokenClass.SYM, "+"),
                new Token(null, TokenClass.SYM, "-"),
                new Token(null, TokenClass.SYM, "++"),
                new Token(null, TokenClass.SYM, "--"),
                new Token(null, TokenClass.SYM, "åäö")
        );
    }
}
