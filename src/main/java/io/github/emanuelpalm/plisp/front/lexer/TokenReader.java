package io.github.emanuelpalm.plisp.front.lexer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

/**
 * Some origin from which tokens may be read and consumed.
 * <p>
 * The token reader facilitates two significant categories of operations: read and consume. The former advances an
 * internal read pointer through a stream of characters, while the latter advances an internal consume pointer to the
 * current position of the read pointer.
 */
public class TokenReader {
    private final ByteBuffer buffer; // Internal offset pointer is used as consume pointer.
    private final TokenOrigin origin;
    private int readPointer = 0;

    /** Creates token reader using given byte buffer and name. */
    public TokenReader(final ByteBuffer b) {
        buffer = b;
        origin = new TokenOrigin();
    }

    /** Creates token reader that reads the contents of the given file. */
    public static TokenReader of(final File f) throws IOException {
        try (final RandomAccessFile raf = new RandomAccessFile(f, "r");
             final FileChannel fc = raf.getChannel()) {
            final ByteBuffer b = fc.map(FileChannel.MapMode.READ_ONLY, 0, f.length()).asReadOnlyBuffer();
            return new TokenReader(b);
        }
    }

    /** Creates token reader that reads the contents of the given string. */
    public static TokenReader of(final String s) {
        final ByteBuffer b = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
        return new TokenReader(b);
    }

    /** Reads one byte. Returns '\0' if the end of the stream has been reached. */
    public byte read() {
        return (readPointer < buffer.limit())
                ? buffer.get(readPointer++)
                : (byte) '\0';
    }

    /** Forwards the read pointer if given predicate is true. Returns result of predicate test. */
    public boolean readIf(final Predicate<Byte> p) {
        if (readPointer < buffer.limit() && p.test(buffer.get(readPointer))) {
            readPointer++;
            return true;
        }
        return false;
    }

    /** Forwards the read pointer while the given predicate is true. */
    public void readWhile(final Predicate<Byte> p) {
        while (readPointer < buffer.limit() && p.test(buffer.get(readPointer))) {
            readPointer++;
        }
    }

    /** Consumes and discards all characters read up until this call. */
    public void consume() {
        int d = readPointer - buffer.position();
        while (d-- != 0) {
            if (buffer.get() == '\n') {
                origin.row++;
                origin.column = 0;

            } else {
                origin.column++;
            }
        }
    }

    /**
     * Consumes all characters read up until this call, producing a token of given type.
     * <p>
     * The consumed token lexeme <b>must not</b> include the character '\n'.
     */
    public Token consume(final TokenClass tc) {
        final byte[] bs = new byte[readPointer - buffer.position()];
        buffer.get(bs);

        final Token t = new Token(origin.copy(), tc, new String(bs, StandardCharsets.UTF_8));
        origin.column += bs.length;
        return t;
    }

    /** Rewinds read and consume pointers to their initial states. */
    public void rewind() {
        buffer.rewind();
        origin.reset();
        readPointer = 0;
    }
}
