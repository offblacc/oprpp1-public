package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class SimpleHashtableTest {
    @Test
    public void testExample() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        // query collection:
        Integer kristinaGrade = examMarks.get("Kristina");
        assertEquals(5, kristinaGrade);
        assertEquals(4, examMarks.size());
    }

    @Test
    public void testDefaultConstructor() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        assertEquals(0, examMarks.size());
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5);

        Integer kristinaGrade = examMarks.get("Kristina");
        assertEquals(5, kristinaGrade);
        assertEquals(4, examMarks.size());
    }

    @Test
    public void testCapacityConstructor() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        assertEquals(0, examMarks.size());
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5);

        Integer kristinaGrade = examMarks.get("Kristina");
        assertEquals(5, kristinaGrade);
        assertEquals(4, examMarks.size());
    }

    @Test
    public void testIllegalCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<>(0));
        assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<>(-1));
    }

    @Test
    public void testPut() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        assertEquals(3, examMarks.size());
    }

    @Test
    public void testPutKeyNull() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        assertThrows(NullPointerException.class, () -> examMarks.put(null, 2));
    }

    @Test
    public void testPutValueNull() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", null);
        assertEquals(1, examMarks.size());
        assertNull(examMarks.get("Ivana"));
    }

    @Test
    public void testPutExistingKey() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ivana", 5);
        assertEquals(1, examMarks.size());
        assertEquals(5, examMarks.get("Ivana"));
    }

    @Test
    public void testPutExistingKeyInABigHashtable() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        for (int i = 0; i < 1000; i++) {
            examMarks.put("Ivana" + i, i + 1);
        }
        assertEquals(1000, examMarks.size());
        examMarks.put("Ivana0", 0);
        assertEquals(1000, examMarks.size());
        assertEquals(0, examMarks.get("Ivana0"));
    }

    @Test
    public void testGet() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", null);
        assertEquals(2, examMarks.get("Ivana"));
        assertEquals(2, examMarks.get("Ante"));
        assertEquals(2, examMarks.get("Jasna"));
        assertNull(examMarks.get("Kristina"));
    }

    @Test
    public void testGetNull() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        assertThrows(NullPointerException.class, () -> examMarks.get(null));
    }

    @Test
    public void testResize() { // not a direct test, but should prove that resize works properly
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        for (int i = 0; i < 1000; i++) {
            examMarks.put("Ivana" + i, i + 1);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(i + 1, examMarks.get("Ivana" + i));
        }
    }

    @Test
    public void testRemove() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        assertEquals(2, examMarks.remove("Ivana"));
        assertEquals(2, examMarks.size());
        assertNull(examMarks.get("Ivana"));
    }

    @Test
    public void testSize() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        for (int i = 0; i < 1000; i++) {
            examMarks.put("Ivana" + i, i + 1);
            assertEquals(i + 1, examMarks.size());
        }

        for (int i = 0; i < 1000; i++) {
            examMarks.remove("Ivana" + i);
            assertEquals(1000 - i - 1, examMarks.size());
        }
    }

    @Test
    public void testContainsKey() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        assertTrue(examMarks.containsKey("Ivana"));
        assertTrue(examMarks.containsKey("Ante"));
        assertTrue(examMarks.containsKey("Jasna"));
        assertFalse(examMarks.containsKey("Kristina"));
        assertThrows(NullPointerException.class, () -> examMarks.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Ivan", null);
        assertTrue(examMarks.containsValue(2));
        assertFalse(examMarks.containsValue(5));
        examMarks.remove("Ivan");
        assertFalse(examMarks.containsValue(null));
        examMarks.put("Ivan", null);
        assertTrue(examMarks.containsValue(null));
    }

    @Test
    public void testIsEmpty() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        assertTrue(examMarks.isEmpty());
        examMarks.put("Ivana", 2);
        assertFalse(examMarks.isEmpty());
        examMarks.remove("Ivana");
        assertTrue(examMarks.isEmpty());
    }

    @Test
    public void assertClear() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.clear();
        assertEquals(0, examMarks.size());
        assertFalse(examMarks.containsKey("Ivana"));
        assertFalse(examMarks.containsKey("Ante"));
        assertFalse(examMarks.containsKey("Jasna"));
    }

    @Test
    public void testToString() {
        // should be "[key1=value1, key2=value2, key3=value3]"
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        assertEquals("[Ivana=2, Ante=2, Jasna=2]", examMarks.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testToArray() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);
        Object[] array = examMarks.toArray();
        assertEquals(3, array.length);
        assertEquals("Ivana", ((SimpleHashtable.TableEntry<String, Integer>) array[0]).getKey());
        assertEquals("Ante", ((SimpleHashtable.TableEntry<String, Integer>) array[1]).getKey());
        assertEquals("Jasna", ((SimpleHashtable.TableEntry<String, Integer>) array[2]).getKey());

        assertEquals(2, ((SimpleHashtable.TableEntry<String, Integer>) array[0]).getValue());
        assertEquals(3, ((SimpleHashtable.TableEntry<String, Integer>) array[1]).getValue());
        assertEquals(4, ((SimpleHashtable.TableEntry<String, Integer>) array[2]).getValue());
    }

    @Test
    public void testFromPDF2() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        StringBuilder sb = new StringBuilder();
        for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
            sb.append(pair.getKey()).append(" => ").append(pair.getValue()).append('\n');
        }
        assertEquals("Ante => 2\nIvana => 5\nJasna => 2\nKristina => 5", sb.toString().trim());
    }

    @Test
    public void testIteratorLargeTable() { // large number of entries -> surely more than one slot and some filled slots
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        for (int i = 0; i < 10000; i++) {
            examMarks.put("Ivana" + i, i + 1);
        }
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        int sum = 0;
        int count = 0;
        while (iterator.hasNext()) {
            SimpleHashtable.TableEntry<String, Integer> pair = iterator.next();
            sum += pair.getValue();
            count++;
        }
        assertFalse(iterator.hasNext());
        assertEquals(10000, count);
        assertEquals(50005000, sum);
    }

    @Test
    public void testIteratorRemove() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        assertThrows(IllegalStateException.class, () -> iterator.remove());
        var cur = iterator.next();
        iterator.remove();
        assertEquals(2, examMarks.size());
        assertFalse(examMarks.containsKey(cur.getKey()));
        assertThrows(IllegalStateException.class, () -> iterator.remove());

        cur = iterator.next();
        iterator.remove();
        assertEquals(1, examMarks.size());
        assertFalse(examMarks.containsKey(cur.getKey()));
        assertThrows(IllegalStateException.class, () -> iterator.remove());

        cur = iterator.next();
        iterator.remove();
        assertEquals(0, examMarks.size());
        assertFalse(examMarks.containsKey(cur.getKey()));
        assertThrows(IllegalStateException.class, () -> iterator.remove());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorRemove2() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        iterator.next();
        iterator.next();
        iterator.remove();
        iterator.next();
        iterator.remove();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorRemove3() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.remove();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorRemove4() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        iterator.next();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
        iterator.remove();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testConcurrentModificationException() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        iterator.next();
        iterator.next();
        examMarks.put("Kristina", 5);
        assertThrows(ConcurrentModificationException.class, () -> iterator.next());
    }

    @Test
    public void testConcurrentModificationException2() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        iterator.next();
        iterator.next();
        examMarks.remove("Ante");
        assertThrows(ConcurrentModificationException.class, () -> iterator.next());
    }

    @Test
    public void testConcurrentModificationException3() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        iterator.next();
        iterator.next();
        iterator.remove();
        iterator.next(); // should not throw exception -> test passed if no exception thrown
    }

    @Test
    public void testConcurrentModificationException4() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator = examMarks.iterator();
        iterator.next();
        iterator.next();
        examMarks.clear();
        assertThrows(ConcurrentModificationException.class, () -> iterator.next());
    }
}
