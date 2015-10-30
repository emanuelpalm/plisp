package io.github.emanuelpalm.util.testing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Various file utilities relevant for testing purposes.
 */
public class FileUtils {
    /** Creates new temporary file containing given byte array. */
    public static File fromBytes(final byte[] bs) throws IOException {
        final File f = File.createTempFile("FileUtils_", ".tmp");
        try (final FileOutputStream out = new FileOutputStream(f)) {
            out.write(bs);
        }
        return f;
    }

    /** Creates new temporary file containing given string. */
    public static File fromString(final String s) throws IOException {
        return fromBytes(s.getBytes(StandardCharsets.UTF_8));
    }
}
