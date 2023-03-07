package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

public class StudentDatabaseTest {
    @Test
    public void testForJMBAG() {
        StudentDatabase db = new StudentDatabase(new ArrayList<String>(Arrays.asList(
                "0000000001	Akšamović	Marin	2",
                "0000000002	Bakamović	Petra	3",
                "0000000003	Bosnić	Andrea	4")));
        assertEquals("0000000001", db.forJMBAG("0000000001").getJmbag());
        assertEquals("Bakamović", db.forJMBAG("0000000002").getLastName());
        assertEquals("Andrea", db.forJMBAG("0000000003").getFirstName());
        assertEquals(4, db.forJMBAG("0000000003").getFinalGrade());
        assertNull(db.forJMBAG("0000000004"));
    }

    @Test
    public void testFilterTrue() {
        StudentDatabase db = new StudentDatabase(new ArrayList<String>(Arrays.asList(
                "0000000001	Akšamović	Marin	2",
                "0000000002	Bakamović	Petra	3",
                "0000000003	Bosnić	Andrea	4")));
        assertEquals(3, db.filter((record) -> true).size());
        assertEquals(0, db.filter((record) -> false).size());
    }
}
