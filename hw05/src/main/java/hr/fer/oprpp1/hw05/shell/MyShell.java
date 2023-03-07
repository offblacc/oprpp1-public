package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.parser.MyShellParser;

import java.util.SortedMap;

import static java.lang.System.exit;

/**
 * The main class of the shell program.
 */
public class MyShell {
    /**
     * Message that is printed when the program is started.
     */
    private static final String WELCOME_MSG = "Welcome to MyShell v1.0";

    /**
     * The main method of the shell program.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        ShellStatus shellStatus = ShellStatus.CONTINUE;
        Environment env = createEnvironment();
        MyShellParser parser = new MyShellParser(env);
        SortedMap<String, ShellCommand> commands = env.commands();

        try {
            env.writeln(WELCOME_MSG);
        } catch (ShellIOException e) {
            System.out.println("Error while writing to output stream.");
            exit(1);
        }

        while(shellStatus != ShellStatus.TERMINATE) {
            try {
                env.write(env.getPromptSymbol() + " ");
                // trimming - this way the parser knows that if the line contains a \n there is a multiline command
                String line = env.readLine().trim();
                parser.parse(line);
                String commandName = parser.getCommandName();
                String arguments = parser.getArguments();
                ShellCommand command = commands.get(commandName);
                if (command == null) {
                    env.writeln("Command not found.");
                } else {
                    shellStatus = command.executeCommand(env, arguments);
                }
            } catch (ShellIOException e) {
                System.out.println("Error while reading from input stream. Terminating shell.");
                exit(1);
            }
        }
    }

    /**
     * Creates a new environment for the shell.
     * @return the new environment
     */
    private static Environment createEnvironment() {
        return new MyShellEnvironment();
    }
}
