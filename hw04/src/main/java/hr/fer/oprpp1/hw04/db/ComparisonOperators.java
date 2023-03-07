package hr.fer.oprpp1.hw04.db;

public class ComparisonOperators {
    /**
     * Checks if the first string is less than the second string.
     */
    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;

    /**
     * Checks if the first string is less than or equal to the second string.
     */
    public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;

    /**
     * Checks if the first string is greater than the second string.
     */
    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;

    /**
     * Checks if the first string is greater than or equal to the second string.
     */
    public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;

    /**
     * Checks if the first string is equal to the second string.
     */
    public static final IComparisonOperator EQUALS = (value1, value2) -> value1.compareTo(value2) == 0;

    /**
     * Checks if the first string is not equal to the second string.
     */
    public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> value1.compareTo(value2) != 0;

    /**
     * Checks if the first string is like the second string, using classic wildcard
     * rules.
     */
    public static final IComparisonOperator LIKE = (value1, value2) -> {
        value1 = value1.toLowerCase();
        value2 = value2.toLowerCase();
        int wildcardCount = 0;
        int wildcardIndex = -1;
        for (char c : value2.toCharArray()) {
            if (c == '*') {
                wildcardCount++;
                wildcardIndex = value2.indexOf(c);
            }
        }
        if (wildcardCount > 1) {
            throw new IllegalArgumentException("Invalid LIKE expression"); // TODO catch this and write message, !don't!
                                                                           // terminate program
        }
        if (wildcardCount == 0) {
            return value1.equals(value2);
        }
        return value1.substring(0, wildcardIndex).startsWith(value2.substring(0, wildcardIndex))
                && value1.substring(wildcardIndex).endsWith(value2.substring(wildcardIndex + 1));
    };
}
