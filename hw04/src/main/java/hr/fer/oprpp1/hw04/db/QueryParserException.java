package hr.fer.oprpp1.hw04.db;

/**
 * Exception that is thrown when the query parser encounters an error.
 */
public class QueryParserException extends RuntimeException {
    public QueryParserException(String message) {
        super(message);
    }

    public QueryParserException() {
        super();
    }
}
