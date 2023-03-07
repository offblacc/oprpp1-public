package hr.fer.oprpp1.hw02.prob1;

/**
 * Exception that is thrown when an error occurs in the lexer.
 */
public class LexerException extends RuntimeException {
    public LexerException() {
        super();
    }

    public LexerException(String message) {
        super(message);
    }
}
