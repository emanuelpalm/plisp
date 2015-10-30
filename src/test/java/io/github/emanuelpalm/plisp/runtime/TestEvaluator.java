package io.github.emanuelpalm.plisp.runtime;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestEvaluator {
    @Test(dataProvider = "functions")
    public void shouldEvaluateInput(final String input, final SExpr expected) {
        assertEquals(Evaluator.eval(input), expected);
    }

    @DataProvider(name = "functions")
    public Object[][] providerFunction() {
        return new Object[][]{
                new Object[]{"'a", SExpr.Atom.of("a")},

                new Object[]{"(atom 'a)", Evaluator.T},
                new Object[]{"(atom '())", Evaluator.T},
                new Object[]{"(atom '(a b))", Evaluator.F},
                new Object[]{"(atom '('a 'b))", Evaluator.F},

                new Object[]{"(eq 'a 'a)", Evaluator.T},
                new Object[]{"(eq 'a 'b)", Evaluator.F},
                new Object[]{"(eq '('a 'b) '('a 'b))", Evaluator.T},
                new Object[]{"(eq '('a 'b) '('a 'c))", Evaluator.F},

                new Object[]{"(car '(a b))", SExpr.Atom.of("a")},
                new Object[]{"(cdr '(a b))", SExpr.Cons.of(SExpr.Atom.of("b"))},

                new Object[]{"(cons 'a 'b)", new SExpr.Cons(SExpr.Atom.of("a"), SExpr.Atom.of("b"))},

                new Object[]{"(cond ((eq 'a 'b) '1) ((atom 'a) '2))", SExpr.Atom.of("2")},
                new Object[]{"(cond ((atom '(a)) '1) ((eq 'a 'b) '2) ('t '3))", SExpr.Atom.of("3")},

                new Object[]{"((lambda (x) (cons x '(b))) 'a)", SExpr.Cons.of(SExpr.Atom.of("a"), SExpr.Atom.of("b"))},
                new Object[]{"((lambda (x y) (cons x (cdr y))) 'z '(a b c))", SExpr.Cons.of(SExpr.Atom.of("z"), SExpr.Atom.of("b"), SExpr.Atom.of("c"))},

                new Object[]{"((label cadr (lambda (x) (car (cdr x)))) (cadr '(a b)))", SExpr.Atom.of("b")}
        };
    }
}
