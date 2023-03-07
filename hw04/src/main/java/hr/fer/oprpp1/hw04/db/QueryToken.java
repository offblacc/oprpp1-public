package hr.fer.oprpp1.hw04.db;

/**
 * Token for the simple query language. Tokens are used by the QueryLexer and
 * QueryParser.
 */
public class QueryToken {
    /**
     * Token's value.
     */
    String value;

    /**
     * Token's type.
     */
    QueryTokenType type;

    /**
     * Creates a new query token.
     * 
     * @param value - token's value
     * @param type  - token's type
     */
    public QueryToken(String value, QueryTokenType type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Returns the token's value.
     * 
     * @return the token's value
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the token's type.
     * 
     * @return the token's type
     */
    public QueryTokenType getType() {
        return type;
    }
}
