package io.github.emanuelpalm.plisp.runtime;

import io.github.emanuelpalm.plisp.lexer.Token;
import io.github.emanuelpalm.plisp.lexer.TokenClass;
import io.github.emanuelpalm.plisp.lexer.TokenOrigin;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestSExpr {
    @Test
    public void shouldReturnSelfAndNulIfCallingCarAndCdrOnAtom() {
        final SExpr.Atom a = SExpr.Atom.of("test");
        assertEquals(a.car(), a);
        assertEquals(a.cdr(), SExpr.NUL);
    }

    @Test
    public void shouldReturnGivenSExprsIfCallingCarOrCdrOnCons() {
        final SExpr.Cons c = new SExpr.Cons(SExpr.Atom.of("a"), SExpr.Atom.of("b"));
        assertEquals(c.car(), SExpr.Atom.of("a"));
        assertEquals(c.cdr(), SExpr.Atom.of("b"));
    }

    @Test
    public void shouldConstructCorrectConsFromListOfSExprs() {
        final SExpr s = SExpr.Cons.of(SExpr.Atom.of("a"), SExpr.Atom.of("b"), SExpr.Atom.of("c"));
        assertEquals(s, new SExpr.Cons(
                SExpr.Atom.of("a"), new SExpr.Cons(
                SExpr.Atom.of("b"), new SExpr.Cons(
                SExpr.Atom.of("c"), SExpr.NUL))));
    }

    @Test
    public void shouldConcatenateConsLists() {
        final SExpr c1 = SExpr.Cons.of(SExpr.Atom.of("c"), SExpr.Atom.of("d"));

        final SExpr c0a = SExpr.Cons.of(SExpr.Atom.of("a"), SExpr.Atom.of("b"));
        assertEquals(c0a.concat(c1), SExpr.Cons.of(
                SExpr.Atom.of("a"),
                SExpr.Atom.of("b"),
                SExpr.Atom.of("c"),
                SExpr.Atom.of("d")
        ));

        final SExpr c0b = new SExpr.Cons(SExpr.Atom.of("x"), SExpr.Atom.of("y")); // Not NUL-terminated.
        assertEquals(c0b.concat(c1), SExpr.Cons.of(
                SExpr.Atom.of("x"),
                SExpr.Atom.of("y"),
                SExpr.Atom.of("c"),
                SExpr.Atom.of("d")
        ));
    }

    @Test
    public void shouldZipConsLists() {
        final SExpr c1 = SExpr.Cons.of(SExpr.Atom.of("b0"), SExpr.Atom.of("b1"));

        final SExpr c0a = SExpr.Cons.of(SExpr.Atom.of("a0"), SExpr.Atom.of("a1"));
        assertEquals(c0a.zip(c1), SExpr.Cons.of(
                new SExpr.Cons(SExpr.Atom.of("a0"), SExpr.Atom.of("b0")),
                new SExpr.Cons(SExpr.Atom.of("a1"), SExpr.Atom.of("b1"))
        ));

        final SExpr c0b = new SExpr.Cons(SExpr.Atom.of("a0"), SExpr.Atom.of("a1")); // Not NUL-terminated.
        assertEquals(c0b.zip(c1), SExpr.Cons.of(
                new SExpr.Cons(SExpr.Atom.of("a0"), SExpr.Atom.of("b0")),
                new SExpr.Cons(SExpr.Atom.of("a1"), SExpr.Atom.of("b1"))
        ));
    }

    @Test
    public void shouldProvideChildTokenIfNoConsTokenIsAvailable() {
        final SExpr c0 = SExpr.Cons.of(
                new SExpr.Atom(new Token(new TokenOrigin(1, 2),TokenClass.ATM, "a"))
        );
        assertEquals(c0.token().get(), new Token(new TokenOrigin(1, 2),TokenClass.ATM, "a"));

        final SExpr c1 = SExpr.Cons.of(
                SExpr.Atom.of("a"),
                SExpr.Cons.of(new SExpr.Atom(new Token(new TokenOrigin(1, 4),TokenClass.ATM, "b")))
        );
        assertEquals(c1.token().get(), new Token(new TokenOrigin(1, 4),TokenClass.ATM, "b"));
    }
}
