package hr.fer.oprpp1.hw02.prob1;

/**
 * Class that represents a single token. A token contains of a TokenType and a
 * value.
 */
public class Token {
    TokenType type;
    Object value;

    /**
     * Token constructor. Sets the type and value of the token.
     * @param type - type of the token from TokenType enum
     * @param value - value of the token to be set
     */
    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns the token value.
     * @return token value
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Returns the token type.
     * @return token type
     */
    public TokenType getType() {
        return this.type;
    }
}