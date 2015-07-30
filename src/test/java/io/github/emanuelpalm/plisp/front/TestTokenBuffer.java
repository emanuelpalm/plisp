package io.github.emanuelpalm.plisp.front;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;

public class TestTokenBuffer {
    @Test
    public void shouldSaveAndRestoreState() {
        final TokenBuffer tb = new TokenBuffer(new ArrayList<>(Arrays.asList(
                new Token(null, TokenClass.PAL, "("),
                new Token(null, TokenClass.INT, "123"),
                new Token(null, TokenClass.PAR, ")")
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
