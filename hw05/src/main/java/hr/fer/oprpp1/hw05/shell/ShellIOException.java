package hr.fer.oprpp1.hw05.shell;

/**
 * Exception thrown when an IO error occours in the shell.
 */
public class ShellIOException extends Exception {
    /**
     * {@inheritDoc}
     */
    public ShellIOException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public ShellIOException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public ShellIOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public ShellIOException(Throwable cause) {
        super(cause);
    }
}
