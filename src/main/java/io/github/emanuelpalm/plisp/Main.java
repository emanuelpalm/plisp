package io.github.emanuelpalm.plisp;

import io.github.emanuelpalm.plisp.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.lexer.Lexer;
import io.github.emanuelpalm.plisp.parser.Parser;
import io.github.emanuelpalm.plisp.parser.ParserException;
import io.github.emanuelpalm.plisp.runtime.Evaluator;
import io.github.emanuelpalm.plisp.runtime.SExpr;
import io.github.emanuelpalm.plisp.runtime.SExprException;

import java.io.File;
import java.io.IOException;

/**
 * Application main class.
 */
public class Main {
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar plisp.jar <file>");
            return;
        }

        final BufferedLexer lexer;
        try {
            lexer = new BufferedLexer(Lexer.fromFile(new File(args[0])));

        } catch (final IOException e) {
            System.err.println("File error: " + e.getMessage());
            return;
        }

        final SExpr expr;
        try {
            expr = Parser.parse(lexer);

        } catch (final ParserException e) {
            System.err.println("Parser error: " + e.getMessage());
            return;
        }

        try {
            System.out.println(Evaluator.eval(expr));

        } catch (final SExprException e) {
            System.err.println("Runtime error: " + e.getMessage());
        }
    }
}
