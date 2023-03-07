package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

public class ElementsGetterTest {

    @Test
    public void testLinkedListIndexedCollectionElementsGetter() {
        LinkedListIndexedCollection<Integer> col = new LinkedListIndexedCollection<>();
        col.add(1);
        col.add(2);
        col.add(3);

        ElementsGetter<Integer> getter = col.createElementsGetter();

        assertEquals(1, getter.getNextElement());
        assertTrue(getter::hasNextElement);
        assertEquals(2, getter.getNextElement());
        assertTrue(getter::hasNextElement);
        assertEquals(3, getter.getNextElement());
        assertFalse(getter::hasNextElement);
        assertThrows(NoSuchElementException.class, getter::getNextElement);
    }

    @Test
    public void testArrayIndexedCollectionElementsGetter() {
        ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<>();
        col.add(1);
        col.add(2);
        col.add(3);

        ElementsGetter<Integer> getter = col.createElementsGetter();

        assertEquals(1, getter.getNextElement());
        assertTrue(getter::hasNextElement);
        assertEquals(2, getter.getNextElement());
        assertTrue(getter::hasNextElement);
        assertEquals(3, getter.getNextElement());
        assertFalse(getter::hasNextElement);
        assertThrows(NoSuchElementException.class, getter::getNextElement);
    }

}
