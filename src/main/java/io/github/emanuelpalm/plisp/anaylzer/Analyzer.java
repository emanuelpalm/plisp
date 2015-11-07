package io.github.emanuelpalm.plisp.anaylzer;

import io.github.emanuelpalm.plisp.runtime.SExpr;

import java.util.Map;

/**
 * Analyzes some {@link SExpr}.
 */
public class Analyzer {
    /**
     * Analyzes given s-expression environment usage.
     * <p>
     * Makes sure only defined labels are used, and that correct argument amounts are provided when using them.
     */
    public static void checkEnvironmentOf(final SExpr e) throws AnalyzerException {
        checkEnvironmentOf(e, Prototypes.defaults());
    }

    private static void checkEnvironmentOf(final SExpr e, final Map<String, Prototype> a) throws AnalyzerException {
        if (e instanceof SExpr.Nil) {
            return;
        }
        if (e instanceof SExpr.Atom) {
            checkAtomDefinedIn((SExpr.Atom) e, a);

        } else if (e.car() instanceof SExpr.Atom) {
            checkExpressionOperatorUsageAgainst(e, a);

        } else if (e.car().car() instanceof SExpr.Atom) {
            switch (((SExpr.Atom) e.car().car()).name()) {
                case "label":
                    checkLabelExpressionUsageAgainst(e, a);
                    break;

                case "lambda":
                    checkLambdaExpressionUsageAgainst(e, a);
                    break;
            }
        } else {
            throw new AnalyzerException("Illegal expression.", e);
        }
    }

    private static void checkAtomDefinedIn(final SExpr.Atom e, final Map<String, Prototype> a) throws AnalyzerException.NotDefined {
        if (!a.containsKey(e.toString())) {
            throw new AnalyzerException.NotDefined(e);
        }
    }

    private static void checkExpressionOperatorUsageAgainst(final SExpr e, final Map<String, Prototype> a) throws AnalyzerException {
        final SExpr.Atom operator = (SExpr.Atom) e.car();
        final SExpr arguments = e.cdr();

        if (operator.name().equals("quote")) {
            return;
        }
        final Prototype prototype = a.get(operator.name());
        if (prototype == null) {
            throw new AnalyzerException.NotDefined(operator);
        }
        if (prototype.size().isPresent() && arguments.size() != prototype.size().get()) {
            throw new AnalyzerException.PrototypeMismatch(e, prototype);
        }
        checkExpressionOperatorArgumentsAgainst(e.cdr(), a);
    }

    private static void checkExpressionOperatorArgumentsAgainst(final SExpr e, final Map<String, Prototype> a) throws AnalyzerException {
        if (e instanceof SExpr.Nil) {
            return;
        }
        checkEnvironmentOf(e.car(), a);
        checkExpressionOperatorArgumentsAgainst(e.cdr(), a);
    }

    private static void checkLabelExpressionUsageAgainst(final SExpr e, final Map<String, Prototype> a) throws AnalyzerException {
        if (Prototypes.LABEL.size().get() != e.car().cdr().size()) {
            throw new AnalyzerException.PrototypeMismatch(e.car(), Prototypes.LABEL);
        }
        final SExpr name = e.car().cdr().car();
        a.put(name.toString(), Prototype.fromLabelExpression(e.car()));

        checkEnvironmentOf(e.cdr().car(), a);

        addLambdaExpressionArgumentsTo(e.car().cdr().cdr().car().cdr().car(), a);
        checkEnvironmentOf(e.car().cdr().cdr().car().cdr().cdr().car(), a);
    }

    private static void checkLambdaExpressionUsageAgainst(final SExpr e, final Map<String, Prototype> a) throws AnalyzerException {
        if (Prototypes.LAMBDA.size().get() != e.car().cdr().size()) {
            throw new AnalyzerException.PrototypeMismatch(e.car(), Prototypes.LAMBDA);
        }
        final int argumentCount = e.cdr().size();
        final int parameterCount = e.car().cdr().car().size();
        if (argumentCount != parameterCount) {
            throw new AnalyzerException.LambdaMisuse(e.car());
        }

        addLambdaExpressionArgumentsTo(e.car().cdr().car(), a);
        checkEnvironmentOf(e.car().cdr().cdr().car(), a);

        checkEnvironmentOf(e.cdr().car(), a);
    }

    private static void addLambdaExpressionArgumentsTo(final SExpr e, final Map<String, Prototype> a) {
        if (e instanceof SExpr.Nil) {
            return;
        }
        a.put(e.car().toString(), Prototype.fromLambdaParameter(e.car()));
        addLambdaExpressionArgumentsTo(e.cdr(), a);
    }
}
