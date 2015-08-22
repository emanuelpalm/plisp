package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;

import static io.github.emanuelpalm.plisp.front.parser.Rule.*;

/**
 * Parser.
 *
 * Adheres to the following grammar:
 *
 * PROG -> EXPR _END | EXPR ERROR | ERROR
 * EXPR -> _ATM | LIST | QUOT
 * LIST -> "(" EXPR* ")"
 * QUOT -> "'" EXPR
 *
 * ERROR -> ERR_PAL | ERR_PAR | ERR_QUO | ERR_ATM
 * ERR_PAL -> "("
 * ERR_PAR -> ")"
 * ERR_QUO -> "'"
 * ERR_ATM -> _ATM
 */
public class Parser {
    /** Parses contents of given buffered lexer. */
    public static SExpr parse(final BufferedLexer l) {
        return prog().apply(l).get();
    }

    // Primary productions.

    private static Rule prog() {
        return (l) -> anyOf(allOf(expr(), oneOf(TokenClass.END)), allOf(expr(), error()), error())
                .transform((s) -> ((SExpr.Cons) s).car())
                .apply(l);
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

    // Error productions.

    private static Rule error() {
        return (l) -> anyOf(errPal(), errPar(), errQuo(), errAtm())
                .apply(l);
    }

    private static Rule errPal() {
        return (l) -> oneOf(TokenClass.PAL)
                .transform((s) -> {
                    throw new ParserException(ParserError.UNBALANCED_PAL);
                })
                .apply(l);
    }

    private static Rule errPar() {
        return (l) -> oneOf(TokenClass.PAR)
                .transform((s) -> {
                    throw new ParserException(ParserError.UNBALANCED_PAR);
                })
                .apply(l);
    }

    private static Rule errQuo() {
        return (l) -> oneOf(TokenClass.QUO)
                .transform((s) -> {
                    throw new ParserException(ParserError.DANGLING_QUOTE);
                })
                .apply(l);
    }

    private static Rule errAtm() {
        return (l) -> oneOf(TokenClass.ATM)
                .transform((s) -> {
                    throw new ParserException(ParserError.DANGLING_ATOM);
                })
                .apply(l);
    }
}
