package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.parser.MyShellParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

/**
 * Class representing symbol command.
 */
public class SymbolCommand implements ShellCommand {
    /**
     * Command name
     */
    public static final String NAME = "symbol";

    /**
     * Description of the command
     */
    public static final List<String> DESCRIPTION = Arrays.asList(
            "Used to change environment symbols.",
            "Currently supports changing prompt, multiline and morelines symbols."
    );

    /**
     * {@inheritDoc}
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = MyShellParser.processArguments(arguments);
        if (args.length != 2) {
            try {
                env.writeln("Expected 2 arguments.");
            } catch (ShellIOException e) {
                exit(1);
            }
            return ShellStatus.CONTINUE;
        }

        int symbolIndex = 0;
        if (List.of(new String[]{"MORELINES", "PROMPT", "MULTILINE"}).contains(args[0])) {
            symbolIndex = 1; // the symbol is at index one
        }

        switch (args[1 - symbolIndex]) {
            case "PROMPT":
                env.setPromptSymbol(args[symbolIndex].charAt(0));
            case "MORELINES":
                env.setMorelinesSymbol(args[symbolIndex].charAt(0));
            case "MULTILINE":
                env.setMultilineSymbol(args[symbolIndex].charAt(0));
        }

        try {
            env.writeln("Symbol for " + args[1 - symbolIndex] + " changed to '" + args[symbolIndex] + "'");
        } catch (ShellIOException e) {
            exit(1);
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCommandName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getCommandDescription() {
        return DESCRIPTION;
    }
}
