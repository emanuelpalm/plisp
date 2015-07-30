package io.github.emanuelpalm.plisp.front.lexer;

import io.github.emanuelpalm.util.testing.FileUtils;
import org.testng.annotations.*;

import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertEquals;

public class TestTokenReader {
    private static final byte[] READER_CONTENTS = "A\nsentence of words.".getBytes(StandardCharsets.UTF_8);

    private final TokenReader tokenReader;

    /** All readers provided to this method are expected to contain a copy of {@link #READER_CONTENTS}. */
    @Factory(dataProvider = "tokenReaders")
    public TestTokenReader(final TokenReader tr) {
        tokenReader = tr;
    }

    @DataProvider
    public static Object[][] tokenReaders() throws Throwable {
        return new Object[][]{
                new Object[]{new TokenReaderFile(FileUtils.fromBytes(READER_CONTENTS))}
        };
    }

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
        assertEquals(token(1, 0, TokenClass.ERR, new String(READER_CONTENTS, StandardCharsets.UTF_8)), result);
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
