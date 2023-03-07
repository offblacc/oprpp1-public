package hr.fer.oprpp1.hw04.db;

/**
 * Class that represents a single student record.
 */
public class StudentRecord {
    /**
     * The student's JMBAG. It is unique for each student, meaning it is also used
     * as the primary key.
     */
    String jmbag;

    /**
     * The student's last name.
     */
    String lastName;

    /**
     * The student's first name.
     */
    String firstName;

    /**
     * The student's final grade.
     */
    int finalGrade;

    /**
     * Constructs a new StudentRecord using the given parameters.
     *
     * @param jmbag      - the student's JMBAG
     * @param lastName   - the student's last name
     * @param firstName  - the student's first name
     * @param finalGrade - the student's final grade
     */
    public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.finalGrade = finalGrade;
    }

    /**
     * Returns the student's JMBAG.
     *
     * @return - the student's JMBAG
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Returns the student's last name.
     *
     * @return - the student's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the student's first name.
     *
     * @return - the student's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the student's final grade.
     *
     * @return - the student's final grade
     */
    public int getFinalGrade() {
        return finalGrade;
    }

    /**
     * Returns hashcode, calculated using the student's JMBAG.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
        return result;
    }

    /**
     * Checks if the given object is equal to this one. Two objects are equal if
     * they are both instances of StudentRecord and their JMBAGs are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentRecord other = (StudentRecord) obj;
        if (jmbag == null) {
            if (other.jmbag != null)
                return false;
        } else if (!jmbag.equals(other.jmbag))
            return false;
        return true;
    }

    /**
     * Returns a string representation of this StudentRecord.
     */
    @Override
    public String toString() {
        return "StudentRecord [jmbag=" + jmbag + ", lastName=" + lastName + ", firstName=" + firstName
                + ", finalGrade=" + finalGrade + "]";
    }

    /**
     * Returns an array of StudentRecord parameters.
     * 
     * @return an array of StudentRecord parameters
     */
    public String[] toArray() {
        String[] arr = new String[4];
        arr[0] = getJmbag();
        arr[1] = getLastName();
        arr[2] = getFirstName();
        arr[3] = Integer.toString(getFinalGrade());
        return arr;
    }
}
