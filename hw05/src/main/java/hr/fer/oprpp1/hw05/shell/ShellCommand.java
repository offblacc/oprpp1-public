package hr.fer.oprpp1.hw05.shell;

import java.util.List;

/**
 * Interface representing a blueprint for a shell command.
 */
public interface ShellCommand {
    /**
     * Executes the command. The method returns a ShellStatus object which
     * determines whether the shell should continue to run or terminate.
     * Env is the environment in which the command is executed, and is used
     * to read and write from stdin, as well as to access the shell's
     * environment variables.
     * @param env the environment in which the command is executed
     * @param arguments command arguments
     * @return a ShellStatus object which determines whether the shell should
     * continue to run or terminate
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Returns the command name.
     * @return the command name
     */
    String getCommandName();

    /**
     * Returns the command description as a list of strings,
     * each string representing a line of the description.
     * @return the command description as a list of strings
     */
    List<String> getCommandDescription();
}
