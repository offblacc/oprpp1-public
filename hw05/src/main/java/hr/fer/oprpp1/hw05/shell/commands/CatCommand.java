package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.parser.MyShellParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

/**
 * Class representing cat command.
 */
public class CatCommand implements ShellCommand {
    /**
     * Command name
     */
    public static final String NAME = "cat";

    /**
     * Command description
     */
    public static final List<String> DESCRIPTION = Arrays.asList("Command takes a single argument - file name,", "and writes its content to the console.");

    /**
     * Size of the buffer used for reading from the file and writing to stdout.
     */
    private static final int BUFFER_SIZE = 1024; // going to the terminal - 4096b  might be too much

    /**
     * {@inheritDoc}
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.equals("")) {
            try {
                env.writeln("Missing file name.");
                return ShellStatus.CONTINUE;
            } catch (ShellIOException e) {
                exit(1);
            }
        }
        String[] argsArray = MyShellParser.parseArgumentsSupportingQuotes(arguments);
        Charset charset = null;

        if (argsArray.length == 1) {
            charset = Charset.defaultCharset();
        } else if (argsArray.length == 2) {
            try {
                charset = Charset.forName(argsArray[1]);
            } catch (IllegalCharsetNameException e) {
                try {
                    env.writeln("Illegal charset name.");
                    return ShellStatus.CONTINUE;
                } catch (ShellIOException e1) {
                    exit(1);
                }
            } catch (UnsupportedCharsetException e) {
                try {
                    env.writeln("Unsupported charset.");
                    return ShellStatus.CONTINUE;
                } catch (ShellIOException e1) {
                    exit(1);
                }
            } catch (NullPointerException e) {
                try {
                    env.writeln("Charset name is null.");
                    return ShellStatus.CONTINUE;
                } catch (ShellIOException e1) {
                    exit(1);
                }
            }
        } else {
            try {
                env.writeln("Invalid number of arguments.");
            } catch (ShellIOException e) {
                exit(1);
            }
            return ShellStatus.CONTINUE;
        }

        try (InputStream is = new BufferedInputStream(Files.newInputStream(Paths.get(argsArray[0])))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int numReadBytes;
            while ((numReadBytes = is.read(buffer)) > 0) {
                String s = new String(buffer, 0, numReadBytes, charset);
                env.write(s);
            }
            env.writeln("");
        } catch (IOException e) {
            try {
                env.writeln("Error while reading file.");
            } catch (ShellIOException e1) {
                exit(1);
            }
            return ShellStatus.CONTINUE;
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
