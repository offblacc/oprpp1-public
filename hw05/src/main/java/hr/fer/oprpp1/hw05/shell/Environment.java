package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

/**
 * Class Environment represents an environment in which the shell operates.
 */
public interface Environment {
    /**
     * Method reads a line from the user using stdin
     *
     * @return line read from the user
     * @throws ShellIOException if an error occurs while reading
     */
    String readLine() throws ShellIOException;

    /**
     * Method writes the given text to the user using stdout
     *
     * @param text - text to be written
     * @throws ShellIOException
     */
    void write(String text) throws ShellIOException;

    /**
     * Method writes the given text to the user using stdout and adds a new line
     *
     * @param text - text to be written
     * @throws ShellIOException if an error occurs while writing
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Method returns a map of commands
     *
     * @return map of commands
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Method returns the multiline symbol
     *
     * @return multiline symbol
     */
    Character getMultilineSymbol();

    /**
     * Method sets the multiline symbol
     *
     * @param symbol - symbol to be set
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Method returns the prompt symbol
     *
     * @return prompt symbol
     */
    Character getPromptSymbol();

    /**
     * Method sets the prompt symbol
     *
     * @param symbol - symbol to be set
     */
    void setPromptSymbol(Character symbol);

    /**
     * Method returns the morelines symbol
     *
     * @return morelines symbol
     */
    Character getMorelinesSymbol();

    /**
     * Method sets the morelines symbol
     *
     * @param symbol - symbol to be set
     */
    void setMorelinesSymbol(Character symbol);
}
