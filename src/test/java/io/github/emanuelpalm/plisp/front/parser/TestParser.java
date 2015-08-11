package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.Lexer;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static io.github.emanuelpalm.util.testing.TreeUtils.*;
import static org.testng.Assert.assertEquals;

public class TestParser {
    @Test
    public void shouldParseValues() {
        final Tree t = parserOf("123 45.6 symbol").run();
        assertEquals(t.root.nodes(), Arrays.asList(numberOf("123"), numberOf("45.6"), symbolOf("symbol")));
    }

    private Parser parserOf(final String s) {
        return new Parser(new BufferedLexer(Lexer.fromString(s)));
    }

    @Test
    public void shouldParseList() {
        final Tree t = parserOf("[1 2 3]").run();
        assertEquals(t.root.nodes(), Collections.singletonList(listOf(numberOf("1"), numberOf("2"), numberOf("3"))));
    }

    @Test
    public void shouldParseCall() {
        final Tree t = parserOf("(fib 10)").run();
        assertEquals(t.root.nodes(), Collections.singletonList(callOf(symbolOf("fib"), numberOf("10"))));
    }

    @Test
    public void shouldParseMeta() {
        final Tree t = parserOf("x: Integer").run();
        assertEquals(t.root.nodes(), Collections.singletonList(metaOf(symbolOf("x"), symbolOf("Integer"))));
    }

    @Test
    public void shouldParseComplexExpression() {
        final Tree t = parserOf("(def fact [x: Integer]: Integer (foldl * 1 (range 1 x)))").run();
        assertEquals(t.root.nodes(), Collections.singletonList(
                callOf(
                        symbolOf("def"),
                        symbolOf("fact"),
                        metaOf(listOf(metaOf(symbolOf("x"), symbolOf("Integer"))), symbolOf("Integer")),
                        callOf(
                                symbolOf("foldl"),
                                symbolOf("*"),
                                numberOf("1"),
                                callOf(symbolOf("range"), numberOf("1"), symbolOf("x"))
                        )
                )
        ));
    }
}
