package io.github.emanuelpalm.plisp.lexer;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;

public class TestBufferedLexer {
    @Test
    public void shouldSaveAndRestoreState() {
        final BufferedLexer tb = BufferedLexer.fromString("'()");

        assertEquals(tb.next().type(), TokenClass.QUO);

        final int s = tb.state();
        assertEquals(tb.next().type(), TokenClass.PAL);
        assertEquals(tb.next().type(), TokenClass.PAR);

        tb.restore(s);
        assertEquals(tb.next().type(), TokenClass.PAL);
        assertEquals(tb.next().type(), TokenClass.PAR);
        assertEquals(tb.next().type(), TokenClass.END);
        assertEquals(tb.next().type(), TokenClass.END);
    }
}
