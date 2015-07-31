package io.github.emanuelpalm.util;

import org.testng.annotations.Test;

import java.util.LinkedList;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class TestImmutableBinaryTree {
    @Test
    public void shouldReturnNullWhenGettingFromEmptyTree() {
        final ImmutableBinaryTree<String, Integer> t = new ImmutableBinaryTree<>();
        assertNull(t.get("key"));
    }

    @Test
    public void shouldAlwaysPutEntryIntoNewTree() {
        final ImmutableBinaryTree<String, Integer> t0 = new ImmutableBinaryTree<>();
        final ImmutableBinaryTree<String, Integer> t1 = t0.put("k1", 100);
        final ImmutableBinaryTree<String, Integer> t2 = t1.put("k2", 200);

        assertNull(t0.get("k1"));
        assertNull(t0.get("k2"));

        assertEquals(100, (int) t1.get("k1"));
        assertNull(t1.get("k2"));

        assertEquals(100, (int) t2.get("k1"));
        assertEquals(200, (int) t2.get("k2"));
    }

    @Test
    public void shouldReplaceValueInNewTreeIfPutMatchesExistingKey() {
        final ImmutableBinaryTree<String, Integer> t0 = new ImmutableBinaryTree<String, Integer>()
                .put("ape", 100)
                .put("badger", 200)
                .put("centipede", 300);

        final ImmutableBinaryTree<String, Integer> t1 = t0.put("badger", 222);

        assertEquals(200, (int) t0.get("badger"));
        assertEquals(222, (int) t1.get("badger"));
    }

    @Test
    public void shouldVisitAllValuesInOrder() {
        final ImmutableBinaryTree<String, Integer> t = new ImmutableBinaryTree<String, Integer>()
                .put("badger", 200)
                .put("centipede", 300)
                .put("ape", 100);

        final LinkedList<Pair<String, Integer>> q = new LinkedList<>();
        t.forEach(q::add);

        assertEquals(q.get(0).first(), "ape");
        assertEquals(q.get(1).first(), "badger");
        assertEquals(q.get(2).first(), "centipede");

        assertEquals((int) q.get(0).second(), 100);
        assertEquals((int) q.get(1).second(), 200);
        assertEquals((int) q.get(2).second(), 300);
    }
}
