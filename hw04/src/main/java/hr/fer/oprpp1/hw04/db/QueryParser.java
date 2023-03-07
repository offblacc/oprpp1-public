package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses a query and creates a list of conditional expressions, with the help
 * of QueryLexer.
 */
public class QueryParser {
    /**
     * Lexer used to tokenize the query.
     */
    private QueryLexer lexer;

    /**
     * The current token that is being processed.
     */
    private QueryToken token;

    /**
     * Boolean that is true if the query is direct, false otherwise.
     */
    private boolean isDirectQuery;

    /**
     * The list of generated conditional expressions, extracted from the query.
     */
    List<ConditionalExpression> expressions;

    /**
     * Creates a new query parser and calls the parse method, parsing it upon
     * creation of the parser itself.
     * 
     * @param query - query to be parsed
     */
    public QueryParser(String query) {
        lexer = new QueryLexer(query);
        parseQuery();
    }

    /**
     * The main parse method, generates the list of conditional expressions.
     */
    public void parseQuery() {
        token = lexer.nextToken();
        expressions = new ArrayList<>();
        if (token.getType() == QueryTokenType.EOF) {
            throw new QueryParserException("Query is empty.");
        }

        var fieldValueGetter = resolveFieldValueGetter(token.getType());
        var comparisonOperator = resolveComparisonOperator((token = lexer.nextToken()));
        var literal = (token = lexer.nextToken()).getValue();
        expressions.add(new ConditionalExpression(fieldValueGetter, literal, comparisonOperator));

        token = lexer.nextToken();

        while (token.getType() != QueryTokenType.EOF) {
            if (token.getType() != QueryTokenType.AND) {
                throw new QueryParserException("Expected AND, got " + token.getValue());
            }
            QueryToken[] condList = new QueryToken[3];
            for (int i = 0; i < 3; i++)
                condList[i] = lexer.nextToken();

            expressions.add(new ConditionalExpression(resolveFieldValueGetter(condList[0].getType()),
                    condList[2].getValue(), resolveComparisonOperator(condList[1])));
            token = lexer.nextToken();
        }
        isDirectQuery = resolveIsDirectQuery();
    }

    /**
     * Determines if the query is direct query.
     * 
     * @return - true if the query is direct query, false otherwise.
     */
    private boolean resolveIsDirectQuery() {
        if (expressions.size() != 1)
            return false;

        ConditionalExpression expression = expressions.get(0);
        if (expression.getFieldGetter() != FieldValueGetters.JMBAG)
            return false;

        if (expression.getComparisonOperator() != ComparisonOperators.EQUALS)
            return false;

        return true;
    }

    /**
     * Getter for isDirectQuery. For actual calculation of isDirectQuery's value,
     * call resolveIsDirectQuery().
     * 
     * @return - true if query is direct query, false otherwise
     */
    public boolean isDirectQuery() {
        return isDirectQuery;
    }

    /**
     * Returns the jmbag for comparison if the query is direct query. Throws
     * IllegalStateException if the query is not direct query.
     * 
     * @return - jmbag for comparison
     * @throws IllegalStateException - if the query is not direct query
     */
    String getQueriedJMBAG() {
        if (!isDirectQuery())
            throw new IllegalStateException("Query is not direct query.");

        return (String) expressions.get(0).getStringLiteral();
    }

    /**
     * Returns the list of conditional expressions.
     * 
     * @return - list of conditional expressions
     */
    public List<ConditionalExpression> getQuery() {
        return expressions;
    }

    /**
     * Resolves the field value getter for the given token type, determining its type
     * based on the token type.
     * 
     * @param typ - token's type
     * @return - field value getter for the given token type
     */
    private IFieldValueGetter resolveFieldValueGetter(QueryTokenType typ) {
        return switch (typ) {
            case LASTNAME -> FieldValueGetters.LAST_NAME;
            case FIRSTNAME -> FieldValueGetters.FIRST_NAME;
            case JMBAG -> FieldValueGetters.JMBAG;
            default -> throw new UnsupportedOperationException("Could not resolve FieldValueGetter" + typ);
        };
    }

    /**¸
     * Resolves the comparison operator for the given token, determining its type
     * based on the token type and value.
     * 
     * @param token - token to be analyzed
     * @return - comparison operator for the given token
     */
    private IComparisonOperator resolveComparisonOperator(QueryToken token) {
        if (token.getType() == QueryTokenType.LIKE) {
            return ComparisonOperators.LIKE;
        }
        return switch (token.getValue()) {
            case "<" -> ComparisonOperators.LESS;
            case "<=" -> ComparisonOperators.LESS_OR_EQUALS;
            case ">" -> ComparisonOperators.GREATER;
            case ">=" -> ComparisonOperators.GREATER_OR_EQUALS;
            case "=" -> ComparisonOperators.EQUALS;
            case "!=" -> ComparisonOperators.NOT_EQUALS;
            default -> throw new UnsupportedOperationException("Could not resolve ComparisonOperator");
        };
    }
}
