package io.github.emanuelpalm.plisp.front.lexer;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;

public class TestBufferedLexer {
    @Test
    public void shouldSaveAndRestoreState() {
        final BufferedLexer tb = new BufferedLexer(new ArrayList<>(Arrays.asList(
                new Token(TokenClass.PAL, "("),
                new Token(TokenClass.INT, "123"),
                new Token(TokenClass.PAR, ")")
        )));

        assertEquals(tb.next().type(), TokenClass.PAL);

        final int s = tb.state();
        assertEquals(tb.next().type(), TokenClass.INT);
        assertEquals(tb.next().type(), TokenClass.PAR);

        tb.restore(s);
        assertEquals(tb.next().type(), TokenClass.INT);
        assertEquals(tb.next().type(), TokenClass.PAR);
        assertEquals(tb.next().type(), TokenClass.END);
        assertEquals(tb.next().type(), TokenClass.END);
    }
}
