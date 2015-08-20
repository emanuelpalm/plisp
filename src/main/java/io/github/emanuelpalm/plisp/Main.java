package io.github.emanuelpalm.plisp;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.Lexer;
import io.github.emanuelpalm.plisp.front.parser.Parser;
import io.github.emanuelpalm.plisp.front.parser.SExpr;

import java.io.File;
import java.io.IOException;

/**
 * Application main class.
 */
public class Main {
    public static void main(final String[] args) throws IOException {
        final BufferedLexer l = new BufferedLexer(Lexer.fromFile(new File(args[0])));
        System.out.println(l);
        final SExpr s = Parser.parse(l);
        System.out.println(s);
    }
}
