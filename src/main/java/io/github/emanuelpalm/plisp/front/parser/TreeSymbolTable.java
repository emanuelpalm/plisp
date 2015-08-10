package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.util.BinaryTreeMap;

import java.util.HashMap;
import java.util.Optional;

/**
 * An abstract syntax {@link Tree} symbol table.
 */
public class TreeSymbolTable {
    private final HashMap<String, TreeNode> global;
    private final BinaryTreeMap<String, TreeNode> local;

    /** Creates new empty symbol table. */
    public TreeSymbolTable() {
        global = new HashMap<>();
        local = new BinaryTreeMap<>();
    }

    private TreeSymbolTable(final HashMap<String, TreeNode> global, final BinaryTreeMap<String, TreeNode> local) {
        this.global = global;
        this.local = local;
    }

    /** Attempts to find symbol by given name. */
    public Optional<TreeNode> search(final String name) {
        return Optional.ofNullable(
                local.search(name).orElse(global.get(name))
        );
    }

    /** Creates new symbol table also including given entry. */
    public TreeSymbolTable insertLocal(final String name, final TreeNode value) {
        return new TreeSymbolTable(global, local.insert(name, value));
    }

    /** Creates new symbol table also including given entry. */
    public TreeSymbolTable insertLocal(final TreeNode.Symbol name, final TreeNode value) {
        return insertLocal(name.name(), value);
    }
}
