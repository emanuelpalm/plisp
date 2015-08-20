package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;

import static io.github.emanuelpalm.plisp.front.parser.Rule.*;

/**
 * Parser.
 *
 * Adheres to the following grammar:
 *
 * EXPR -> _ATM | LIST | QUOT
 * LIST -> "(" EXPR* ")"
 * QUOT -> "'" EXPR
 */
public class Parser {
    /** Parses contents of given buffered lexer. */
    public static SExpr parse(final BufferedLexer l) {
        return expr().apply(l).get();
    }

    private static Rule expr() {
        return (l) -> anyOf(oneOf(TokenClass.ATM), list(), quot())
                .apply(l);
    }

    private static Rule list() {
        return (l) -> allOf(oneOf(TokenClass.PAL), manyOf(expr()), oneOf(TokenClass.PAR))
                .transform((s) -> ((SExpr.Cons) s).cdar())
                .apply(l);
    }

    private static Rule quot() {
        return (l) -> allOf(oneOf(TokenClass.QUO), expr())
                .transform((s) -> new SExpr.Cons(new SExpr.Atom("quote"), ((SExpr.Cons) s).cdr()))
                .apply(l);
    }
}
