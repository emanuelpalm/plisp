package io.github.emanuelpalm.plisp.runtime;

/**
 * Default execution environment.
 */
public class Environment {
    public static final SExpr.Atom F = SExpr.Atom.of("f");
    public static final SExpr.Atom T = SExpr.Atom.of("t");

    public static final SExpr.Atom ATOM = SExpr.Atom.of("atom");
    public static final SExpr.Atom EQ = SExpr.Atom.of("eq");
    public static final SExpr.Atom QUOTE = SExpr.Atom.of("quote");

    /** Creates new default execution environment. */
    public static SExpr create() {
        return SExpr.Cons.of(
                entry(QUOTE, (args, env) -> args.car()),
                entry(ATOM, (args, env) -> (args.car() instanceof SExpr.Atom) ? T : F),
                entry(EQ, (args, env) -> args.car().equals(args.cdr().car()) ? T : F)
        );
    }

    private static SExpr entry(final SExpr.Atom name, final SExpr.Callable f) {
        return new SExpr.Cons(name, f);
    }
}
