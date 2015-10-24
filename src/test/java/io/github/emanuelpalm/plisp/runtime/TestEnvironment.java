package io.github.emanuelpalm.plisp.runtime;

import io.github.emanuelpalm.plisp.parser.Parser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestEnvironment {
    @Test(dataProvider = "functions")
    public void shouldEvaluateQuote(final String input, final SExpr expected) {
        assertEquals(Parser.parse(input).eval(Environment.create()), expected);
    }

    @DataProvider(name = "functions")
    public Object[][] providerFunction() {
        return new Object[][]{
                new Object[]{"'a", SExpr.Atom.of("a")},

                new Object[]{"(atom a)", Environment.T},
                new Object[]{"(atom 'a)", Environment.F},
                new Object[]{"(atom ())", Environment.F},
                new Object[]{"(atom ('a 'b))", Environment.F},

                new Object[]{"(eq 'a 'a)", Environment.T},
                new Object[]{"(eq 'a 'b)", Environment.F},
                new Object[]{"(eq ('a 'b) ('a 'b))", Environment.T},
                new Object[]{"(eq ('a 'b) ('a 'c))", Environment.F}
        };
    }
}
