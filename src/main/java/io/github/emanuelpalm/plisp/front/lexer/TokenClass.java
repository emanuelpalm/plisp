package io.github.emanuelpalm.plisp.front.lexer;

/**
 * A token class enumerator.
 */
public enum TokenClass {
    END, // End of input.
    ERR, // Error.
    NIL, // Nil.

    PAL, // (
    PAR, // )
    COL, // :
    BRL, // [
    BRR, // ]

    NUM, // Number.
    SYM  // Symbol.
}
