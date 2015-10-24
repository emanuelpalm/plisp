package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.lexer.BufferedLexer;
import io.github.emanuelpalm.plisp.lexer.Token;
import io.github.emanuelpalm.plisp.lexer.TokenClass;
import io.github.emanuelpalm.plisp.runtime.SExpr;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

import static io.github.emanuelpalm.util.testing.LexerUtils.lexerOf;
import static org.testng.Assert.assertEquals;

public class TestRule {
    @Test(dataProvider = "rules")
    public void shouldEvaluateRuleInto(final BufferedLexer l, final Rule r, final Optional<SExpr> expected) {
        assertEquals(r.apply(l), expected);
    }

    @DataProvider
    public Object[][] rules() {
        return new Object[][]{
                new Object[]{
                        lexerOf(),
                        Rule.oneOf(TokenClass.ATM),
                        Optional.empty()
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x")),
                        Rule.oneOf(TokenClass.ATM),
                        Optional.of(new SExpr.Atom("x"))
                },
                new Object[]{
                        lexerOf(),
                        Rule.allOf(Rule.oneOf(TokenClass.QUO), Rule.oneOf(TokenClass.ATM)),
                        Optional.empty()
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.QUO, "'")),
                        Rule.allOf(Rule.oneOf(TokenClass.QUO), Rule.oneOf(TokenClass.ATM)),
                        Optional.empty()
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "(")),
                        Rule.allOf(Rule.oneOf(TokenClass.QUO), Rule.oneOf(TokenClass.ATM)),
                        Optional.empty()
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.QUO, "'"), new Token(TokenClass.ATM, "x")),
                        Rule.allOf(Rule.oneOf(TokenClass.QUO), Rule.oneOf(TokenClass.ATM)),
                        Optional.of(SExpr.Cons.of(new SExpr.Atom("'"), new SExpr.Atom("x")))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAR, ")")),
                        Rule.manyOf(Rule.oneOf(TokenClass.ATM)),
                        Optional.of(SExpr.NUL)
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x")),
                        Rule.manyOf(Rule.oneOf(TokenClass.ATM)),
                        Optional.of(SExpr.Cons.of(new SExpr.Atom("x")))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x"), new Token(TokenClass.ATM, "y"), new Token(TokenClass.PAL, "(")),
                        Rule.manyOf(Rule.oneOf(TokenClass.ATM)),
                        Optional.of(SExpr.Cons.of(new SExpr.Atom("x"), new SExpr.Atom("y")))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAR, ")")),
                        Rule.anyOf(Rule.oneOf(TokenClass.ATM), Rule.oneOf(TokenClass.PAL)),
                        Optional.empty()
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "(")),
                        Rule.anyOf(Rule.oneOf(TokenClass.ATM), Rule.oneOf(TokenClass.PAL)),
                        Optional.of(new SExpr.Atom("("))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x"), new Token(TokenClass.PAL, "(")),
                        Rule.anyOf(Rule.oneOf(TokenClass.ATM), Rule.oneOf(TokenClass.PAL)),
                        Optional.of(new SExpr.Atom("x"))
                },
        };
    }
}
