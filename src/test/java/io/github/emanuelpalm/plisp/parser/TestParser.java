package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.runtime.SExpr;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class TestParser {
    @Test(dataProvider = "lexers")
    public void shouldParseIntoExpression(final String input, final SExpr expected) {
        assertEquals(Parser.parse(input), expected);
    }

    @DataProvider(name = "lexers")
    public Object[][] providerLexers() {
        return new Object[][]{
                new Object[]{"x", SExpr.Atom.of("x")},
                new Object[]{"(x)", SExpr.Cons.of(SExpr.Atom.of("x"))},
                new Object[]{"'x", SExpr.Cons.of(SExpr.Atom.of("quote"), SExpr.Atom.of("x"))},
                new Object[]{"(a . b)", new SExpr.Cons(SExpr.Atom.of("a"), SExpr.Atom.of("b"))},
        };
    }

    @Test(dataProvider = "parserExpressionsAndExceptions")
    public void shouldFailWithParserError(final String input, final Class<ParserException> expected) {
        try {
            Parser.parse(input);
            fail("Expected exception never thrown.");

        } catch (final ParserException e) {
            assertEquals(e.getClass(), expected);
        }
    }

    @DataProvider(name = "parserExpressionsAndExceptions")
    public Object[][] providerParserExpressionsAndExceptions() {
        return new Object[][]{
                new Object[]{"(", ParserException.UnbalancedOpeningParenthesis.class},
                new Object[]{")", ParserException.UnbalancedClosingParenthesis.class},
                new Object[]{"'", ParserException.DanglingQuote.class},
                new Object[]{"(x y", ParserException.UnbalancedOpeningParenthesis.class},
                new Object[]{"(()", ParserException.UnbalancedOpeningParenthesis.class},
                new Object[]{"())", ParserException.UnbalancedClosingParenthesis.class},
                new Object[]{"x'", ParserException.DanglingQuote.class},
                new Object[]{"()'", ParserException.DanglingQuote.class},
                new Object[]{"() x", ParserException.DanglingAtom.class},
        };
    }
}
