package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.Token;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.emanuelpalm.util.testing.LexerUtils.lexerOf;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TestParser {
    @Test(dataProvider = "lexers")
    public void shouldParseIntoExpression(final BufferedLexer input, final SExpr expected) {
        assertEquals(Parser.parse(input), expected);
    }

    @DataProvider
    public Object[][] lexers() {
        return new Object[][]{
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x")),
                        new SExpr.Atom("x")
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "("), new Token(TokenClass.ATM, "x"), new Token(TokenClass.PAR, ")")),
                        SExpr.Cons.of(new SExpr.Atom("x"))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.QUO, "'"), new Token(TokenClass.ATM, "x")),
                        SExpr.Cons.of(new SExpr.Atom("quote"), new SExpr.Atom("x"))
                }
        };
    }

    @Test(dataProvider = "parserErrorLexers")
    public void shouldFailWithParserError(final BufferedLexer input, final ParserError expected) {
        try {
            Parser.parse(input);
            fail("No parser error generated.");

        } catch (final ParserException e) {
            assertEquals(e.error(), expected);
        }
    }

    @DataProvider
    public Object[][] parserErrorLexers() {
        return new Object[][]{
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "(")),
                        ParserError.UNBALANCED_PAL
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAR, ")")),
                        ParserError.UNBALANCED_PAR
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.QUO, "'")),
                        ParserError.DANGLING_QUOTE
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "("), new Token(TokenClass.ATM, "x"), new Token(TokenClass.ATM, "y")),
                        ParserError.UNBALANCED_PAL
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "("), new Token(TokenClass.PAL, "("), new Token(TokenClass.PAR, ")")),
                        ParserError.UNBALANCED_PAL
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "("), new Token(TokenClass.PAR, ")"), new Token(TokenClass.PAR, ")")),
                        ParserError.UNBALANCED_PAR
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x"), new Token(TokenClass.QUO, "'")),
                        ParserError.DANGLING_QUOTE
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "("), new Token(TokenClass.PAR, ")"), new Token(TokenClass.QUO, "'")),
                        ParserError.DANGLING_QUOTE
                },
        };
    }
}
