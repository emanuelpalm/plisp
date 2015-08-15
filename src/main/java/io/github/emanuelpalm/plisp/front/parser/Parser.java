package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.front.lexer.Token;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Parses the output of a {@link BufferedLexer} into an abstract syntax tree.
 * <p>
 * The parser adheres to the following grammar:
 * <pre>
 * EXPR  ->  _NUM | _SYM | LIST
 * LIST  -> "(" EXPR* ")"
 * </pre>
 */
public class Parser {
    private final BufferedLexer lexer;

    private final Rule _PAL = _expect(TokenClass.PAL);
    private final Rule _PAR = _expect(TokenClass.PAR);
    private final Rule _NUM = _expect(TokenClass.NUM, TreeNode.Number::of);
    private final Rule _SYM = _expect(TokenClass.SYM, TreeNode.Symbol::of);

    /** Creates new parser, parsing contents of given buffered lexer. */
    public Parser(final BufferedLexer l) {
        lexer = l;
    }

    private Rule _expect(final TokenClass c) {
        return _expect(c, TreeNode.Void::of);
    }

    private Rule _expect(final TokenClass c, final Function<Token, TreeNode> f) {
        return () -> {
            final Token t = lexer.next();
            return (t.type() == c)
                    ? Optional.of(f.apply(t))
                    : Optional.empty();
        };
    }

    /** Runs parser, producing an abstract syntax tree */
    public TreeNode run() {
        return expr().apply().get();
    }

    private Rule expr() {
        return () -> _oneOf(_NUM, _SYM, list())
                .apply();
    }

    private Rule list() {
        return () -> _allOf(_PAL, _manyOf(expr()), _PAR)
                .thenCombine((ns) -> TreeNode.List.of(ns.get(0).token(), (TreeNode.List) ns.get(1)))
                .apply();
    }

    private Rule _oneOf(final Rule... rs) {
        return () -> {
            for (final Rule r : rs) {
                final Optional<TreeNode> n = __try(r);
                if (n.isPresent()) {
                    return Optional.of(n.get());
                }
            }
            return Optional.empty();
        };
    }

    private Rule _manyOf(final Rule r) {
        return () -> {
            final ArrayList<TreeNode> ns = new ArrayList<>(3);
            while (true) {
                final Optional<TreeNode> n = __try(r);
                if (!n.isPresent()) break;
                ns.add(n.get());
            }
            return Optional.of(TreeNode.List.of(ns));
        };
    }

    private CombinationRule _allOf(final Rule... rs) {
        return (f) -> {
            final ArrayList<TreeNode> ns = new ArrayList<>(rs.length);
            for (final Rule r : rs) {
                final Optional<TreeNode> n = r.apply();
                if (n.isPresent()) {
                    ns.add(n.get());

                } else {
                    return Optional::empty;
                }
            }
            return () -> Optional.of(f.apply(ns));
        };
    }

    private Optional<TreeNode> __try(final Rule r) {
        final int s = lexer.state();

        final Optional<TreeNode> n = r.apply();
        if (n.isPresent()) return n;

        lexer.restore(s);
        return Optional.empty();
    }

    /**
     * A function used to apply some parser rule.
     */
    private interface Rule {
        Optional<TreeNode> apply();
    }

    /**
     * A function used to combine the results of several rules into one new rule.
     */
    private interface CombinationRule {
        Rule thenCombine(final Function<List<TreeNode>, TreeNode> f);
    }
}
