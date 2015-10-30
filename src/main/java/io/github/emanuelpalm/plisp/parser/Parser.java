package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.lexer.TokenBuffer;
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
    public static SExpr parse(final TokenBuffer b) {
        return prog().apply(b).get();
    }

    /** Parses contents of given string. */
    public static SExpr parse(final String s) {
        return parse(TokenBuffer.fromString(s));
    }

    // Primary productions.

    private static Rule prog() {
        return (buffer) -> anyOf(allOf(expr(), oneOf(TokenClass.END)), allOf(expr(), error()), error())
                .transform(SExpr::car)
                .apply(buffer);
    }

    private static Rule expr() {
        return (buffer) -> anyOf(oneOf(TokenClass.ATM), cons(), list(), quot())
                .apply(buffer);
    }

    private static Rule cons() {
        return (buffer) -> allOf(oneOf(TokenClass.PAL), expr(), oneOf(TokenClass.DOT), expr(), oneOf(TokenClass.DOT))
                .apply(buffer);
    }

    private static Rule list() {
        return (buffer) -> allOf(oneOf(TokenClass.PAL), manyOf(expr()), oneOf(TokenClass.PAR))
                .transform((s) -> s.cdr().car())
                .apply(buffer);
    }

    private static Rule quot() {
        return (buffer) -> allOf(oneOf(TokenClass.QUO), expr())
                .transform((s) -> new SExpr.Cons(SExpr.Atom.of("quote"), s.cdr()))
                .apply(buffer);
    }

    // Error productions.

    private static Rule error() {
        return (buffer) -> anyOf(errPal(), errPar(), errQuo(), errAtm())
                .apply(buffer);
    }

    private static Rule errPal() {
        return (buffer) -> oneOf(TokenClass.PAL)
                .transform((s) -> {
                    throw new ParserException.UnbalancedOpeningParenthesis(s.token());
                })
                .apply(buffer);
    }

    private static Rule errPar() {
        return (buffer) -> oneOf(TokenClass.PAR)
                .transform((s) -> {
                    throw new ParserException.UnbalancedClosingParenthesis(s.token());
                })
                .apply(buffer);
    }

    private static Rule errQuo() {
        return (buffer) -> oneOf(TokenClass.QUO)
                .transform((s) -> {
                    throw new ParserException.DanglingQuote(s.token());
                })
                .apply(buffer);
    }

    private static Rule errAtm() {
        return (buffer) -> oneOf(TokenClass.ATM)
                .transform((s) -> {
                    throw new ParserException.DanglingAtom(s.token());
                })
                .apply(buffer);
    }
}
