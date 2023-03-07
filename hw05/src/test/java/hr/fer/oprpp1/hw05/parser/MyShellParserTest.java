package hr.fer.oprpp1.hw05.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyShellParserTest {

    // tests if filenames contianing quotes and backslashes will work
    @Test
    void parseArgumentsSupportingQuotes() {
        String arguments = "\"C:\\\\Users\\\\user\\\\Desktop\\\\test.txt\" \"C:\\\\Users\\\\user\\\\Desktop\\\\test2.txt\"";
        String[] args = MyShellParser.parseArgumentsSupportingQuotes(arguments);
        assertEquals(2, args.length);
        assertEquals("C:\\Users\\user\\Desktop\\test.txt", args[0]);
        assertEquals("C:\\Users\\user\\Desktop\\test2.txt", args[1]);
    }

    @Test
    void processArguments() {
        // quotes are treated like normal characters
        String arguments = "neki \"argumenti\" \"sa\" \"navodnicima\"";
        String[] args = MyShellParser.parseArgumentsSupportingQuotes(arguments);
        assertEquals(4, args.length);
        assertEquals("neki", args[0]);
        assertEquals("argumenti", args[1]);
        assertEquals("sa", args[2]);
        assertEquals("navodnicima", args[3]);
    }

    @Test
    void processArguments2() {
        String args = " PROMPT #";
        String[] arguments = MyShellParser.processArguments(args);
        assertEquals(2, arguments.length);
        assertEquals("PROMPT", arguments[0]);
        assertEquals("#", arguments[1]);
    }
}