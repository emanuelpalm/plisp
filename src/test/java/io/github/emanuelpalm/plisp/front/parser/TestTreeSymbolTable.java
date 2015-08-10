package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.Token;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;
import io.github.emanuelpalm.plisp.front.lexer.TokenOrigin;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class TestTreeSymbolTable {
    @DataProvider
    public static Object[][] symbolTables() throws Throwable {
        return new Object[][]{
                new Object[]{new TreeSymbolTable()
                        .insertLocal(symNode("one"), intNode("10"))
                        .insertLocal(symNode("two"), intNode("20"))
                        .insertLocal(symNode("three"), intNode("30"))}
        };
    }

    private static TreeNode.Symbol symNode(final String lexeme) {
        return TreeNode.Symbol.of(new Token(new TokenOrigin("test", 1, 0), TokenClass.SYM, lexeme));
    }

    private static TreeNode.Integer intNode(final String lexeme) {
        return TreeNode.Integer.of(new Token(new TokenOrigin("test", 1, 0), TokenClass.INT, lexeme));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldFindValuesPreviouslyPut(final TreeSymbolTable t0) {
        assertEquals(t0.search("one").get(), intNode("10"));
        assertEquals(t0.search("two").get(), intNode("20"));
        assertEquals(t0.search("three").get(), intNode("30"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldReplaceExistingValueAssociatedWithSameKeyWhenUsingPut(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0
                .insertLocal(symNode("one"), intNode("20"));

        assertEquals(t0.search("one").get(), intNode("10"));
        assertEquals(t1.search("one").get(), intNode("20"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldCreateNewTablesWhenPuttingNewValues(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0.insertLocal(symNode("four"), intNode("40"));
        final TreeSymbolTable t2 = t1.insertLocal(symNode("five"), intNode("50"));

        assertFalse(t0.search("four").isPresent());
        assertFalse(t0.search("five").isPresent());

        assertEquals(t1.search("four").get(), intNode("40"));
        assertFalse(t1.search("five").isPresent());

        assertEquals(t2.search("four").get(), intNode("40"));
        assertEquals(t2.search("five").get(), intNode("50"));
    }
}
