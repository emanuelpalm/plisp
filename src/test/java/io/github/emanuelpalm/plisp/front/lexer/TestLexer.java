package io.github.emanuelpalm.plisp.front.lexer;

import io.github.emanuelpalm.util.testing.FileUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestLexer {
    @Test
    public void shouldTokenizeParentheses() throws Throwable {
        test("()",
                new Token(null, TokenClass.PAL, "("),
                new Token(null, TokenClass.PAR, ")"),
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
                new Token(null, TokenClass.COL, ":"),
                new Token(null, TokenClass.COL, ":"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeBrackets() throws Throwable {
        test("[]",
                new Token(null, TokenClass.BRL, "["),
                new Token(null, TokenClass.BRR, "]"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeInteger() throws Throwable {
        test("1234",
                new Token(null, TokenClass.INT, "1234"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeIntegersWithInfixOperators() throws Throwable {
        test("+123 -456",
                new Token(null, TokenClass.INT, "+123"),
                new Token(null, TokenClass.INT, "-456"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeNumber() throws Throwable {
        test("123.456",
                new Token(null, TokenClass.NUM, "123.456"),
                Token.END
        );
    }

    @Test
    public void shouldTokenizeNumbersWithInfixOperators() throws Throwable {
        test("+12.3 -4.56",
                new Token(null, TokenClass.NUM, "+12.3"),
                new Token(null, TokenClass.NUM, "-4.56"),
                Token.END
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

    @Test
    public void shouldTokenizeExpression() throws Throwable {
        test("(+ 10 (int (foldl * 1. [-72.2 -5.1])))",
                new Token(null, TokenClass.PAL, "("),
                new Token(null, TokenClass.SYM, "+"),
                new Token(null, TokenClass.INT, "10"),
                new Token(null, TokenClass.PAL, "("),
                new Token(null, TokenClass.SYM, "int"),
                new Token(null, TokenClass.PAL, "("),
                new Token(null, TokenClass.SYM, "foldl"),
                new Token(null, TokenClass.SYM, "*"),
                new Token(null, TokenClass.NUM, "1."),
                new Token(null, TokenClass.BRL, "["),
                new Token(null, TokenClass.NUM, "-72.2"),
                new Token(null, TokenClass.NUM, "-5.1"),
                new Token(null, TokenClass.BRR, "]"),
                new Token(null, TokenClass.PAR, ")"),
                new Token(null, TokenClass.PAR, ")"),
                new Token(null, TokenClass.PAR, ")")
        );
    }
}
