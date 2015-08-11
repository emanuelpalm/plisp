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
                        .insertLocal("one", numberOf("10"))
                        .insertLocal("two", numberOf("20"))
                        .insertLocal("three", numberOf("30"))}
        };
    }

    @Test(dataProvider = "symbolTables")
    public void shouldFindValuesPreviouslyInserted(final TreeSymbolTable t0) {
        assertEquals(t0.search("one").get(), numberOf("10"));
        assertEquals(t0.search("two").get(), numberOf("20"));
        assertEquals(t0.search("three").get(), numberOf("30"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldMaskExistingLocalValueWhenInsertingNewWithSameKey(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0
                .insertLocal("one", numberOf("20"));

        assertEquals(t0.search("one").get(), numberOf("10"));
        assertEquals(t1.search("one").get(), numberOf("20"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldInsertGlobal(final TreeSymbolTable t) {
        t.insertGlobal("four", numberOf("40"));
        assertEquals(numberOf("40"), t.search("four").get());
    }


    @Test(dataProvider = "symbolTables")
    public void shouldAlwaysUseLocalValueBeforeGlobal(final TreeSymbolTable t) {
        t.insertGlobal("one", numberOf("1000"));
        assertEquals(numberOf("10"), t.search("one").get());
    }

    @Test(dataProvider = "symbolTables")
    public void shouldCreateNewTablesWhenInsertingLocalValues(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0.insertLocal(symbolOf("four"), numberOf("40"));
        final TreeSymbolTable t2 = t1.insertLocal(symbolOf("five"), numberOf("50"));

        assertFalse(t0.search("four").isPresent());
        assertFalse(t0.search("five").isPresent());

        assertEquals(t1.search("four").get(), numberOf("40"));
        assertFalse(t1.search("five").isPresent());

        assertEquals(t2.search("four").get(), numberOf("40"));
        assertEquals(t2.search("five").get(), numberOf("50"));
    }

    @Test(dataProvider = "symbolTables")
    public void shouldMutateAllRelatedTablesWhenInsertingGlobalValue(final TreeSymbolTable t0) {
        final TreeSymbolTable t1 = t0.insertLocal("four", numberOf("40"));

        assertFalse(t0.search("five").isPresent());
        assertFalse(t1.search("five").isPresent());

        t1.insertGlobal("five", numberOf("50"));

        assertEquals(t0.search("five").get(), numberOf("50"));
        assertEquals(t1.search("five").get(), numberOf("50"));
    }
}
