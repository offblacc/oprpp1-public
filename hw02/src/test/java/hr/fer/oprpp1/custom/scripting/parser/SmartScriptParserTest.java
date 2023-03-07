package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class SmartScriptParserTest {
    private String readExample(int n) {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer" + n + ".txt")) {
            if (is == null)
                throw new RuntimeException("Datoteka extra/primjer" + n + ".txt je nedostupna.");
            byte[] data = is.readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }

    // ----------------------------------------------------------
    // this part of tests only asserts if there is 1 text node
    // ----------------------------------------------------------
    // actual parser functionality will be tested later
    // ----------------------------------------------------------
    @Test
    public void testExample1() {
        String docBody = readExample(1);
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(1, parser.countTextNodesRecursively(parser.getDocumentNode()));
    }

    @Test
    public void testExample2() {
        String docBody = readExample(2);
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(1, parser.countTextNodesRecursively(parser.getDocumentNode()));
    }

    @Test
    public void testExample3() {
        String docBody = readExample(3);
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(1, parser.countTextNodesRecursively(parser.getDocumentNode()));
    }

    @Test
    public void testExample4() {
        String docBody = readExample(4);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));

    }

    @Test
    public void testExample5() {
        String docBody = readExample(5);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));

    }

    @Test
    public void testExample6() {
        String docBody = readExample(6);
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(1, parser.countTextNodesRecursively(parser.getDocumentNode()));
    }

    @Test
    public void testExample7() {
        String docBody = readExample(7);
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(1, parser.countTextNodesRecursively(parser.getDocumentNode()));
    }

    @Test
    public void testExample8() {
        String docBody = readExample(8);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testExample9() {
        String docBody = readExample(9);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    // ----------------------------------------------------------

    @Test
    public void testForLoopNode() {
        String docBody = "{$ FOR i 1 10 1 $} text {$END$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        ForLoopNode forLoopNode = (ForLoopNode) parser.getDocumentNode().getChild(0);
        assertEquals("i", forLoopNode.getVariable().asText());
        assertEquals("1", forLoopNode.getStartExpression().asText());
        assertEquals("10", forLoopNode.getEndExpression().asText());
        assertEquals("1", forLoopNode.getStepExpression().asText());
        assertEquals(" text ", forLoopNode.getChild(0).toString());
    }

    @Test
    public void testForLoopNodeTooManyArguments() {
        String docBody = "{$ FOR i 1 10 1 1 $} text {$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testForLoopNodeTooFewArguments() {
        String docBody = "{$ FOR i 1 $} text {$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testForLoopNodeWithStringArguments() {
        String docBody = "{$ FOR i \"1\" \"10\" \"1\" $} text {$END$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        ForLoopNode forLoopNode = (ForLoopNode) parser.getDocumentNode().getChild(0);

        assertEquals("i", forLoopNode.getVariable().asText());
        assertTrue(forLoopNode.getVariable() instanceof ElementVariable);

        assertEquals("1", forLoopNode.getStartExpression().asText());
        assertTrue(forLoopNode.getStartExpression() instanceof ElementConstantInteger);

        assertEquals("10", forLoopNode.getEndExpression().asText());
        assertTrue(forLoopNode.getStartExpression() instanceof ElementConstantInteger);

        assertEquals("1", forLoopNode.getStepExpression().asText());
        assertTrue(forLoopNode.getStartExpression() instanceof ElementConstantInteger);

        assertEquals(" text ", forLoopNode.getChild(0).toString());
    }

    @Test
    public void testForLoopWithNumberAsVariable() {
        String docBody = "{$ FOR 1 1 10 1 $} text {$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testForLoopWithNoEndTag() {
        String docBody = "{$ FOR i 1 10 1 $} text";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testForLoopTwoEndTags() {
        String docBody = "{$ FOR i 1 10 1 $} text {$END$}{$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testOnlyEndTag() {
        String docBody = "{$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));

    }

    @Test
    public void testUnclosedForTag() {
        String docBody = "{$ FOR i 1 10 1 $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));

    }

    @Test
    public void testUnclosedForTagAndText() {
        String docBody = "{$ FOR i 1 10 1 $} text";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testUnclosedForTagAndTextBeforeAndAfterTag() {
        String docBody = "neki tekst {$ FOR i 1 10 1 $} text";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testUnclosedEchoTag() {
        String docBody = "{$= i";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testEchoTagWithEndTag() {
        String docBody = "{$= i $}{$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testEndTagWithText() {
        String docBody = "{$END$} text";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testEndTagWithEchoTag() {
        String docBody = "{$END$}{$= i $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testEscapePartialTag() {
        String docBody = "\\{= i $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals("\\{= i $}", parser.getDocumentNode().toString());
    }

    @Test
    public void testEscapeForTag() {
        String docBody = "\\{$ FOR i 1 10 1 $} text";
        assertEquals("\\{$ FOR i 1 10 1 $} text", new SmartScriptParser(docBody).getDocumentNode().toString());
    }

    @Test
    public void testEscapeEchoTag() {
        String docBody = "\\{$= i $}";
        assertEquals("\\{$= i $}", new SmartScriptParser(docBody).getDocumentNode().toString());
    }

    @Test
    public void testExamplePDF() {
        String docBody = "This is sample text.\n{$ FOR i 1 10 1 $}\n This is {$= i $}-th time this message is generated.\n{$END$}\n{$FOR i 0 10 2 $}\n sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n{$END$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    // test out of bounds checks for { tag, if it starts a tag or is just plain text

    @Test
    public void testFakeTagOpenThoroughly1() {
        String docBody = "{{This is {oh, it's, not, { again, not {";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        // this depends on implementation:
        assertEquals("\\{\\{This is \\{oh, it's, not, \\{ again, not \\{", parser.getDocumentNode().toString());
        // this must, however, always be true:
        assertTrue(parser.getDocumentNode().toString()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode().toString()));
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));

    }

    @Test
    public void testFakeTagOpenThoroughly() {
        String docBody = "{{This is {oh, it's, not, { again, not {{{";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        // this depends on implementation:
        assertEquals("\\{\\{This is \\{oh, it's, not, \\{ again, not \\{\\{\\{", parser.getDocumentNode().toString());
        // this must, however, always be true:
        assertTrue(parser.getDocumentNode().toString()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode().toString()));
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testExceptionInTextNodeEscape() {
        String docBody = "Th\\is is {$= i $} text";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testExceptionInTextNodeEscape2() {
        String docBody = "\\This is {$= i $} text";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testExceptionInTextNodeEscape3() {
        String docBody = "\\ This is {$= i $} text";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testExceptionInTextNodeEscape4() {
        String docBody = "\\ {$= i $} text";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testValidTextNodeEscape() {
        String docBody = "This is \\{$= i $} text";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals("This is \\{$= i $} text", parser.getDocumentNode().toString());
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));

    }

    @Test
    public void testValidTextNodeEscape2() {
        String docBody = "This is \\\\{$= i $} text";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
        assertEquals("This is \\\\{$= i $} text", parser.getDocumentNode().toString());
    }

    @Test
    public void testInvalidTagName() {
        String docBody = "{$kaj 1 2 3$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testValidTagEscape() {
        String docBody = "{$= \"Hello \\\"over\\\" there\" $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals("{$= \"Hello \\\"over\\\" there\" $}", parser.getDocumentNode().toString());
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    // --- different combinations of whitespaces inside tags testing ---
    @Test
    public void testVariousWhitespaceCombinations() {
        String docBody = "{$FOR i\"1\"\"10\"\"1\"$} text {$END$}{$=i\"0.000\"@sin$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testVariousWhitespaceCombinations2() {
        String docBody = "{$FOR i \"1\" \"10\" \"1\" $} text {$ END $} {$ = i \"0.000\" @sin $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testVariousWhitespaceCombinations3() {
        String docBody = "{$FOR i 1 10 1 $} text {$ END $} {$ = i 0.000 @sin $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testVariousWhitespaceCombinations4() {
        String docBody = "{$   FOR    i     1      10 1 $} text {$   END    $} {$   =   i    0.000      @sin $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testVariousWhitespaceCombinations5() {
        String docBody = "{$  \t FOR \t   i \t    1      \t10 1 $} text {$  \t END\t    $} {$  \t = \t  i  \t  0.000 \t     @sin $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testQuotationMarkNextToBounds() {
        String docBody = "{$= i \"0.000\" \"@sin\"$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testValidFunctionNames() {
        String docBody = "{$= @sin @S_in9 @s1n_$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testFunctionStartingWithDigit() {
        String docBody = "{$= @1sin $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testFunctionStartingWithUnderscore() {
        String docBody = "{$= @_sin $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testForLoopWithNoStepExpression() {
        String docBody = "{$FOR i 1 10$} text {$END$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testForLoopWithNoStepExpression2() {
        String docBody = "{$FOR i \"1\" \"10\"$} text {$END$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertTrue(parser.getDocumentNode()
                .equals(new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode()));
    }

    @Test
    public void testEndTagWithNoRightBound() {
        String docBody = "{$END$";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testEndTagWithNoRightBound2() {
        String docBody = "{$END";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testEndTagWithNoRightBound3() {
        String docBody = "{$END ";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testForLoopWithDoubleValues() {
        String docBody = "{$FOR i 1.0 10.0 1.0$} text {$END$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }

    @Test
    public void testForLoopWithDoubleValues2() {
        String docBody = "{$FOR i \"1.0\" \"10.0\" \"1.0\"$} text {$END$}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }

    @Test
    public void testForLoopWithInvalidStringParameter() {
        String docBody = "{$FOR i \"1.0\" \"10b.0\" \"1.0\"$} text {$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testForLoopWithVariableInsteadOfNumber() {
        String docBody = "{$FOR i a 10 1$} text {$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testInvalidVariableName() {
        String docBody = "{$FOR i] 1 10 1$} text {$END$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testEchoWithAllRandomOperators() {
        String docBody = "{$= i i * / - + ^ \"0.000\" @sin $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }

    @Test
    public void testNewlineInString() {
        String docBody = "{$= \"\n\" $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }

    @Test
    public void testNewlineInString2() {
        String docBody = "{$= \"\\n\" $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }

    @Test
    public void testCarriageReturnInString() {
        String docBody = "{$= \"\r\" $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }

    @Test
    public void testCarriageReturnInString2() {
        String docBody = "{$= \"\\r\" $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }

    @Test
    public void testTabInString() {
        String docBody = "{$= \"\t\" $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }

    @Test
    public void testTabInString2() {
        String docBody = "{$= \"\\t\" $}";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        assertEquals(parser.getDocumentNode(), new SmartScriptParser(parser.getDocumentNode().toString()).getDocumentNode());
    }
}
