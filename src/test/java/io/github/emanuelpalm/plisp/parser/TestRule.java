package io.github.emanuelpalm.plisp.parser;

import io.github.emanuelpalm.plisp.lexer.TokenBuffer;
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
    public void shouldEvaluateRuleInto(final TokenBuffer buffer, final Rule r, final Optional<SExpr> expected) {
        assertEquals(r.apply(buffer), expected);
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
                        Optional.of(SExpr.Atom.of("x"))
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
                        Optional.of(SExpr.Cons.of(SExpr.Atom.of("'"), SExpr.Atom.of("x")))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAR, ")")),
                        Rule.manyOf(Rule.oneOf(TokenClass.ATM)),
                        Optional.of(SExpr.NIL)
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x")),
                        Rule.manyOf(Rule.oneOf(TokenClass.ATM)),
                        Optional.of(SExpr.Cons.of(SExpr.Atom.of("x")))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x"), new Token(TokenClass.ATM, "y"), new Token(TokenClass.PAL, "(")),
                        Rule.manyOf(Rule.oneOf(TokenClass.ATM)),
                        Optional.of(SExpr.Cons.of(SExpr.Atom.of("x"), SExpr.Atom.of("y")))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAR, ")")),
                        Rule.anyOf(Rule.oneOf(TokenClass.ATM), Rule.oneOf(TokenClass.PAL)),
                        Optional.empty()
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.PAL, "(")),
                        Rule.anyOf(Rule.oneOf(TokenClass.ATM), Rule.oneOf(TokenClass.PAL)),
                        Optional.of(SExpr.Atom.of("("))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "x"), new Token(TokenClass.PAL, "(")),
                        Rule.anyOf(Rule.oneOf(TokenClass.ATM), Rule.oneOf(TokenClass.PAL)),
                        Optional.of(SExpr.Atom.of("x"))
                },
                new Object[]{
                        lexerOf(new Token(TokenClass.ATM, "keyword")),
                        Rule.anyOf(Rule.oneOf("keyword")),
                        Optional.of(SExpr.Atom.of("keyword"))
                },
        };
    }
}
