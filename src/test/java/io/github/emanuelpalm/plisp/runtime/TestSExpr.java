package io.github.emanuelpalm.plisp.runtime;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestSExpr {
    @Test
    public void shouldReturnNulIfCallingCarOrCdrOnAtom() {
        final SExpr.Atom a = SExpr.Atom.of("test");
        assertEquals(a.car(), SExpr.NUL);
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
}
