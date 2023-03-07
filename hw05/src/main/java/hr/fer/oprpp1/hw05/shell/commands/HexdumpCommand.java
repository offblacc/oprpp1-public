package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.crypto.Util;
import hr.fer.oprpp1.hw05.parser.MyShellParser;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.System.exit;

/**
 * Class representing hexdump command.
 */
public class HexdumpCommand implements ShellCommand {
    /**
     * Command name
     */
    public static final String NAME = "hexdump";

    /**
     * Description of the command
     */
    public static final List<String> DESCRIPTION = List.of(
            "Expects a single argument: file name and produces hex-output"
    );

    /**
     * Size of the buffer used for reading from the file
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Width of the hexdump - number of hex bytes per line.
     * Must be an even number. The output will scale accordingly.
     */
    private static final int DUMP_WIDTH = 16; // should be an even number

    /**
     * Lower bound for the printable characters. If lower,
     * the character will be replaced with a dot.
     */
    private static final int STANDARD_CHARSET_LOWER_BOUND = 32;

    /**
     * Upper bound for the printable characters. If higher,
     * the character will be replaced with a dot.
     */
    private static final int STANDARD_CHARSET_UPPER_BOUND = 127;

    /**
     * {@inheritDoc}
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = MyShellParser.parseArgumentsSupportingQuotes(arguments);
        if (args.length != 1) {
            try {
                env.writeln("Hexdump command expects a single argument: file name");
            } catch (ShellIOException e) {
                exit(1);
            }
            return ShellStatus.CONTINUE;
        }

        Path source = Paths.get(args[0]);
        if (!Files.isReadable(source)) {
            try {
                env.writeln("File " + source + " is not readable or does not exist.");
            } catch (ShellIOException e) {
                exit(1);
            }
            return ShellStatus.CONTINUE;
        }
        try (InputStream is = new BufferedInputStream(Files.newInputStream(source))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            int hexCounter = 0;
            StringBuilder sbLine = new StringBuilder();
            while ((bytesRead = is.read(buffer)) > 0) {
                for (int offset = 0; offset < bytesRead; offset += DUMP_WIDTH) {
                    sbLine.append(String.format("%08X", hexCounter)).append(':');
                    int rowLen = Math.min(DUMP_WIDTH, bytesRead - offset);
                    for (int i = 0; i < rowLen; i++) {
                        if (i != 0 && i % 8 == 0) sbLine.append('|');
                        else sbLine.append(' ');
                        sbLine.append(String.format("%02X", buffer[offset + i]));
                    }

                    if (rowLen < DUMP_WIDTH) {
                        for (int i = rowLen; i < DUMP_WIDTH; i++) {
                            if (i % 8 == 0) sbLine.append('|');
                            else sbLine.append(' ');
                            sbLine.append("  ");
                        }
                    }

                    sbLine.append(" | ");
                    for (int i = 0; i < rowLen; i++) {
                        if (buffer[offset + i] < STANDARD_CHARSET_LOWER_BOUND || buffer[offset + i] > STANDARD_CHARSET_UPPER_BOUND)
                            sbLine.append('.');
                        else sbLine.append((char) buffer[offset + i]);
                    }
                    env.writeln(sbLine.toString());
                    hexCounter += DUMP_WIDTH;
                    sbLine = new StringBuilder();
                }
            }
        } catch (IOException e) {
            try {
                env.writeln("Error while reading file " + source);
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
