package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface that represents a filter for student records.
 */
public interface IFilter {
    /**
     * Returns true if the given student record satisfies the filter.
     * 
     * @param record - student record to be checked
     * @return true if the given student record satisfies the filter, false
     *         otherwise.
     */
    boolean accepts(StudentRecord record);
}
