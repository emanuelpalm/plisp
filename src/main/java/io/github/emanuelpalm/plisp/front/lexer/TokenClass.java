package io.github.emanuelpalm.plisp.front.lexer;

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
    PIP, // |

    INT, // Integer.
    NUM, // Number.
    SYM  // Symbol.
}
