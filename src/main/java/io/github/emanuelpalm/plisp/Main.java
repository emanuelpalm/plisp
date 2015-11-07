package io.github.emanuelpalm.plisp;

import io.github.emanuelpalm.plisp.anaylzer.Analyzer;
import io.github.emanuelpalm.plisp.anaylzer.AnalyzerException;
import io.github.emanuelpalm.plisp.lexer.TokenBuffer;
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
        if (!(args.length == 1 || (args.length == 2 && args[1].equals("--analyze")))) {
            System.err.println("Usage: ./plisp <file> [--analyze]");
            return;
        }

        final TokenBuffer buffer;
        try {
            buffer = new TokenBuffer(Lexer.fromFile(new File(args[0])));

        } catch (final IOException e) {
            System.err.println("File error: " + e.getMessage());
            return;
        }

        final SExpr expr;
        try {
            expr = Parser.parse(buffer);

        } catch (final ParserException e) {
            System.err.println("Parser error: " + e.getMessage());
            return;
        }

        if (args.length == 2) {
            try {
                Analyzer.checkEnvironmentOf(expr);
                System.out.println("No errors detected.");

            } catch (final AnalyzerException e) {
                System.err.println("Analyzer error: " + e.getMessage());
            }
        } else {
            try {
                System.out.println(Evaluator.eval(expr));

            } catch (final SExprException e) {
                System.err.println("Runtime error: " + e.getMessage());
            }
        }
    }
}
