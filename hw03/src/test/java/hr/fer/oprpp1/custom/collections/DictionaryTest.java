package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {
    @Test
    public void testConstructor() {
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        assertEquals(0, dictionary.size());
        assertTrue(dictionary.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        Dictionary<String, Integer> dict = new Dictionary<>();
        assertTrue(dict.isEmpty());
        dict.put("a", 1);
        assertFalse(dict.isEmpty());
    }

    @Test
    public void testSize() {
        Dictionary<String, Integer> dict = new Dictionary<>();
        assertEquals(0, dict.size());
        dict.put("a", 1);
        assertEquals(1, dict.size());
        dict.put("b", 2);
        assertEquals(2, dict.size());
        dict.put("c", 3);
        assertEquals(3, dict.size());
    }

    @Test
    public void testClear() {
        Dictionary<String, Integer> dict = new Dictionary<>();
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 3);
        dict.clear();
        assertEquals(0, dict.size());
    }

    @Test
    public void testPut() {
        Dictionary<String, Integer> dict = new Dictionary<>();
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 3);
        assertEquals(3, dict.size());
        assertEquals(1, dict.get("a"));
        assertEquals(2, dict.get("b"));
        assertEquals(3, dict.get("c"));
    }

    @Test
    public void testPutNullAsKey() {
        Dictionary<String, Integer> dict = new Dictionary<>();
        assertThrows(NullPointerException.class, () -> dict.put(null, 1));
    }

    @Test
    public void testNullAsValue() {
        Dictionary<String, Integer> dict = new Dictionary<>();
        dict.put("a", null);
        assertNull(dict.get("a"));
    }

    @Test
    public void testRemove() {
        Dictionary<String, Integer> dict = new Dictionary<>();
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 3);
        assertEquals(2, dict.remove("b"));
        assertEquals(2, dict.size());
        assertEquals(1, dict.get("a"));
        assertEquals(3, dict.get("c"));
        assertNull(dict.get("b"));
        assertNull(dict.get("foo"));
    }

}
