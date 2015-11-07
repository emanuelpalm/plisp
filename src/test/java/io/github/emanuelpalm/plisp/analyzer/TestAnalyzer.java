package io.github.emanuelpalm.plisp.analyzer;

import io.github.emanuelpalm.plisp.anaylzer.Analyzer;
import io.github.emanuelpalm.plisp.anaylzer.AnalyzerException;
import io.github.emanuelpalm.plisp.parser.Parser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.assertEquals;

public class TestAnalyzer {
    @Test(dataProvider = "expressionsAndExceptions")
    public void shouldDiscoverAnyFault(final String input, final Optional<Class<Throwable>> expectedException) {
        Optional<Class> actualException;
        try {
            Analyzer.checkEnvironmentOf(Parser.parse(input));
            actualException = Optional.empty();

        } catch (final AnalyzerException e) {
            actualException = Optional.of(e.getClass());
        }
        assertEquals(actualException, expectedException);
    }

    @DataProvider(name = "expressionsAndExceptions")
    public static Object[][] providerExpressionsAndExceptions() {
        return new Object[][]{
                new Object[]{"'x", Optional.empty()},
                new Object[]{"'(a b c)", Optional.empty()},
                new Object[]{"(car '(a . b))", Optional.empty()},
                new Object[]{"(car (cdr '(a b c)))", Optional.empty()},
                new Object[]{"(cond ((eq 'a 'b) 'a) ('t 'b))", Optional.empty()},
                new Object[]{"((label cadr (lambda (x) (car (cdr x)))) (cadr '(a b)))", Optional.empty()},
                new Object[]{"((label cdar (lambda (x) (cdar (car x)))) cdar)", Optional.empty()},
                new Object[]{"((lambda (x) (car (car (x)))) ((a) (b)))", Optional.empty()},

                new Object[]{"x", Optional.of(AnalyzerException.NotDefined.class)},
                new Object[]{"(a b c)", Optional.of(AnalyzerException.NotDefined.class)},
                new Object[]{"(cwr '(a . b))", Optional.of(AnalyzerException.NotDefined.class)},
                new Object[]{"(car (cwr '(a b c)))", Optional.of(AnalyzerException.NotDefined.class)},
                new Object[]{"((label cadr (lambda (x) (car (cdr x)))) (cadr 'a 'b))", Optional.of(AnalyzerException.PrototypeMismatch.class)},
                new Object[]{"((label a) a)", Optional.of(AnalyzerException.PrototypeMismatch.class)},
                new Object[]{"((label cdar (lambda (x) (cdr (cadar x)))) cdar)", Optional.of(AnalyzerException.NotDefined.class)},
                new Object[]{"((lambda (x) (car (car (x)))) 'a 'b)", Optional.of(AnalyzerException.LambdaMisuse.class)},
                new Object[]{"((lambda (x) (eq x y)) 'a)", Optional.of(AnalyzerException.NotDefined.class)},
        };
    }
}
