package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.Token;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static io.github.emanuelpalm.util.testing.TreeUtils.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestTreeNode {
    @Test(dataProvider = "nodesEvaluatingToSelf")
    public void shouldEvaluateToSelf(final TreeNode n) {
        assertEquals(n.evaluate(new TreeSymbolTable()), n);
    }

    @DataProvider
    public static Object[][] nodesEvaluatingToSelf() {
        return new Object[][]{
                new Object[]{TreeNode.Void.of(Token.NIL)},
                new Object[]{TreeNode.Number.of(Token.NIL)},
                new Object[]{new TreeNode.List(Token.NIL, Collections.emptyList())},
        };
    }

    @Test
    public void shouldEvaluateSymbolToAssociatedNode() {
        final TreeSymbolTable t = new TreeSymbolTable()
                .insertLocal(symbolOf("test"), numberOf("123"));

        assertEquals(symbolOf("test").evaluate(t), numberOf("123"));
    }

    @Test
    public void shouldConvertNumberToJavaDouble() {
        assertEquals(numberOf("123.45").toDouble(), 123.45);
    }

    @Test
    public void shouldReturnSymbolName() {
        assertEquals(symbolOf("test").name(), "test");
    }

    @Test
    public void shouldReturnListNodes() {
        assertEquals(listOf(numberOf("123"), numberOf("456")).nodes(), Arrays.asList(numberOf("123"), numberOf("456")));
    }
}
