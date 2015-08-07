package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.Lexer;
import org.testng.annotations.Test;

import java.util.Arrays;

import static io.github.emanuelpalm.util.testing.TreeUtils.*;
import static org.testng.Assert.assertEquals;

public class TestParser {
    @Test
    public void shouldParseValues() {
        final Parser p = new Parser(new BufferedLexer(Lexer.fromString("123 45.6 symbol")));
        final Tree t = p.run();
        assertEquals(t.root.nodes(), Arrays.asList(integerOf("123"), numberOf("45.6"), symbolOf("symbol")));
    }
}
