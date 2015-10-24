package io.github.emanuelpalm.plisp.runtime;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestSExpr {
    @Test
    public void shouldReturnNulIfCallingCarOrCdrOnAtom() {
        final SExpr.Atom a = new SExpr.Atom("test");
        assertEquals(a.car(), SExpr.NUL);
        assertEquals(a.cdr(), SExpr.NUL);
    }

    @Test
    public void shouldReturnGivenSExprsIfCallingCarOrCdrOnCons() {
        final SExpr.Cons c = new SExpr.Cons(new SExpr.Atom("a"), new SExpr.Atom("b"));
        assertEquals(c.car(), new SExpr.Atom("a"));
        assertEquals(c.cdr(), new SExpr.Atom("b"));
    }

    @Test
    public void shouldConstructCorrectConsFromListOfSExprs() {
        final SExpr s = SExpr.Cons.of(new SExpr.Atom("a"), new SExpr.Atom("b"), new SExpr.Atom("c"));
        assertEquals(s, new SExpr.Cons(
                new SExpr.Atom("a"), new SExpr.Cons(
                new SExpr.Atom("b"), new SExpr.Cons(
                new SExpr.Atom("c"), SExpr.NUL))));
    }
}
