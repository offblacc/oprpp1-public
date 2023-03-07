package hr.fer.oprpp1.hw04.db;

/**
 * Interface for field getters of class StudentRecord.
 */
@FunctionalInterface
public interface IFieldValueGetter {
    /**
     * Returns the value of the field of the given student record.
     * 
     * @param record - student record
     * @return the value of the field of the given student record
     */
    public String get(StudentRecord record);
}
