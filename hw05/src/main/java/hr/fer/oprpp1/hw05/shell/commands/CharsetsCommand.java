package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

/**
 * Class representing charsets command.
 */
public class CharsetsCommand implements ShellCommand {
    /**
     * Command name
     */
    public static final String NAME = "charsets";

    /**
     * Description of the command
     */
    public static final List<String> DESCRIPTION = Arrays.asList("lists names of supported charsets for your Java platform",
            "A single charset name is written per line.");

    /**
     * {@inheritDoc}
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Charset.availableCharsets().forEach((k, v) -> {
            try {
                env.writeln(k);
            } catch (ShellIOException e) {
                exit(1);
            }
        });
        return null;
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
