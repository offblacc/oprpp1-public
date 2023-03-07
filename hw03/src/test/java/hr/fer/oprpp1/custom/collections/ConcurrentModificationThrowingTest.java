package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentModificationThrowingTest {
    @Test
    public void testAdd() {
        Collection<String> col = new ArrayIndexedCollection<>();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter<String> getter = col.createElementsGetter();
        col.add("New element");
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testClear() {
        Collection<String> col = new ArrayIndexedCollection<>();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter<String> getter = col.createElementsGetter();
        col.clear();
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testInsert() {
        ArrayIndexedCollection<String> col = new ArrayIndexedCollection<>();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter<String> getter = col.createElementsGetter();
        col.insert("New element", 1);
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testRemove() {
        Collection<String> col = new ArrayIndexedCollection<>();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter<String> getter = col.createElementsGetter();
        col.remove("Ivo");
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }

    @Test
    public void testAddLinkedList() {
        Collection<String> col = new LinkedListIndexedCollection<>();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter<String> getter = col.createElementsGetter();
        col.add("New element");
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testClearLinkedList() {
        Collection<String> col = new LinkedListIndexedCollection<>();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter<String> getter = col.createElementsGetter();
        col.clear();
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testInsertLinkedList() {
        LinkedListIndexedCollection<String> col = new LinkedListIndexedCollection<>();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter<String> getter = col.createElementsGetter();
        col.insert("New element", 1);
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testRemoveLinkedList() {
        Collection<String> col = new LinkedListIndexedCollection<>();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter<String> getter = col.createElementsGetter();
        col.remove("Ivo");
        assertThrows(ConcurrentModificationException.class, getter::getNextElement);
    }
}
