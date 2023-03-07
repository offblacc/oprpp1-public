package hr.fer.oprpp1.hw05.parser;

import hr.fer.oprpp1.hw05.shell.Environment;

import java.util.ArrayList;
import java.util.List;

public class MyShellParser {
    Environment env;
    String commandName;
    String arguments;

    public MyShellParser(Environment env) {
        this.env = env;
    }

    /**
     * Parses the text into command name and arguments. Converts text to a single line, parsing
     * multiline commands into a single line correspondingly to the environment settings.
     *
     * @param text the text to be parsed
     */
    public void parse(String text) {
        commandName = null; // reset on new parse() call, parsing a new line with the same parser object
        arguments = null;
        // this first StringBuilder usage is to build the command name
        StringBuilder sb = new StringBuilder();
        int i;
        int textLen = text.length();

        // reading the command name
        for (i = 0; i < textLen; i++) {
            if (text.charAt(i) == ' ') {
                commandName = sb.toString();
                sb = new StringBuilder(); // this SB purpose is to transform a multiline command into a single line text
                break;
            } else {
                sb.append(text.charAt(i));
            }
        }

        if (i == textLen) { // if the command name is the only thing on the line
            commandName = sb.toString();
            arguments = "";
            return;
        }

        int argumentsStartingIndex = i;

        // converting to a single line text
        if (text.indexOf('\n') != -1) {
            while (i < textLen) {
                // the current char is always appended, in case of newline it's the next two that need to be skipped
                // so the only difference between there being a newline or not is the `i` increment
                sb.append(text.charAt(i));
                if (i < textLen - 2) {
                    if (text.charAt(i) == ' ' && text.charAt(i + 1) == env.getMorelinesSymbol() && text.charAt(i + 2) == '\n') {
                        i += 3;
                    }
                } else {
                    i++;
                }
            }
        }
        arguments = text.substring(argumentsStartingIndex).trim();
    }

    /**
     * Returns the command name.
     *
     * @return the command name
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Returns the arguments.
     *
     * @return the arguments
     */
    public String getArguments() {
        return arguments;
    }

    /**
     * Gets a string of arguments and returns a list of arguments. Arguments are separated by spaces,
     * but spaces inside quotes are not considered separators. Quotes can be escaped with a backslash.
     * Backlashes can be escaped with another backslash. Backslash followed by anything else is legal
     * and is treated as plain text, as a backslash followed by whatever character.
     *
     * @param arguments - string of arguments
     * @return list of arguments
     */
    public static String[] parseArgumentsSupportingQuotes(String arguments) {
        List<String> argumentList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int argLen = arguments.length();
        boolean inQuotes = false;
        boolean escaped = false;
        while (i < argLen) {
            char c = arguments.charAt(i);
            if (c == ' ' && !inQuotes) {
                argumentList.add(sb.toString());
                sb = new StringBuilder();
            } else if (c == '"') {
                if (escaped) {
                    sb.append('"');
                    escaped = false;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == '\\') {
                if (escaped) {
                    sb.append('\\');
                    escaped = false;
                } else {
                    escaped = true;
                }
            } else {
                sb.append(c);
                escaped = false;
            }
            i++;
        }
        argumentList.add(sb.toString());
        return argumentList.toArray(new String[0]);
    }

    /**
     * Gets a string of arguments and returns a list of arguments. Arguments are separated by spaces.
     * @param arguments - string of arguments
     * @return list of arguments
     */
    public static String[] processArguments(String arguments) {
        List<String> argumentList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        arguments = arguments.trim();
        int argLen = arguments.length();
        while (i < argLen) {
            char c = arguments.charAt(i);
            if (c == ' ') {
                argumentList.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
            i++;
        }
        argumentList.add(sb.toString());
        return argumentList.toArray(new String[0]);
    }
}