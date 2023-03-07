package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

/**
 * Class representing help command.
 */
public class HelpCommand implements ShellCommand {
    /**
     * Command name
     */
    public static final String NAME = "help";

    /**
     * Command description
     */
    public static final List<String> DESCRIPTION = Arrays.asList("Takes a single argument - command name,",
            "and prints a description of the given command.",
            "If no argument is given, prints a list of all supported commands.");


    /**
     * {@inheritDoc}
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            try {
                env.writeln("Available commands:");
            } catch (ShellIOException e) {
                exit(1);
            }
            env.commands().forEach((name, command) -> {
                try {
                    env.writeln(name);
                } catch (ShellIOException e) {
                    exit(1);
                }
            });
        } else {
            String commandName = arguments.trim();
            ShellCommand command = env.commands().get(commandName);
            if (command == null) {
                try {
                    env.writeln("Command not found.");
                } catch (ShellIOException e) {
                    exit(1);
                }
            } else {
                try {
                    env.writeln(command.getCommandName().toUpperCase());
                    command.getCommandDescription().forEach(description -> {
                        try {
                            env.writeln(description);
                        } catch (ShellIOException e) {
                            exit(1);
                        }
                    });
                } catch (ShellIOException e) {
                    exit(1);
                }
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
