package io.github.emanuelpalm.plisp.anaylzer;

import io.github.emanuelpalm.plisp.lexer.Token;
import io.github.emanuelpalm.plisp.runtime.SExpr;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Describes some expression available in a runtime environment.
 */
public class Prototype implements Comparable<Prototype> {
    private final String name;
    private final Optional<Integer> size;

    /** Creates new prototype with given name and parameter count. */
    public Prototype(final String name, final Integer size) {
        this.name = name;
        this.size = Optional.ofNullable(size);
    }

    /** Creates new prototype from expression on the form "(label name expression)". */
    public static Prototype fromLabelExpression(final SExpr e) {
        if (e.cdr().cdr().car().car().token().orElse(Token.END).lexeme().equals("lambda")) {
            return new Prototype(e.cdr().car().toString(), e.cdr().cdr().car().cdr().car().size());
        }
        return new Prototype(e.cdr().car().toString(), 0);
    }

    /** Creates new prototype from a lone lambda parameter. */
    public static Prototype fromLambdaParameter(final SExpr e) {
        return new Prototype(e.toString(), 0);
    }

    /** Amount of parameters required, unless variadic, when calling expression. */
    public Optional<Integer> size() {
        return size;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("");
        if (!size.isPresent()) {
            builder.append("...");

        } else if (size.get() > 0) {
            builder
                    .append("[")
                    .append(IntStream.range(0, size.get())
                            .mapToObj(value -> String.valueOf((char) (97 + value)))
                            .collect(Collectors.joining(";")))
                    .append("]");
        }
        return name.toUpperCase(Locale.ROOT) + builder;
    }

    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof Prototype
                && name.equals(((Prototype) o).name)
                && size.equals(((Prototype) o).size);
    }

    @Override
    public int compareTo(final Prototype o) {
        final int diff = name.compareTo(o.name);
        if (diff != 0) {
            return diff;
        }
        return size.orElse(0) - o.size.orElse(0);
    }
}
