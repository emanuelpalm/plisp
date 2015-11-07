package io.github.emanuelpalm.plisp.analyzer;

import io.github.emanuelpalm.plisp.anaylzer.Prototype;
import io.github.emanuelpalm.plisp.parser.Parser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestPrototype {
    @Test(dataProvider = "providerPrototypesAndStrings")
    public void shouldConvertIntoCorrectString(final Prototype input, final String expected) {
        assertEquals(input.toString(), expected);
    }

    @DataProvider
    public Object[][] providerPrototypesAndStrings() {
        return new Object[][]{
                new Object[]{new Prototype("x", 0), "X"},
                new Object[]{new Prototype("quote", 1), "QUOTE[a]"},
                new Object[]{new Prototype("cons", 2), "CONS[a;b]"},
                new Object[]{new Prototype("subst", 3), "SUBST[a;b;c]"},
        };
    }

    @Test(dataProvider = "providerSExprsPairsAndPrototypes")
    public void shouldConstructCorrectPrototypeFromExpression(final String input, final Prototype expected) {
        assertEquals(Prototype.fromLabelExpression(Parser.parse(input)), expected);
    }

    @DataProvider
    public Object[][] providerSExprsPairsAndPrototypes() {
        return new Object[][]{
                new Object[]{"(label x 'c)", new Prototype("x", 0)},
                new Object[]{"(label cadr (lambda (x) (car (cdr x))))", new Prototype("cadr", 1)},
                new Object[]{"(label find (lambda (x y) (cond ((eq x (car y)) x) ('t (find (cdr y))))))", new Prototype("find", 2)},
                new Object[]{"(label subst (lambda (x y z) (cond ((atom z) (cond ((eq z y) x) ('t z))) ('t (cons (subst x y (car z)) (subst x y (cdr z)))))))", new Prototype("subst", 3)}
        };
    }
}
