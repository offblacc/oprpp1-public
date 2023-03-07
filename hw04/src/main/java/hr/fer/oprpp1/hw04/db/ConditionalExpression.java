package hr.fer.oprpp1.hw04.db;

/**
 * A contitional expression that is used to filter records. Contains a field
 * getter which determines which field we're comparing, a string literal which
 * is the value we're comparing to, and a comparison operator which determines
 * how we're comparing the two.
 */
public class ConditionalExpression {
    /**
     * Field getter, determines which field we're comparing.
     */
    public IFieldValueGetter fieldGetter;

    /**
     * String literal, the value we're comparing to.
     */
    public String literal;

    /**
     * Comparison operator, determines how we're comparing the two.
     */
    public IComparisonOperator comparisonOperator;

    /**
     * Creates a new conditional expression.
     * 
     * @param fieldGetter        - field getter
     * @param literal            - string literal
     * @param comparisonOperator - comparison operator
     */
    public ConditionalExpression(IFieldValueGetter fieldGetter, String literal,
            IComparisonOperator comparisonOperator) {
        this.fieldGetter = fieldGetter;
        this.literal = literal;
        this.comparisonOperator = comparisonOperator;
    }

    /**
     * Returns the field getter.
     * 
     * @return field getter
     */
    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    /**
     * Returns the string literal.
     * 
     * @return string literal
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Same as {@link #getLiteral()}, just calls it.
     */
    public String getStringLiteral() {
        return getLiteral();
    }

    /**
     * Returns the comparison operator.
     * 
     * @return comparison operator
     */
    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }
}
