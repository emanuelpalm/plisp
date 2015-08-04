package io.github.emanuelpalm.plisp.front.parser;

import io.github.emanuelpalm.plisp.front.lexer.Token;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
                new Object[]{TreeNode.Integer.of(Token.NIL)},
                new Object[]{TreeNode.Number.of(Token.NIL)},
                new Object[]{new TreeNode.List(Token.NIL, Collections.emptyList())},
        };
    }

    @Test
    public void shouldEvaluateSymbolToAssociatedNode() {
        final TreeSymbolTable t = new TreeSymbolTable()
                .put(symbolOf("test"), integerOf("123"));

        assertEquals(symbolOf("test").evaluate(t), integerOf("123"));
    }

    @Test
    public void shouldEvaluateCallToCallableResult() {
        final TreeSymbolTable t = new TreeSymbolTable()
                .put(symbolOf("test"), callableOf((t0, args) -> {
                    assertEquals(args, listOf(numberOf("1.23"), numberOf("45.6")));
                    assertTrue(t0.find("test").isPresent());
                    return integerOf("123");
                }));
        final TreeNode result = callOf(symbolOf("test"), numberOf("1.23"), numberOf("45.6"))
                .evaluate(t);

        assertEquals(result, integerOf("123"));
    }

    @Test
    public void shouldEvaluateMetaAsItsOriginNode() {
        final TreeNode n = metaOf(integerOf("123"), numberOf("456"));
        assertEquals(n.evaluate(new TreeSymbolTable()), integerOf("123"));
    }

    @Test
    public void shouldEvaluateRootProperly() {
        // TODO.
    }
}
