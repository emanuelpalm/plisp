package io.github.emanuelpalm.plisp.runtime;

/**
 * Default execution environment.
 */
public class Environment {
    public static final SExpr.Atom F = SExpr.Atom.of("f");
    public static final SExpr.Atom T = SExpr.Atom.of("t");

    public static final SExpr.Atom ATOM = SExpr.Atom.of("atom");
    public static final SExpr.Atom CAR = SExpr.Atom.of("car");
    public static final SExpr.Atom CDR = SExpr.Atom.of("cdr");
    public static final SExpr.Atom COND = SExpr.Atom.of("cond");
    public static final SExpr.Atom CONS = SExpr.Atom.of("cons");
    public static final SExpr.Atom EQ = SExpr.Atom.of("eq");
    public static final SExpr.Atom QUOTE = SExpr.Atom.of("quote");

    /** Creates new default execution environment. */
    public static SExpr create() {
        return SExpr.Cons.of(
                entry(QUOTE, (args, env) -> args.car()),
                entry(ATOM, (args, env) -> (args.car() instanceof SExpr.Atom) ? T : F),
                entry(EQ, (args, env) -> args.car().equals(args.cdr().car()) ? T : F),
                entry(CAR, (args, env) -> args.car().car()),
                entry(CDR, (args, env) -> args.car().cdr()),
                entry(CONS, (args, env) -> args),
                entry(COND, (args, env) -> {
                    final SExpr a = args.car();
                    args = args.cdr().car();
                    while (true) {
                        final SExpr s = args.car();
                        if (a.equals(s)) {
                            return T;
                        }
                        if (s instanceof SExpr.Nul) {
                            return F;
                        }
                        args = args.cdr();
                    }
                })
        );
    }

    private static SExpr entry(final SExpr.Atom name, final SExpr.Callable f) {
        return new SExpr.Cons(name, f);
    }
}
