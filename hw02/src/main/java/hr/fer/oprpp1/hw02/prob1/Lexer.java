package hr.fer.oprpp1.hw02.prob1;

public class Lexer {
    /**
     * Stores all characters passed to the constructor as a string.
     */
    private final char[] data;
    /**
     * The last generated token.
     */
    private Token token;
    /**
     * Index of the first unprocessed character.
     */
    private int currentIndex;
    /**
     * Current state of the lexer. Can be one of the following:
     * BASIC, EXTENDED
     */
    LexerState state;

    /**
     * Creates a new Lexer object from the given text. Current index set to 0 and
     * state set to BASIC.
     * Token is set to null, and data is set to the given text. Token will be
     * generated after the first nextToken() method call.
     * 
     * @param text text to be processed
     */
    public Lexer(String text) {
        data = text.toCharArray();
        currentIndex = 0;
        token = null;
        state = LexerState.BASIC;
    }

    /**
     * Generates and returns the next token. Moves the current index as it is
     * passing through the data array of characters.
     * 
     * @return next token
     * @throws LexerException if the token cannot be generated
     */
    public Token nextToken() {
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("No more tokens available.");
        }
        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, null);
            return token;
        }
        if (Character.isWhitespace(data[currentIndex])) {
            currentIndex++;
            return nextToken();
        }

        /*
         * This block is triggered either when we're in LexerState.BASIC state and need
         * to process # as a symbol and switch states, or when we were in extended state
         * and the following block did not increase currentIndex after finding '#', so
         * we return the WORD we processed in EXTENDED state, then switch states and,
         * since we didn't increase our index, move on to the else block next time this
         * method is called and tokenize '#' as a symbol
         */
        StringBuilder sb = new StringBuilder();
        if (state == LexerState.EXTENDED && data[currentIndex] != '#') {
            while (data[currentIndex] != ' ' && data[currentIndex] != '#') {
                sb.append(data[currentIndex++]);
            }
            if (data[currentIndex] == '#') {
                flipLexerState();
            }
            token = new Token(TokenType.WORD, sb.toString());
        } else {

            if (currentIndex == data.length - 1 && data[currentIndex] == '\\') {
                throw new LexerException("Invalid escape ending.");
            }
            if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
                while (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
                    if (data[currentIndex] == '\\') {
                        currentIndex++;
                        if (Character.isLetter(data[currentIndex])) {
                            throw new LexerException("Invalid escape sequence");
                        }
                    }
                    sb.append(data[currentIndex]);
                    if (++currentIndex >= data.length) {
                        break;
                    }
                }
                token = new Token(TokenType.WORD, sb.toString());
            } else if (Character.isDigit(data[currentIndex])) {
                while (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.') {
                    sb.append(data[currentIndex]);
                    if (++currentIndex >= data.length) {
                        break;
                    }
                }
                try {
                    token = new Token(TokenType.NUMBER, Long.parseLong(sb.toString()));
                } catch (NumberFormatException ex) {
                    throw new LexerException("Number cannot be represented as Long");
                }
            } else { // meaning it is a TokenType.SYMBOL
                if (data[currentIndex] == '#') {
                    flipLexerState();
                }
                token = new Token(TokenType.SYMBOL, data[currentIndex++]);
            }
        }
        return token;
    }

    /**
     * Returns the last generated token. Basically just a getter function.
     * 
     * @return the last generated token.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Sets the state of the lexer to the passed LexerState enum. Passing null as
     * state will throw a NullPointerException.
     * 
     * @param state - state to be set
     * @throws NullPointerException if passed state is null
     */
    public void setState(LexerState state) {
        if (state == null) {
            throw new NullPointerException("Lexer state cannot be null");
        }
        this.state = state;
    }

    /**
     * Sets the lexer state to the opposite, since there are only two states and the
     * same symbol is responsible for the flip. Internally used method to help clean
     * up the code.
     */
    private void flipLexerState() {
        if (state == LexerState.BASIC) {
            state = LexerState.EXTENDED;
        } else {
            state = LexerState.BASIC;
        }
    }
}