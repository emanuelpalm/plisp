package io.github.emanuelpalm.plisp.lexer;

/**
 * A token class enumerator.
 */
public enum TokenClass {
    END, // End of input.
    ERR, // Error.

    QUO, // '
    PAL, // (
    PAR, // )
    DOT, // .

    ATM  // Atom.
}
