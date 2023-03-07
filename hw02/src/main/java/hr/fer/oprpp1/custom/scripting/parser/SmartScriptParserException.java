package hr.fer.oprpp1.custom.scripting.parser;

/**
 * Exception that is thrown when an error occurs during parsing.
 */
public class SmartScriptParserException extends RuntimeException {
    public SmartScriptParserException() {
        super();
    }

    public SmartScriptParserException(String message) {
        super(message);
    }

    public SmartScriptParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartScriptParserException(Throwable cause) {
        super(cause);
    }
}
