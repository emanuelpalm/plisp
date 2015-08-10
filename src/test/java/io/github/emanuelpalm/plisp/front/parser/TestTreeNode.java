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
                new Object[]{TreeNode.Integer.of(Token.NIL)},
                new Object[]{TreeNode.Number.of(Token.NIL)},
                new Object[]{new TreeNode.List(Token.NIL, Collections.emptyList())},
        };
    }

    @Test
    public void shouldEvaluateSymbolToAssociatedNode() {
        final TreeSymbolTable t = new TreeSymbolTable()
                .insertLocal(symbolOf("test"), integerOf("123"));

        assertEquals(symbolOf("test").evaluate(t), integerOf("123"));
    }

    @Test
    public void shouldEvaluateCallToCallableResult() {
        final TreeSymbolTable t = new TreeSymbolTable()
                .insertLocal(symbolOf("test"), callableOf((t0, args) -> {
                    assertEquals(args, listOf(numberOf("1.23"), numberOf("45.6")));
                    assertTrue(t0.search("test").isPresent());
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
        final TreeNode.Root r = rootOf(
                declarableOf((t) -> t.insertGlobal("fun0", callableOf((t0, args) -> callOf(symbolOf("fun1"), args.nodes().get(0)).evaluate(t0)))),
                declarableOf((t) -> t.insertGlobal("main", callableOf((t0, args) -> callOf(symbolOf("fun0"), args.nodes().get(0)).evaluate(t0)))),
                declarableOf((t) -> t.insertGlobal("fun1", callableOf((t0, args) -> args.nodes().get(0).evaluate(t0))))
        );
        assertEquals(r.evaluate(new TreeSymbolTable(), listOf(integerOf("123"))), integerOf("123"));
    }

    @Test
    public void shouldConvertIntegerToJavaLong() {
        assertEquals(integerOf("123").toLong(), 123L);
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
        assertEquals(listOf(integerOf("123"), integerOf("456")).nodes(), Arrays.asList(integerOf("123"), integerOf("456")));
    }

    @Test
    public void shouldReturnCallFunctionAndArguments() {
        final TreeNode.Call c = callOf(symbolOf("+"), integerOf("10"), integerOf("20"));
        assertEquals(c.function(), symbolOf("+"));
        assertEquals(c.arguments(), listOf(integerOf("10"), integerOf("20")));
    }

    @Test
    public void shouldReturnMetaOriginAndMetaValue() {
        final TreeNode.Meta m = metaOf(symbolOf("x"), symbolOf("Integer"));
        assertEquals(m.origin(), symbolOf("x"));
        assertEquals(m.meta(), symbolOf("Integer"));
    }

    @Test
    public void shouldReturnRootNodes() {
        assertEquals(rootOf(symbolOf("abc"), symbolOf("def")).nodes(), Arrays.asList(symbolOf("abc"), symbolOf("def")));
    }
}
