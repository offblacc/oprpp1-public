package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.*;

import java.io.*;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyShellEnvironment implements Environment {
    private SortedMap<String, ShellCommand> commands;
    private char multilineSymbol;
    private char promptSymbol;
    private char moreLinesSymbol;
    private final char DEFAULT_PROMPT = '>';
    private final char DEFAULT_MORELINES = '\\';
    private final char DEFAULT_MULTILINE = '|';
    private BufferedReader br;
    private BufferedWriter bw;

    public MyShellEnvironment() {
        this.multilineSymbol = DEFAULT_MULTILINE;
        this.promptSymbol = DEFAULT_PROMPT;
        this.moreLinesSymbol = DEFAULT_MORELINES;
        this.commands = initCommands();
        br = new BufferedReader(new InputStreamReader(System.in));
        bw = new BufferedWriter(new OutputStreamWriter(System.out));
    }


    /**
     * Reads a single line from the standard input. If the line ends with the
     * morelines symbol, the method will continue reading until the line ends
     * without the multiline symbol.
     *
     * @return the line read from the standard input
     * @throws ShellIOException if an error occurs while reading from the standard input
     */
    @Override
    public String readLine() throws ShellIOException {
        StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();
            sb.append(line);
            while (line.endsWith(String.valueOf(moreLinesSymbol))) {
                bw.write(multilineSymbol + " ");
                bw.flush();
                line = br.readLine();
                sb.append(line);
            }
        } catch (Exception e) {
            throw new ShellIOException("Error while reading from input stream.");
        }
        return sb.toString();
    }

    /**
     * Writes the given text to the standard output.
     *
     * @param text the text to be written
     * @throws ShellIOException if an error occurs while writing to the standard output
     */
    @Override
    public void write(String text) throws ShellIOException {
        if (text == null) {
            return; // should not happen
        }
        try {
            bw.write(text);
            bw.flush();
        } catch (IOException e) {
            throw new ShellIOException("Error while writing to output stream.");
        }
    }

    /**
     * Writes the given text to the standard output and adds a new line.
     *
     * @param text the text to be written
     * @throws ShellIOException if an error occurs while writing to the standard output
     */
    @Override
    public void writeln(String text) throws ShellIOException {
        write(text + System.lineSeparator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ShellCommand> commands() {
        return commands;
    }

    /**
     * Initializes the map of commands, returning an unmodifiable map.
     * @return the map of commands, unmodifiable
     */
    private SortedMap<String, ShellCommand> initCommands() {
        return Collections.unmodifiableSortedMap(new TreeMap<>() {{
            put("cat", new CatCommand());
            put("charsets", new CharsetsCommand());
            put("copy", new CopyCommand());
            put("hexdump", new HexdumpCommand());
            put("ls", new LSCommand());
            put("mkdir", new MkdirCommand());
            put("tree", new TreeCommand());
            put("symbol", new SymbolCommand());
            put("exit", new ExitCommand());
            put("help", new HelpCommand());
        }});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getMultilineSymbol() {
        return multilineSymbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMultilineSymbol(Character symbol) {
        multilineSymbol = symbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getPromptSymbol() {
        return promptSymbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPromptSymbol(Character symbol) {
        promptSymbol = symbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getMorelinesSymbol() {
        return moreLinesSymbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMorelinesSymbol(Character symbol) {
        moreLinesSymbol = symbol;
    }
}
