package io.github.emanuelpalm.plisp.runtime;

import io.github.emanuelpalm.plisp.parser.Parser;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestEnvironment {
    @Test
    public void shouldEvaluateQuotedAtomToAtom() {
        final SExpr a = Parser.parse("'a").eval(Environment.create());
        assertEquals(a, SExpr.Atom.of("a"));
    }
}
