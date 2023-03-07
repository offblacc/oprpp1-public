package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldValueGettersTest {
    @Test
    public void testFirstName() {
        StudentRecord record = new StudentRecord("0000000001", "Bosnić", "Ivan", 5);
        assertEquals("Ivan", FieldValueGetters.FIRST_NAME.get(record));
    }

    @Test
    public void testLastName() {
        StudentRecord record = new StudentRecord("0000000001", "Bosnić", "Ivan", 5);
        assertEquals("Bosnić", FieldValueGetters.LAST_NAME.get(record));
    }

    @Test
    public void testJmbag() {
        StudentRecord record = new StudentRecord("0000000001", "Bosnić", "Ivan", 5);
        assertEquals("0000000001", FieldValueGetters.JMBAG.get(record));
    }
}
