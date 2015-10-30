package io.github.emanuelpalm.plisp.lexer;

/**
 * Represents the origin of some token.
 */
public class TokenOrigin {
    /** Signifies that some token originates from some source of less relevance. */
    public static final TokenOrigin OTHER = new TokenOrigin(0, 0);

    private int row, column;

    /** Creates new token origin. */
    public TokenOrigin() {
        reset();
    }

    /** Creates a new token origin with given name, row and column. */
    public TokenOrigin(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    /** Adds single row and reset column. */
    public void addRow() {
        row += 1;
        column = 1;
    }

    /** Adds N to column. */
    public void addColumn(final int n) {
        column += n;
    }

    /** Resets origin row/column. */
    public void reset() {
        row = 1;
        column = 1;
    }

    /** Copies token origin object. */
    public TokenOrigin copy() {
        return new TokenOrigin(row, column);
    }

    @Override
    public String toString() {
        return row + ":" + column;
    }

    @Override
    public boolean equals(final Object that) {
        return that != null && that instanceof TokenOrigin
                && this.row == ((TokenOrigin) that).row
                && this.column == ((TokenOrigin) that).column;
    }
}
