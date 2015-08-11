package io.github.emanuelpalm.util.testing;

import io.github.emanuelpalm.plisp.front.lexer.Token;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;
import io.github.emanuelpalm.plisp.front.parser.Tree;
import io.github.emanuelpalm.plisp.front.parser.TreeNode;
import io.github.emanuelpalm.plisp.front.parser.TreeSymbolTable;

import java.util.Arrays;

/**
 * Various {@link Tree} related utilities for testing purposes.
 */
public class TreeUtils {
    /** Creates number node from given string. */
    public static TreeNode.Number numberOf(final String s) {
        return TreeNode.Number.of(new Token(TokenClass.NUM, s));
    }

    /** Creates symbol node from given string. */
    public static TreeNode.Symbol symbolOf(final String s) {
        return TreeNode.Symbol.of(new Token(TokenClass.SYM, s));
    }

    /** Creates list node containing given nodes. */
    public static TreeNode.List listOf(final TreeNode... ns) {
        return TreeNode.List.of(Arrays.asList(ns));
    }

    /** Creates call node containing given arguments. At least one argument has to be given. */
    public static TreeNode.Call callOf(final TreeNode... args) {
        return (TreeNode.Call) TreeNode.Call.of(Token.NIL, listOf(args));
    }

    /** Creates callable node using given interface implementation. */
    public static TreeNode.Function functionOf(final Function f) {
        return new TreeNode.Function() {
            @Override
            public Token token() {
                return Token.NIL;
            }

            @Override
            public TreeNode evaluate(final TreeSymbolTable t, final List args) {
                return f.evaluate(t, args);
            }
        };
    }

    /** Creates meta node from given origin and meta values. The meta value may not be empty. */
    public static TreeNode.Meta metaOf(final TreeNode origin, final TreeNode meta) {
        return (TreeNode.Meta) TreeNode.Meta.of(Token.NIL, origin, meta);
    }

    /** Creates root node containing given nodes. */
    public static TreeNode.Root rootOf(final TreeNode... ns) {
        return (TreeNode.Root) TreeNode.Root.of(listOf(ns));
    }

    /** Creates declarable node using given interface implementation. */
    public static TreeNode.Declarable declarableOf(final Declarable d) {
        return new TreeNode.Declarable() {
            @Override
            public Token token() {
                return Token.NIL;
            }

            @Override
            public void addSymbolTo(final TreeSymbolTable t) {
                d.addSymbolTo(t);
            }
        };
    }

    /**
     * Mock version of {@link TreeNode.Function}.
     */
    public interface Function {
        TreeNode evaluate(final TreeSymbolTable t, final TreeNode.List args);
    }

    /**
     * Mock version of {@link TreeNode.Declarable}.
     */
    public interface Declarable {
        void addSymbolTo(final TreeSymbolTable t);
    }
}
