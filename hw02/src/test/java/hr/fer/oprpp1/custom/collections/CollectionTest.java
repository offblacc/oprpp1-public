package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionTest {
    @Test
    public void testAddAllSatisfying() {
        ArrayIndexedCollection col = new ArrayIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        col.add(4);
        col.add(5);
        ArrayIndexedCollection col2 = new ArrayIndexedCollection();
        col2.add(1);
        col2.add(2);
        col2.add(3);
        col2.add(4);
        col2.add(5);
        col.addAllSatisfying(col2, (Object o) -> (int) o % 2 == 0);
        assertEquals(7, col.size());
        assertEquals(2, col.get(5));
        assertEquals(4, col.get(6));
    }
}