package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.Token;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;

import java.util.ArrayList;

/**
 * An abstract syntax tree.
 *
 * @see TreeNode
 * @see TreeSymbolTable
 */
public class Tree {
    public final TreeNode.Root root;

    /** Creates new tree with given list as root node. */
    public Tree(final TreeNode.Root root) {
        this.root = root;
    }

    /** Evaluates abstract syntax tree with given symbol table and arguments. */
    public TreeNode evaluate(final TreeSymbolTable t, final String... args) {
        final ArrayList<TreeNode> ns = new ArrayList<>(args.length);
        for (final String arg : args) {
            ns.add(TreeNode.Number.of(new Token(TokenClass.NUM, arg)));
        }
        return root.evaluate(t, (TreeNode.List) TreeNode.List.of(Token.NIL, ns));
    }
}
