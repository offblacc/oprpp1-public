package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.parser.MyShellParser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

/**
 * Class representing mkdir command.
 */
public class MkdirCommand implements ShellCommand {
    /**
     * Command name
     */
    public static final String NAME = "mkdir";

    /**
     * Description of the command
     */
    public static final List<String> DESCRIPTION = Arrays.asList(
            "Command takes a single argument: directory name,",
            "and creates the appropriate directory structure."
    );

    /**
     * {@inheritDoc}
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = MyShellParser.parseArgumentsSupportingQuotes(arguments);
        if (args.length != 1) {
            try {
                env.writeln("Expected 1 argument.");
            } catch (ShellIOException e) {
                exit(1);
            }
            return ShellStatus.CONTINUE;
        }
        String pathString = args[0];
        File file = new File(pathString);
        if (file.mkdirs()) {
            try {
                env.writeln("Directory created.");
            } catch (ShellIOException e) {
                exit(1);
            }
        } else {
            try {
                env.writeln("Directory not created. Something went wrong.");
            } catch (ShellIOException e) {
                exit(1);
            }
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
