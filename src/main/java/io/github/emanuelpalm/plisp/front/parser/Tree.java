package io.github.emanuelpalm.plisp.front.parser;

/**
 * An abstract syntax tree.
 *
 * @see TreeNode
 * @see TreeSymbolTable
 */
public class Tree {
    public final TreeNode.Root root;
    public final TreeSymbolTable table;

    /** Creates new tree with given root and table. */
    public Tree(final TreeNode.Root root, final TreeSymbolTable t) {
        this.root = root;
        this.table = t;
    }

    /** Evaluates abstract syntax tree. */
    public TreeNode evaluate() {
        return root.evaluate(table);
    }
}
