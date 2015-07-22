package io.github.emanuelpalm.plisp.lexer;

/**
 * Represents the origin of some token.
 */
public class TokenOrigin {
    /** Name of token origin. */
    public final String name;

    /** Origin row. */
    public int row;

    /** Origin column. */
    public int column;

    /** Creates new token origin with given name. */
    public TokenOrigin(final String name) {
        this.name = name;
        reset();
    }

    /** Creates a new token origin with given name, row and column. */
    public TokenOrigin(final String name, final int row, final int column) {
        this.name = name;
        this.row = row;
        this.column = column;
    }

    /** Resets origin row/column. */
    public void reset() {
        row = 1;
        column = 0;
    }

    /** Copies token origin object. */
    public TokenOrigin copy() {
        return new TokenOrigin(name, row, column);
    }

    @Override
    public String toString() {
        return name + ":" + row + ":" + column;
    }

    @Override
    public boolean equals(final Object that) {
        return that != null && that instanceof TokenOrigin
                && this.name.equals(((TokenOrigin) that).name)
                && this.row == ((TokenOrigin) that).row
                && this.column == ((TokenOrigin) that).column;
    }
}
