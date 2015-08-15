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
    public void shouldParseNumber() {
        final TreeNode t = parserOf("123").run();
        assertEquals(t, numberOf("123"));
    }

    private Parser parserOf(final String s) {
        return new Parser(new BufferedLexer(Lexer.fromString(s)));
    }

    @Test
    public void shouldParseList() {
        final TreeNode t = parserOf("(1 2 3)").run();
        assertEquals(t, listOf(numberOf("1"), numberOf("2"), numberOf("3")));
    }

    @Test
    public void shouldParseComplexExpression() {
        final TreeNode t = parserOf("(defn fact (x) (foldl * 1 (range 1 x)))").run();
        assertEquals(t,
                listOf(
                        symbolOf("defn"),
                        symbolOf("fact"),
                        listOf(symbolOf("x")),
                        listOf(
                                symbolOf("foldl"),
                                symbolOf("*"),
                                numberOf("1"),
                                listOf(symbolOf("range"), numberOf("1"), symbolOf("x"))
                        )
                )
        );
    }
}
