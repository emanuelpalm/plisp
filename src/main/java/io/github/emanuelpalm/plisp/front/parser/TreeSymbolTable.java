package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.util.BinaryTreeMap;

import java.util.HashMap;
import java.util.Optional;

/**
 * An abstract syntax {@link Tree} symbol table.
 */
public class TreeSymbolTable {
    private final HashMap<String, TreeNode> map;
    private final BinaryTreeMap<String, TreeNode> tree;

    /** Creates new empty symbol table. */
    public TreeSymbolTable() {
        map = new HashMap<>();
        tree = new BinaryTreeMap<>();
    }

    private TreeSymbolTable(final HashMap<String, TreeNode> map, final BinaryTreeMap<String, TreeNode> tree) {
        this.map = map;
        this.tree = tree;
    }

    /** Attempts to find symbol by given name. */
    public Optional<TreeNode> search(final String name) {
        return Optional.ofNullable(
                tree.search(name).orElse(map.get(name))
        );
    }

    /** Creates new symbol table also including given entry. */
    public TreeSymbolTable insert(final String name, final TreeNode value) {
        return new TreeSymbolTable(map, tree.insert(name, value));
    }

    /** Creates new symbol table also including given entry. */
    public TreeSymbolTable insert(final TreeNode.Symbol name, final TreeNode value) {
        return insert(name.name(), value);
    }
}
