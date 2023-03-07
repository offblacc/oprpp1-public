package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LinkedListIndexedCollectionTest {
    @Test
    public void testConstructor() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        assertEquals(0, col.size());
    }

    @Test
    public void testConstructorWithCollectionContainingValues() {
        var col1 = new LinkedListIndexedCollection();
        col1.add(2);
        var col = new LinkedListIndexedCollection(col1);
        assertAll(
                () -> assertEquals(1, col.size()),
                () -> assertEquals(2, col.get(0)));

    }

    @Test
    public void testConstructorWithArrayContainingValues() {
        var col1 = new ArrayIndexedCollection();
        col1.add(2);
        var col = new LinkedListIndexedCollection(col1);
        assertAll(
                () -> assertEquals(1, col.size()),
                () -> assertEquals(2, col.get(0)));

    }

    @Test
    public void testSize() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        assertEquals(0, col.size());
        col.add(1);
        assertEquals(1, col.size());
        col.add(2);
        assertEquals(2, col.size());
        col.add(3);
        assertEquals(3, col.size());
    }

    @Test
    public void testIsEmpty() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        assertTrue(col.isEmpty());
        col.add(1);
        assertFalse(col.isEmpty());
    }

    @Test
    public void testAdd() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertAll(
                () -> assertEquals(3, col.size()),
                () -> assertEquals(1, col.get(0)),
                () -> assertEquals(2, col.get(1)),
                () -> assertEquals(3, col.get(2)));
    }

    @Test
    public void testAddNull() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        assertThrows(NullPointerException.class, () -> {
            col.add(null);
        });
    }

    @Test
    public void testInsert() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(3);
        col.add(4);
        col.add(5);
        col.insert(1, 0);
        col.insert(2, 1);
        col.insert(4, 3);
        assertAll(
                () -> assertEquals(6, col.size()),
                () -> assertEquals(1, col.get(0)),
                () -> assertEquals(2, col.get(1)),
                () -> assertEquals(3, col.get(2)),
                () -> assertEquals(4, col.get(3)),
                () -> assertEquals(4, col.get(4)),
                () -> assertEquals(5, col.get(5)));
    }

    @Test
    public void testGet() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertAll(
                () -> assertEquals(1, col.get(0)),
                () -> assertEquals(2, col.get(1)),
                () -> assertEquals(3, col.get(2)));
    }

    @Test
    public void testGetOutOfBounds() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            col.get(3);
        });
    }

    @Test
    public void testIndexOf() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertAll(
                () -> assertEquals(0, col.indexOf(1)),
                () -> assertEquals(1, col.indexOf(2)),
                () -> assertEquals(2, col.indexOf(3)));
    }

    @Test
    public void testIndexOfNonExisting() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertEquals(-1, col.indexOf(4));
    }

    @Test
    public void testContains() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertAll(
                () -> assertTrue(col.contains(1)),
                () -> assertTrue(col.contains(2)),
                () -> assertTrue(col.contains(3)),
                () -> assertFalse(col.contains(4)));
    }

    @Test
    public void testRemoveByValue() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        col.remove(Integer.valueOf(2));
        assertAll(
                () -> assertEquals(2, col.size()),
                () -> assertEquals(1, col.get(0)),
                () -> assertEquals(3, col.get(1)));
    }

    @Test
    public void testRemoveByValueNonExisting() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertFalse(col.remove(Integer.valueOf(4)));
    }

    @Test
    public void testRemoveByIndex() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        col.remove(1);
        assertAll(
                () -> assertEquals(2, col.size()),
                () -> assertEquals(1, col.get(0)),
                () -> assertEquals(3, col.get(1)));
    }

    @Test
    public void testRemoveByIndexOutOfBounds() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            col.remove(3);
        });
    }
    
    @Test
    public void testToArray() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        Object[] array = col.toArray();
        assertAll(
                () -> assertEquals(3, array.length),
                () -> assertEquals(1, array[0]),
                () -> assertEquals(2, array[1]),
                () -> assertEquals(3, array[2]));
    }

    @Test
    public void testClear() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        col.clear();
        assertEquals(0, col.size());
        assertThrows(IndexOutOfBoundsException.class, () -> {
            col.get(1);
        });
    }

    @Test
    public void assertAddAll() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        LinkedListIndexedCollection col2 = new LinkedListIndexedCollection();
        col2.add(4);
        col2.add(5);
        col2.add(6);
        col.addAll(col2);
        assertAll(
                () -> assertEquals(6, col.size()),
                () -> assertEquals(1, col.get(0)),
                () -> assertEquals(2, col.get(1)),
                () -> assertEquals(3, col.get(2)),
                () -> assertEquals(4, col.get(3)),
                () -> assertEquals(5, col.get(4)),
                () -> assertEquals(6, col.get(5)));
    }

    @Test
    public void testRemoveByValueEdgeCases() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add(1);
        col.add(2);
        col.add(3);
        assertAll(
                () -> assertTrue(col.remove(Integer.valueOf(1))),
                () -> assertTrue(col.remove(Integer.valueOf(3))),
                () -> assertTrue(col.remove(Integer.valueOf(2))),
                () -> assertFalse(col.remove(Integer.valueOf(4))));
    }

    @Test
    public void testInsertIntoEmpty() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.insert(1, 0);
        assertEquals(1, col.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            col.get(1);
        });
    }
}