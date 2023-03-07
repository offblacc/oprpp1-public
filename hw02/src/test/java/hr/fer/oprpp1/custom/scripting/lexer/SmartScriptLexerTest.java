package hr.fer.oprpp1.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptLexerTest {

    // assert eof
    @Test
    public void testEmptyText() {
        SmartScriptLexer lexer = new SmartScriptLexer("");
        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testBasicText() {
        SmartScriptLexer lexer = new SmartScriptLexer("This is sample text.");
        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("This is sample text.", lexer.getToken().getValue());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);
        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testBasicTestWithNumbers() {
        SmartScriptLexer lexer = new SmartScriptLexer("This is sample text 123 1.23.");
        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("This is sample text 123 1.23.", lexer.getToken().getValue());

    }

    @Test
    public void testInvalidEscapeCharacter() {
        var lexer = new SmartScriptLexer("Hello this is text with invalid escape seq \\");
        assertThrows(SmartScriptParserException.class, () -> lexer.nextToken());
        var lexer2 = new SmartScriptLexer("Hello this is text with in\\valid escape seq");
        assertThrows(SmartScriptParserException.class, () -> lexer2.nextToken());
    }

    @Test
    public void testTextWithOpeningTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("This is sample text {$");
        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("This is sample text ", lexer.getToken().getValue());
        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
    }

    @Test
    public void testTextWithEchoTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("This is sample text {$=1$}");

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("This is sample text ", lexer.getToken().getValue());

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("1", lexer.getToken().getValue());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantInteger);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);
    }

    // {$ FOR i-1.35bbb"1" $} -> {$ FOR i -1.35 bbb "1" $}

    @Test
    public void testWithComplexEchoTag() {
        SmartScriptLexer lexer = new SmartScriptLexer("This is text {$=1.23 @sin\"0.000\"$} još neki tekst.");

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("This is text ", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("1.23", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantDouble);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("@sin", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementFunction);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("0.000", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals(" još neki tekst.", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testAnotherEcho() {
        SmartScriptLexer lexer = new SmartScriptLexer("Banana split: \" onda do Rovinja \\{");
        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Banana split: \" onda do Rovinja {", lexer.getToken().getValue());
    }

    @Test
    public void testHalfTagWithoutDollarSign() {
        SmartScriptLexer lexer = new SmartScriptLexer(
                "Testiram nasumični tekst { je usred njega i ovo je sve jedan $ test.");

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Testiram nasumični tekst { je usred njega i ovo je sve jedan $ test.",
                lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testWithTagAndEndTagAfter() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$=1$}{$END$}");

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("1", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantInteger);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.END, lexer.nextToken().getType());
        assertEquals("END", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testExampleFromPDF() {
        String docBody = "A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}.";
        var SmartScriptLexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BASIC, SmartScriptLexer.nextToken().getType());
        assertEquals("A tag follows ", SmartScriptLexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, SmartScriptLexer.getState());
        assertTrue(SmartScriptLexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, SmartScriptLexer.nextToken().getType());
        assertEquals("{$", SmartScriptLexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, SmartScriptLexer.getState());
        assertTrue(SmartScriptLexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, SmartScriptLexer.nextToken().getType());
        assertEquals("=", SmartScriptLexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, SmartScriptLexer.getState());
        assertTrue(SmartScriptLexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, SmartScriptLexer.nextToken().getType());
        assertEquals("Joe \"Long\" Smith", SmartScriptLexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, SmartScriptLexer.getState());
        assertTrue(SmartScriptLexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, SmartScriptLexer.nextToken().getType());
        assertEquals("$}", SmartScriptLexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, SmartScriptLexer.getState());
        assertTrue(SmartScriptLexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, SmartScriptLexer.nextToken().getType());
        assertEquals(".", SmartScriptLexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, SmartScriptLexer.getState());
        assertTrue(SmartScriptLexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, SmartScriptLexer.nextToken().getType());
    }

    @Test
    public void testWithTagAndEndTagAfterAndWhitespace() {
        SmartScriptLexer lexer = new SmartScriptLexer("{$  =    1   $}{$  END \t $}");

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("1", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantInteger);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.END, lexer.nextToken().getType());
        assertEquals("END", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testWithTagWithRandomName() {
        var lexer = new SmartScriptLexer("{$randomName 1 1.5 -1.5 -1$}tekst  {$END$}");

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("randomName", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("1", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantInteger);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("1.5", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantDouble);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("-1.5", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantDouble);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("-1", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantInteger);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("tekst  ", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.END, lexer.nextToken().getType());
        assertEquals("END", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testTagWithVarAndStrings() {
        SmartScriptLexer lexer = new SmartScriptLexer("pretext {$= \"1\" \"ovo je string\" ovoVarijabla$}");

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("pretext ", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("1", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("ovo je string", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("ovoVarijabla", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementVariable);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
        assertThrows(SmartScriptParserException.class, () -> lexer.nextToken());
    }

    @Test
    public void testEchoPDFVersion() {
        var lexer = new SmartScriptLexer("{$= i i * @sin @decfmt $}");

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("i", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementVariable);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("i", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementVariable);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("*", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementOperator);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("@sin", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementFunction);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("@decfmt", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementFunction);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    // helper method
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

    @Test
    public void testreadExample1() {
        String docBody = readExample(1);
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Ovo je \nsve jedan text node", lexer.getToken().getValue());
    }

    @Test
    public void testreadExample2() {
        String docBody = readExample(2);
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Ovo je \nsve jedan {$ text node", lexer.getToken().getValue());
    }

    @Test
    public void readExample3() {
        String docBody = readExample(3);
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Ovo je \nsve jedan \\{$text node", lexer.getToken().getValue());
    }

    @Test
    public void testReadExample4() {
        String docBody = readExample(4);
        var lexer = new SmartScriptLexer(docBody);

        assertThrows(SmartScriptParserException.class, () -> lexer.nextToken());
    }

    @Test
    public void testReadExample5() {
        String docBody = readExample(4);
        var lexer = new SmartScriptLexer(docBody);

        assertThrows(SmartScriptParserException.class, () -> lexer.nextToken());
    }

    @Test
    public void testReadExample6() {
        String docBody = readExample(6);
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Ovo je OK ", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("String ide\nu više redaka\nčak tri", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testReadExample7() {
        String docBody = readExample(7);
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Ovo je isto OK ", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("String ide\nu \"više\" \nredaka\novdje a stvarno četiri", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testReadExample8() {
        String docBody = readExample(8);
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Ovo se ruši ", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertThrows(SmartScriptParserException.class, () -> lexer.nextToken());
    }

    @Test
    public void testReadExample9() {
        String docBody = readExample(9);
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("Ovo se ruši ", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertThrows(SmartScriptParserException.class, () -> lexer.nextToken());
    }

    @Test
    public void testPDFExampleNoWhitespaces() {
        String docBody = "{$ FOR i-1.35bbb\"1\" $}";
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("FOR", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("i", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementVariable);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("-1.35", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementConstantDouble);

        assertEquals(SmartScriptTokenType.BASIC, lexer.nextToken().getType());
        assertEquals("bbb", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementVariable);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("1", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testValidStringEscapeNewline() {
        String docBody = "{$= \"text\nnewline\" $}";
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("text\nnewline", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testValidStringEscapeNewline2() {
        String docBody = "{$= \"text\\nnewline\" $}";
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("text\nnewline", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testValidStringEscapeCarriageReturn() {
        String docBody = "{$= \"text\rCR\" $}";
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("text\rCR", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testValidStringEscapeCarriageReturn2() {
        String docBody = "{$= \"text\\rCR\" $}";
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("text\rCR", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testValidStringEscapeTab() {
        String docBody = "{$= \"text\tTAB\" $}";
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("text\tTAB", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testValidStringEscapeTab2() {
        String docBody = "{$= \"text\\tTAB\" $}";
        var lexer = new SmartScriptLexer(docBody);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("{$", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG, lexer.nextToken().getType());
        assertEquals("=", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.TAG_STRING, lexer.nextToken().getType());
        assertEquals("text\tTAB", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.TAG, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.BOUND, lexer.nextToken().getType());
        assertEquals("$}", lexer.getToken().getValue());
        assertEquals(SmartScriptLexerState.BASIC, lexer.getState());
        assertTrue(lexer.getToken().getElement() instanceof ElementString);

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

}