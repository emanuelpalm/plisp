package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.util.ImmutableBinaryTree;

import java.util.HashMap;
import java.util.Optional;

/**
 * An abstract syntax {@link Tree} symbol table.
 */
public class TreeSymbolTable {
    private final HashMap<String, TreeNode> map;
    private final ImmutableBinaryTree<String, TreeNode> tree;

    /** Creates new empty symbol table. */
    public TreeSymbolTable() {
        map = new HashMap<>();
        tree = new ImmutableBinaryTree<>();
    }

    private TreeSymbolTable(final HashMap<String, TreeNode> map, final ImmutableBinaryTree<String, TreeNode> tree) {
        this.map = map;
        this.tree = tree;
    }

    /** Attempts to find symbol by given name. */
    public Optional<TreeNode> find(final String name) {
        return Optional.ofNullable(
                Optional.ofNullable(tree.get(name))
                        .orElse(map.get(name))
        );
    }

    /** Creates new symbol table also including given entry. */
    public TreeSymbolTable put(final TreeNode.Symbol name, final TreeNode value) {
        return new TreeSymbolTable(map, tree.put(name.name(), value));
    }

    /** Copies current symbol table and optimizes the copy for faster entry retrieval. */
    public TreeSymbolTable optimize() {
        final HashMap<String, TreeNode> m = new HashMap<>(map);
        tree.forEach(pair -> m.put(pair.first(), pair.second()));
        return new TreeSymbolTable(m, new ImmutableBinaryTree<>());
    }
}
