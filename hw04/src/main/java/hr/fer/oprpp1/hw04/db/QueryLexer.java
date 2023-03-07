package hr.fer.oprpp1.hw04.db;

import java.util.NoSuchElementException;

/**
 * Lexer for the simple query language. Splits the query into tokens which are
 * then used by the QueryParser.
 */
public class QueryLexer {
    /**
     * Data to be tokenized.
     */
    char[] data;

    /**
     * Curret position in the data array.
     */
    int currentIndex;

    /**
     * The last token that was generated.
     */
    QueryToken currentToken;

    /**
     * The value of the token that is in the process of generating.
     */
    String word;

    /**
     * Creates a new query lexer.
     * 
     * @param query - query to be tokenized
     */
    public QueryLexer(String query) {
        data = query.toCharArray();
        currentIndex = 0;
    }

    /**
     * Returns the next token. Throws NoSuchElementException if there are no more
     * tokens.
     * @return the next token
     * @throws NoSuchElementException if there are no more tokens
     */
    public QueryToken nextToken() {
        if (currentToken != null && currentToken.getType() == QueryTokenType.EOF) {
            throw new NoSuchElementException("No more tokens.");
        }

        if (currentIndex >= data.length) {
            currentToken = new QueryToken(null, QueryTokenType.EOF);
            return currentToken;
        }
        if (Character.isWhitespace(data[currentIndex])) {
            currentIndex++;
            return nextToken();
        }
        word = readNextWord();
        QueryTokenType type = determineTokenType();
        return new QueryToken(word, type);

    }

    /**
     * Reads and returns the next word (essentially token's value), whether an
     * operator, a string or a command.
     * 
     * @return the next token's value
     */
    private String readNextWord() {
        StringBuilder sb = new StringBuilder();
        if (Character.isLetter(data[currentIndex])) {
            while (currentIndex < data.length && Character.isLetter(data[currentIndex])) {
                sb.append(data[currentIndex++]);
            }
        } else if (data[currentIndex] == '=') {
            sb.append(data[currentIndex++]);
        } else if (data[currentIndex] == '"') {
            sb.append(data[currentIndex++]);
            while (data[currentIndex] != '"') {
                sb.append(data[currentIndex++]);
            }
            sb.append(data[currentIndex++]);
        } else if ("<>!".indexOf(data[currentIndex]) != -1) {
            if (data[currentIndex] == '<' || data[currentIndex] == '>') {
                sb.append(data[currentIndex++]);
                if (data[currentIndex] == '=') {
                    sb.append(data[currentIndex++]);
                }
            } else {
                sb.append(data[currentIndex++]);
                sb.append(data[currentIndex++]);
            }
        } else {
            while (currentIndex < data.length && !Character.isWhitespace(data[currentIndex])) {
                sb.append(data[currentIndex++]);
            }
        }
        return sb.toString();
    }

    /**
     * After the current word is read by readNextWord() method, this determines the
     * token type based on that word and returns it.
     * 
     * @return - token type based on the current word
     */
    private QueryTokenType determineTokenType() {
        if (word.length() == 1 || word.length() == 2 || word.equals("LIKE")) {
            if (!Character.isLetter(word.charAt(0))) {
                return QueryTokenType.COMPARISON_OPERATOR;
            } else if (word.equals("LIKE")) {
                return QueryTokenType.LIKE;
            }
            throw new QueryParserException("Invalid operator.");
        } else if (word.equals("query")) {
            return QueryTokenType.QUERYKW; // TODO get rid of this
        } else if (word.startsWith("\"") && word.endsWith("\"")) {
            // remove quotes, they were here only to make it easier to determine it's a
            // string
            word = word.substring(1, word.length() - 1);
            return QueryTokenType.STRING;
        } else if (word.equals("jmbag")) {
            return QueryTokenType.JMBAG;
        } else if (word.equalsIgnoreCase("AND")) {
            return QueryTokenType.AND;
        } else if (word.equals("lastName")) {
            return QueryTokenType.LASTNAME;
        } else if (word.equals("firstName")) {
            return QueryTokenType.FIRSTNAME;
        }
        throw new UnsupportedOperationException("Could not determine token type.");
    }
}
