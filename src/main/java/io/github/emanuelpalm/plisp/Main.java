package io.github.emanuelpalm.plisp;

import io.github.emanuelpalm.plisp.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.lexer.Lexer;
import io.github.emanuelpalm.plisp.parser.Parser;
import io.github.emanuelpalm.plisp.runtime.SExpr;

import java.io.File;
import java.io.IOException;

/**
 * Application main class.
 */
public class Main {
    public static void main(final String[] args) throws IOException {
        final BufferedLexer l = new BufferedLexer(Lexer.fromFile(new File(args[0])));
        try {
            final SExpr s = Parser.parse(l);
            System.out.println(s);

        } catch (final Throwable e) {
            System.err.println("Parser error: " + e.getMessage());
        }
    }
}
