package io.github.emanuelpalm.plisp.runtime;

/**
 * Evaluates s-expressions.
 */
public class Evaluator {
    /** Truth. */
    public static final SExpr T = SExpr.Atom.of("t");

    /** Falsity, which also happens to be the empty list. */
    public static final SExpr F = SExpr.NUL;

    /**
     * Evaluates given expression e.
     * <p>
     * The evaluator is significantly influenced by the description of LISP outlined in the paper "The Roots of Lisp"
     * written by Paul Graham.
     * <p>
     * The following are the elementary s-functions always available in the evaluator environment:
     * <pre>
     * QUOTE[e]     - Prevents e from being evaluated.
     * ATOM[e]      - Tests if e is an atom, as opposed to a cons.
     * EQ[e;f]      - Tests if e is equal to f.
     * CAR[x]       - Assuming x is a cons, gets the contents of its address register.
     * CDR[x]       - Assuming x is a cons, gets the contents of its decrement register.
     * CONS[x;y]    - Constructs new cons list containing the elements x and y.
     * COND[s]      - Returns CDR of first cons element in s whose CAR evaluates to t.
     * LAMBDA[x;e]* - Defines function which takes a list of arguments x and a expression body e.
     * LABEL[n;l]*  - Adds entry to environment containing name n and lambda (or constant) l.
     * </pre>
     * * LAMBDA and LABEL are meant to be used to effect the environment. Because of this, they are treated rather
     * differently than other functions. If the first argument of an evaluated list is a LAMBDA or LABEL call, that
     * argument is evaluated first, contrary to normal procedure, allowing it to change the environment before the
     * second argument is evaluated.
     * <p>
     * Example:
     * <pre>
     * (
     *     (label cadr (lambda (x) (car (cdr x))))
     *     (cadr '(a b c))
     * )
     * </pre>
     *
     * @see <a href="http://www.paulgraham.com/rootsoflisp.html">The Roots of Lisp - Paul Graham, January 2002</a>
     */
    public static SExpr eval(final SExpr e) {
        return eval(e, SExpr.NUL);
    }

    /**
     * Evaluates expression e using environment a.
     * <p>
     * The environment has to be a list, with atom/lambda pairs as elements.
     * <p>
     * Example:
     * <pre>
     * (
     *     ('caar . (lambda (x) (car (car x))))
     *     ('cadr . (lambda (x) (car (cdr x))))
     * )
     * </pre>
     */
    private static SExpr eval(final SExpr e, final SExpr a) {
        if (e instanceof SExpr.Atom) {
            return assoc(e, a);
        }
        if (e.car() instanceof SExpr.Atom) {
            switch (((SExpr.Atom) e.car()).name()) {
                case "quote":
                    return quote(e);

                case "atom":
                    return atom(e, a);

                case "eq":
                    return eq(e, a);

                case "car":
                    return car(e, a);

                case "cdr":
                    return cdr(e, a);

                case "cons":
                    return cons(e, a);

                case "cond":
                    return cond(e, a);

                default:
                    return eval(new SExpr.Cons(assoc(e.car(), a), e.cdr()), a);
            }
        }
        if (e.car().car() instanceof SExpr.Atom) {
            switch (((SExpr.Atom) e.car().car()).name()) {
                case "lambda":
                    return lambda(e, a);

                case "label":
                    return label(e, a);
            }
        }
        throw new IllegalStateException("Illegal expression: " + e);
    }

    /** Gets lambda associated with atom e in environment a. */
    private static SExpr assoc(final SExpr e, final SExpr a) {
        if (a instanceof SExpr.Nul) {
            throw new IllegalStateException("Atom not in environment: " + e);
        }
        if (a.car().car().equals(e)) {
            return a.car().cdr();
        }
        return assoc(e, a.cdr());
    }

    /** Evaluates some quote expression e. */
    private static SExpr quote(final SExpr e) {
        return e.cdr().car();
    }

    /** Evaluates some atom verification expression e using environment a. */
    private static SExpr atom(final SExpr e, final SExpr a) {
        return eval(e.cdr().car(), a) instanceof SExpr.Cons ? F : T;
    }

    /** Evaluates some equality expression e using environment a. */
    private static SExpr eq(final SExpr e, final SExpr a) {
        final SExpr e0 = eval(e.cdr().car(), a);
        final SExpr e1 = eval(e.cdr().cdr().car(), a);
        return e0.equals(e1) ? T : F;
    }

    /** Evaluates some car (Contents of Address Register) expression e using environment a. */
    private static SExpr car(final SExpr e, final SExpr a) {
        return eval(e.cdr().car(), a).car();
    }

    /** Evaluates some cdr (Contents of Decrement Register) expression e using environment a. */
    private static SExpr cdr(final SExpr e, final SExpr a) {
        return eval(e.cdr().car(), a).cdr();
    }

    /** Evaluates some cons expression e using environment a. */
    private static SExpr cons(final SExpr e, final SExpr a) {
        final SExpr e0 = eval(e.cdr().car(), a);
        final SExpr e1 = eval(e.cdr().cdr().car(), a);
        return new SExpr.Cons(e0, e1);
    }

    /** Evaluates some condition expression e using environment a. */
    private static SExpr cond(final SExpr e, final SExpr a) {
        if (e instanceof SExpr.Nul) {
            throw new IllegalStateException("No successful condition.");
        }
        if (eval(e.cdr().car().car(), a).equals(T)) {
            return eval(e.cdr().car().cdr().car(), a);
        }
        return cond(e.cdr(), a);
    }

    /** Evaluates some label expression e using environment a. */
    private static SExpr label(final SExpr e, final SExpr a) {
        final SExpr label = e.car().cdr().car();
        final SExpr lambda = e.car().cdr().cdr().car();
        return eval(
                e.cdr().car(),
                new SExpr.Cons(new SExpr.Cons(label, lambda), a)
        );
    }

    /** Evaluates some lambda expression e using environment a. */
    private static SExpr lambda(final SExpr e, final SExpr a) {
        return eval(
                e.car().cdr().cdr().car(),
                e.car().cdr().car().zip(evlis(e.cdr(), a)).concat(a)
        );
    }

    /** Evaluates some list of expressions e using environment a. */
    private static SExpr evlis(final SExpr e, final SExpr a) {
        if (e instanceof SExpr.Nul) {
            return e;
        }
        return new SExpr.Cons(eval(e.car(), a), evlis(e.cdr(), a));
    }
}
