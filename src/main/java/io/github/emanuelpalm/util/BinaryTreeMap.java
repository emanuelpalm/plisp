package io.github.emanuelpalm.util;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * An immutable binary search tree map.
 * <p>
 * This particular implementation has an insertion and retrieval speed between O(log(n)) and O(n), depending on how
 * balanced the tree happens to become as new entries are inserted.
 */
public class BinaryTreeMap<K extends Comparable<K>, V> {
    private final Node<K, V> root;

    /** Creates new empty binary tree. */
    public BinaryTreeMap() {
        root = null;
    }

    private BinaryTreeMap(final Node<K, V> n) {
        root = n;
    }

    /** Creates new binary tree that includes given key and value. */
    public BinaryTreeMap<K, V> insert(final K key, final V value) {
        return new BinaryTreeMap<>(Node.insert(root, key, value));
    }

    /** Gets value associated with given key, or {@code null} in case the key couldn't be found. */
    public Optional<V> get(final K key) {
        return Node.search(root, key);
    }

    /** Calls given action with each binary tree key/value pair in order. */
    public void forEach(final Consumer<Pair<K, V>> action) {
        Node.traverse(root, action);
    }

    private static class Node<K extends Comparable<K>, V> {
        private final Node<K, V> left, right;
        private final K key;
        private final V value;

        private Node(final Node<K, V> l, final Node<K, V> r, final K k, final V v) {
            left = l;
            right = r;
            key = k;
            value = v;
        }

        static <K extends Comparable<K>, V> Node<K, V> insert(final Node<K, V> n, final K k, final V v) {
            if (n == null) {
                return new Node<>(null, null, k, v);
            }
            final int c = n.key.compareTo(k);
            if (c > 0) {
                return new Node<>(insert(n.left, k, v), n.right, n.key, n.value);
            } else if (c < 0) {
                return new Node<>(n.left, insert(n.right, k, v), n.key, n.value);
            }
            return new Node<>(n.left, n.right, k, v);
        }

        static <K extends Comparable<K>, V> Optional<V> search(final Node<K, V> n, final K k) {
            if (n == null) {
                return Optional.empty();
            }
            final int c = n.key.compareTo(k);
            if (c > 0) {
                return search(n.left, k);
            } else if (c < 0) {
                return search(n.right, k);
            }
            return Optional.of(n.value);
        }

        static <K extends Comparable<K>, V> void traverse(final Node<K, V> n, final Consumer<Pair<K, V>> a) {
            if (n.left != null) traverse(n.left, a);
            a.accept(Pair.of(n.key, n.value));
            if (n.right != null) traverse(n.right, a);
        }
    }
}
