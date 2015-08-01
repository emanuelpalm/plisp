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
                        .put(symNode("one"), intNode("10"))
                        .put(symNode("two"), intNode("20"))
                        .put(symNode("three"), intNode("30"))},
                new Object[]{new TreeSymbolTable()
                        .put(symNode("one"), intNode("10"))
                        .put(symNode("two"), intNode("20"))
                        .put(symNode("three"), intNode("30"))
                        .optimize()}
        };
    }

    private static TreeNode.Symbol symNode(final String lexeme) {
        return new TreeNode.Symbol(new Token(new TokenOrigin("test", 1, 0), TokenClass.SYM, lexeme));
    }

    private static TreeNode.Integer intNode(final String lexeme) {
        return new TreeNode.Integer(new Token(new TokenOrigin("test", 1, 0), TokenClass.INT, lexeme));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldFindValuesPreviouslyPut(final TreeSymbolTable t0) {
        assertEquals(t0.find("one").get(), intNode("10"));
        assertEquals(t0.find("two").get(), intNode("20"));
        assertEquals(t0.find("three").get(), intNode("30"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldReplaceExistingValueAssociatedWithSameKeyWhenUsingPut(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0
                .put(symNode("one"), intNode("20"));

        assertEquals(t0.find("one").get(), intNode("10"));
        assertEquals(t1.find("one").get(), intNode("20"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldCreateNewTablesWhenPuttingNewValues(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0.put(symNode("four"), intNode("40"));
        final TreeSymbolTable t2 = t1.put(symNode("five"), intNode("50"));

        assertFalse(t0.find("four").isPresent());
        assertFalse(t0.find("five").isPresent());

        assertEquals(t1.find("four").get(), intNode("40"));
        assertFalse(t1.find("five").isPresent());

        assertEquals(t2.find("four").get(), intNode("40"));
        assertEquals(t2.find("five").get(), intNode("50"));
    }
}
