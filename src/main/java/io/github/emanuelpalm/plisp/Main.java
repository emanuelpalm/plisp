package io.github.emanuelpalm.plisp;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.Lexer;
import io.github.emanuelpalm.plisp.front.parser.Parser;
import io.github.emanuelpalm.plisp.front.parser.TreeNode;

import java.io.File;
import java.io.IOException;

/**
 * Application main class.
 */
public class Main {
    public static void main(final String[] args) {
        try {
            final BufferedLexer l = new BufferedLexer(Lexer.fromFile(new File(args[0])));
            final Parser p = new Parser(l);

            final TreeNode t = p.run();
            System.out.println(t);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
