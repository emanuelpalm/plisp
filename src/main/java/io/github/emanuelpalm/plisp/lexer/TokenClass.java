package io.github.emanuelpalm.plisp.lexer;

/**
 * A token class enumerator.
 */
public enum TokenClass {
    END, // End of input.
    ERR, // Error.

    PAL, // (
    PAR, // )
    COL, // :
    BRL, // [
    BRR, // ]

    INT, // Integer.
    NUM, // Number.
    SYM  // Symbol.
}
