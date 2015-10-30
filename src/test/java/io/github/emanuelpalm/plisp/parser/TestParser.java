package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.runtime.SExpr;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TestParser {
    @Test(dataProvider = "lexers")
    public void shouldParseIntoExpression(final String input, final SExpr expected) {
        assertEquals(Parser.parse(input), expected);
    }

    @DataProvider
    public Object[][] lexers() {
        return new Object[][]{
                new Object[]{"x", SExpr.Atom.of("x")},
                new Object[]{"(x)", SExpr.Cons.of(SExpr.Atom.of("x"))},
                new Object[]{"'x", SExpr.Cons.of(SExpr.Atom.of("quote"), SExpr.Atom.of("x"))}
        };
    }

    @Test(dataProvider = "parserErrorLexers")
    public void shouldFailWithParserError(final String input, final ParserError expected) {
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
                new Object[]{"(", ParserError.UNBALANCED_PAL},
                new Object[]{")", ParserError.UNBALANCED_PAR},
                new Object[]{"'", ParserError.DANGLING_QUOTE},
                new Object[]{"(x y", ParserError.UNBALANCED_PAL},
                new Object[]{"(()", ParserError.UNBALANCED_PAL},
                new Object[]{"())", ParserError.UNBALANCED_PAR},
                new Object[]{"x'", ParserError.DANGLING_QUOTE},
                new Object[]{"()'", ParserError.DANGLING_QUOTE},
        };
    }
}
