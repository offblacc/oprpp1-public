package hr.fer.oprpp1.hw04.db;

/**
 * Demands that classes that implement this interface implement a method that
 * compares two strings. Used in all operators that compare two strings.
 */
@FunctionalInterface
public interface IComparisonOperator {
    /**
     * Returns true if the two strings satisfy the condition, false otherwise.
     * 
     * @param value1 - first string
     * @param value2 - second string
     * @return true if the two strings satisfy the condition, false otherwise
     */
    boolean satisfied(String value1, String value2);
}
