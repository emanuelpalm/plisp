package io.github.emanuelpalm.util;

/**
 * A pair of two arbitrarily typed values.
 */
public class Pair<A, B> {
    private final A first;
    private final B second;

    private Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }

    /** Creates new pair from given values. */
    public static <A, B> Pair<A, B> of(final A first, final B second) {
        return new Pair<>(first, second);
    }

    /** First value. */
    public A first() {
        return first;
    }

    /** Second value. */
    public B second() {
        return second;
    }
}
