package io.github.emanuelpalm.plisp.front.lexer;

import org.testng.Assert;
import org.testng.annotations.*;

public class TestTokenReader {
    private static final String READER_CONTENTS = "A\nsentence of words.";

    private final TokenReader tokenReader = TokenReader.of(READER_CONTENTS);

    @BeforeMethod
    public void setUp() {
        tokenReader.rewind();
    }

    @Test
    public void shouldReadAndConsumeTheFirstCharacter() {
        tokenReader.read();
        final Token result = tokenReader.consume(TokenClass.ERR);
        assertEquals(token(1, 0, TokenClass.ERR, "A"), result);
    }

    private Token token(final int row, final int column, final TokenClass tc, final String lexeme) {
        return new Token(new TokenOrigin(tokenReader.name(), row, column), tc, lexeme);
    }

    private void assertEquals(final Token actual, final Token expected) {
        Assert.assertEquals(actual, expected);
        Assert.assertEquals(actual.origin(), expected.origin());
    }

    @Test
    public void shouldSkip10CharactersAndThenConsumeNothing() {
        for (int i = 10; i-- != 0; ) {
            tokenReader.read();
        }
        tokenReader.consume();
        final Token result = tokenReader.consume(TokenClass.ERR);
        assertEquals(token(2, 8, TokenClass.ERR, ""), result);
    }

    @Test
    public void shouldFailToRead3rdCharacterAsX() {
        tokenReader.read();
        tokenReader.read();
        tokenReader.consume();
        tokenReader.readIf((b) -> b == 'x');
        final Token result = tokenReader.consume(TokenClass.ERR);
        assertEquals(token(2, 0, TokenClass.ERR, ""), result);
    }

    @Test
    public void shouldSucceedToRead3rdCharacterAsS() {
        tokenReader.read();
        tokenReader.read();
        tokenReader.consume();
        tokenReader.readIf((b) -> b == 's');
        final Token result = tokenReader.consume(TokenClass.ERR);
        assertEquals(token(2, 0, TokenClass.ERR, "s"), result);
    }

    @Test
    public void shouldReadAndConsumeAllCharacters() {
        tokenReader.readWhile((b) -> true);
        final Token result = tokenReader.consume(TokenClass.ERR);
        assertEquals(token(1, 0, TokenClass.ERR, READER_CONTENTS), result);
    }

    @Test
    public void shouldSkip2CharactersAndThenReadAndConsumeTheWordSentence() {
        tokenReader.read();
        tokenReader.read();
        tokenReader.consume();
        tokenReader.readWhile((b) -> b >= 'a' && b <= 'z');
        final Token result = tokenReader.consume(TokenClass.ERR);
        assertEquals(token(2, 0, TokenClass.ERR, "sentence"), result);
    }
}
