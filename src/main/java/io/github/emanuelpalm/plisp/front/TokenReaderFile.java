package io.github.emanuelpalm.plisp.front;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

/**
 * Reads tokens from a file.
 */
public class TokenReaderFile implements TokenReader {
    private final ByteBuffer buffer;
    private final TokenOrigin origin;
    private int readPointer = 0;

    /** Creates new token reader, reading from given file. */
    public TokenReaderFile(final File f) throws IOException {
        try (final RandomAccessFile raf = new RandomAccessFile(f, "r");
             final FileChannel fc = raf.getChannel()) {
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, f.length()).asReadOnlyBuffer();
        }
        origin = new TokenOrigin(f.getAbsolutePath());
    }

    @Override
    public String name() {
        return origin.name;
    }

    @Override
    public byte read() {
        return (readPointer < buffer.limit())
                ? buffer.get(readPointer++)
                : (byte) '\0';
    }

    @Override
    public boolean readIf(final Predicate<Byte> p) {
        if (readPointer < buffer.limit() && p.test(buffer.get(readPointer))) {
            readPointer++;
            return true;
        }
        return false;
    }

    @Override
    public void readWhile(final Predicate<Byte> p) {
        while (readPointer < buffer.limit() && p.test(buffer.get(readPointer))) {
            readPointer++;
        }
    }

    @Override
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

    @Override
    public Token consume(final TokenClass tc) {
        final byte[] bs = new byte[readPointer - buffer.position()];
        buffer.get(bs);

        final Token t = new Token(origin.copy(), tc, new String(bs, StandardCharsets.UTF_8));
        origin.column += bs.length;
        return t;
    }

    @Override
    public void rewind() {
        buffer.rewind();
        origin.reset();
        readPointer = 0;
    }
}
