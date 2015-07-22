package io.github.emanuelpalm.plisp.lexer;

import java.util.function.Predicate;

/**
 * Some origin from which tokens may be read and consumed.
 * <p>
 * The token reader facilitates two significant categories of operations: read and consume. The former advances an
 * internal read pointer through a stream of characters, while the latter advances an internal consume pointer to the
 * current position of the internal read pointer.
 */
public interface TokenReader {
    /** Yields a name identifying the reader. */
    String name();

    /** Reads one 8-bit unsigned character. Returns '\0' if the end of the stream has been reached. */
    byte read();

    /** Forwards the read pointer if given predicate is true. Returns result of predicate test. */
    boolean readIf(final Predicate<Byte> p);

    /** Forwards the read pointer while the given predicate is true. */
    void readWhile(final Predicate<Byte> p);

    /** Consumes all characters read up until this call. */
    void consume();

    /**
     * Consumes all characters read up until this call, producing a token of given type out of the characters.
     * <p>
     * The token lexeme may not include the character '\n'.
     */
    Token consume(final TokenClass tc);

    /** Rewinds read and consume pointers to their initial states. */
    void rewind();
}
