package io.github.emanuelpalm.util.testing;

import io.github.emanuelpalm.plisp.front.lexer.Token;
import io.github.emanuelpalm.plisp.front.lexer.TokenClass;
import io.github.emanuelpalm.plisp.front.parser.TreeNode;
import io.github.emanuelpalm.plisp.front.parser.TreeSymbolTable;

import java.util.Arrays;
import java.util.Collections;

/**
 * Various {@link TreeNode} related utilities for testing purposes.
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
}
