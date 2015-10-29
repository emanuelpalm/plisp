package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.lexer.Lexer;
import io.github.emanuelpalm.plisp.lexer.TokenClass;
import io.github.emanuelpalm.plisp.runtime.SExpr;

import static io.github.emanuelpalm.plisp.parser.Rule.*;

/**
 * Parser.
 * <p>
 * Adheres to the following grammar:
 * <pre>
 * PROG -> EXPR _END | EXPR ERROR | ERROR
 * EXPR -> _ATM | CONS | LIST | QUOT
 * CONS -> "(" EXPR "." EXPR ")"
 * LIST -> "(" EXPR* ")"
 * QUOT -> "'" EXPR
 * </pre>
 * <pre>
 * ERROR -> ERR_PAL | ERR_PAR | ERR_QUO | ERR_ATM
 * ERR_PAL -> "("
 * ERR_PAR -> ")"
 * ERR_QUO -> "'"
 * ERR_ATM -> _ATM
 * </pre>
 */
public class Parser {
    /** Parses contents of given buffered lexer. */
    public static SExpr parse(final BufferedLexer l) {
        return prog().apply(l).get();
    }

    /** Parses contents of given string. */
    public static SExpr parse(final String s) {
        return parse(BufferedLexer.fromString(s));
    }

    // Primary productions.

    private static Rule prog() {
        return (l) -> anyOf(allOf(expr(), oneOf(TokenClass.END)), allOf(expr(), error()), error())
                .transform(SExpr::car)
                .apply(l);
    }

    private static Rule expr() {
        return (l) -> anyOf(oneOf(TokenClass.ATM), cons(), list(), quot())
                .apply(l);
    }

    private static Rule cons() {
        return (l) -> allOf(oneOf(TokenClass.PAL), expr(), oneOf(TokenClass.DOT), expr(), oneOf(TokenClass.DOT))
                .apply(l);
    }

    private static Rule list() {
        return (l) -> allOf(oneOf(TokenClass.PAL), manyOf(expr()), oneOf(TokenClass.PAR))
                .transform((s) -> s.cdr().car())
                .apply(l);
    }

    private static Rule quot() {
        return (l) -> allOf(oneOf(TokenClass.QUO), expr())
                .transform((s) -> new SExpr.Cons(SExpr.Atom.of("quote"), s.cdr()))
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
                    throw new ParserException(s.token(), ParserError.UNBALANCED_PAL);
                })
                .apply(l);
    }

    private static Rule errPar() {
        return (l) -> oneOf(TokenClass.PAR)
                .transform((s) -> {
                    throw new ParserException(s.token(), ParserError.UNBALANCED_PAR);
                })
                .apply(l);
    }

    private static Rule errQuo() {
        return (l) -> oneOf(TokenClass.QUO)
                .transform((s) -> {
                    throw new ParserException(s.token(), ParserError.DANGLING_QUOTE);
                })
                .apply(l);
    }

    private static Rule errAtm() {
        return (l) -> oneOf(TokenClass.ATM)
                .transform((s) -> {
                    throw new ParserException(s.token(), ParserError.DANGLING_ATOM);
                })
                .apply(l);
    }
}
