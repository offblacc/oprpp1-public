package hr.fer.oprpp1.hw04.db;

/**
 * Getters for student record fields.
 */
public class FieldValueGetters {
    /**
     * Getter for the first name.
     */
    public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;

    /**
     * Getter for the last name.
     */
    public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;

    /**
     * Getter for the jmbag.
     */
    public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
}
