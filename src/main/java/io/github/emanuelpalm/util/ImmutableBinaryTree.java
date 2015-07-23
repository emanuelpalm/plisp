package io.github.emanuelpalm.util;

/**
 * An immutable binary tree.
 * <p>
 * This particular implementation has an insertion and retrieval speed between O(log(n)) and O(n), depending on how
 * balanced the tree happens to become as new entries are inserted.
 * <p>
 * The tree implementation is written for the sole purpose of serving as local symbol table. Most programs are expected
 * to contain relatively few local symbols at any given scope, which means that the need for tree balancing should be
 * low, if not negligible.
 */
public class ImmutableBinaryTree<K extends Comparable<K>, V> {
    private final Node<K, V> root;

    /** Creates new empty binary tree. */
    public ImmutableBinaryTree() {
        root = null;
    }

    private ImmutableBinaryTree(final Node<K, V> n) {
        root = n;
    }

    /** Creates new binary tree that includes given key and value. */
    public ImmutableBinaryTree<K, V> put(final K key, final V value) {
        return new ImmutableBinaryTree<>(Node.insert(root, key, value));
    }

    /** Gets value associated with given key, or {@code null} in case the key couldn't be found. */
    public V get(final K key) {
        return Node.search(root, key);
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
            if (c < 0) {
                return new Node<>(insert(n.left, k, v), n.right, n.key, n.value);
            } else if (c > 0) {
                return new Node<>(n.left, insert(n.right, k, v), n.key, n.value);
            }
            return new Node<>(n.left, n.right, k, v);
        }

        static <K extends Comparable<K>, V> V search(final Node<K, V> n, final K k) {
            if (n == null) {
                return null;
            }
            final int c = n.key.compareTo(k);
            if (c < 0) {
                return search(n.left, k);
            } else if (c > 0) {
                return search(n.right, k);
            }
            return n.value;
        }
    }
}
