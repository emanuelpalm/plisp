package io.github.emanuelpalm.plisp.lexer;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestTokenBuffer {
    @Test
    public void shouldSaveAndRestoreState() {
        final TokenBuffer b = TokenBuffer.fromString("'()");

        assertEquals(b.next().type(), TokenClass.QUO);

        final int s = b.state();
        assertEquals(b.next().type(), TokenClass.PAL);
        assertEquals(b.next().type(), TokenClass.PAR);

        b.restore(s);
        assertEquals(b.next().type(), TokenClass.PAL);
        assertEquals(b.next().type(), TokenClass.PAR);
        assertEquals(b.next().type(), TokenClass.END);
        assertEquals(b.next().type(), TokenClass.END);
    }
}
