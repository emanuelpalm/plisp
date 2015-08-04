package io.github.emanuelpalm.util;

import org.testng.annotations.Test;

import java.util.LinkedList;

import static org.testng.Assert.*;

public class TestImmutableBinaryTree {
    @Test
    public void shouldReturnNullWhenGettingFromEmptyTree() {
        final BinaryTreeMap<String, Integer> t = new BinaryTreeMap<>();
        assertFalse(t.search("key").isPresent());
    }

    @Test
    public void shouldAlwaysPutEntryIntoNewTree() {
        final BinaryTreeMap<String, Integer> t0 = new BinaryTreeMap<>();
        final BinaryTreeMap<String, Integer> t1 = t0.insert("k1", 100);
        final BinaryTreeMap<String, Integer> t2 = t1.insert("k2", 200);

        assertFalse(t0.search("k1").isPresent());
        assertFalse(t0.search("k2").isPresent());

        assertEquals(100, (int) t1.search("k1").get());
        assertFalse(t1.search("k2").isPresent());

        assertEquals(100, (int) t2.search("k1").get());
        assertEquals(200, (int) t2.search("k2").get());
    }

    @Test
    public void shouldReplaceValueInNewTreeIfPutMatchesExistingKey() {
        final BinaryTreeMap<String, Integer> t0 = new BinaryTreeMap<String, Integer>()
                .insert("ape", 100)
                .insert("badger", 200)
                .insert("centipede", 300);

        final BinaryTreeMap<String, Integer> t1 = t0.insert("badger", 222);

        assertEquals(200, (int) t0.search("badger").get());
        assertEquals(222, (int) t1.search("badger").get());
    }

    @Test
    public void shouldVisitAllValuesInOrder() {
        final BinaryTreeMap<String, Integer> t = new BinaryTreeMap<String, Integer>()
                .insert("badger", 200)
                .insert("centipede", 300)
                .insert("ape", 100);

        final LinkedList<Pair<String, Integer>> q = new LinkedList<>();
        t.forEach((k, v) -> q.add(Pair.of(k, v)));

        assertEquals(q.get(0).first(), "ape");
        assertEquals(q.get(1).first(), "badger");
        assertEquals(q.get(2).first(), "centipede");

        assertEquals((int) q.get(0).second(), 100);
        assertEquals((int) q.get(1).second(), 200);
        assertEquals((int) q.get(2).second(), 300);
    }
}
