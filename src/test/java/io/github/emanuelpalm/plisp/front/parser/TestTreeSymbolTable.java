package io.github.emanuelpalm.plisp.front.parser;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.emanuelpalm.util.testing.TreeUtils.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class TestTreeSymbolTable {
    @DataProvider
    public static Object[][] symbolTables() throws Throwable {
        return new Object[][]{
                new Object[]{new TreeSymbolTable()
                        .insertLocal("one", integerOf("10"))
                        .insertLocal("two", integerOf("20"))
                        .insertLocal("three", integerOf("30"))}
        };
    }

    @Test(dataProvider = "symbolTables")
    public void shouldFindValuesPreviouslyInserted(final TreeSymbolTable t0) {
        assertEquals(t0.search("one").get(), integerOf("10"));
        assertEquals(t0.search("two").get(), integerOf("20"));
        assertEquals(t0.search("three").get(), integerOf("30"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldMaskExistingLocalValueWhenInsertingNewWithSameKey(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0
                .insertLocal("one", integerOf("20"));

        assertEquals(t0.search("one").get(), integerOf("10"));
        assertEquals(t1.search("one").get(), integerOf("20"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldCreateNewTablesWhenInsertingLocalValues(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0.insertLocal(symbolOf("four"), integerOf("40"));
        final TreeSymbolTable t2 = t1.insertLocal(symbolOf("five"), integerOf("50"));

        assertFalse(t0.search("four").isPresent());
        assertFalse(t0.search("five").isPresent());

        assertEquals(t1.search("four").get(), integerOf("40"));
        assertFalse(t1.search("five").isPresent());

        assertEquals(t2.search("four").get(), integerOf("40"));
        assertEquals(t2.search("five").get(), integerOf("50"));
    }
}
