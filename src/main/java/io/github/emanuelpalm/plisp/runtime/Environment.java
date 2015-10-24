package io.github.emanuelpalm.plisp.runtime;

/**
 * Default execution environment.
 */
public class Environment {
    public static final SExpr.Atom QUOTE = SExpr.Atom.of("quote");

    /** Creates new default execution environment. */
    public static SExpr create() {
        return SExpr.Cons.of(
                entry(QUOTE, (args, env) -> args.car())
        );
    }

    private static SExpr entry(final SExpr.Atom name, final SExpr.Callable f) {
        return new SExpr.Cons(name, f);
    }
}
