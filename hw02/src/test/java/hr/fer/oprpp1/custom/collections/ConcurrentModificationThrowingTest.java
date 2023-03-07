package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentModificationThrowingTest {
    @Test
    public void testAdd() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        col.add("New element");
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testClear() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        col.clear();
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testInsert() {
        ArrayIndexedCollection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        col.insert("New element", 1);
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testRemove() {
        Collection col = new ArrayIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        col.remove("Ivo");
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }

    @Test
    public void testAddLinkedList() {
        Collection col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        col.add("New element");
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testClearLinkedList() {
        Collection col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        col.clear();
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testInsertLinkedList() {
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        col.insert("New element", 1);
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }
    @Test
    public void testRemoveLinkedList() {
        Collection col = new LinkedListIndexedCollection();
        col.add("Ivo");
        col.add("Ana");
        col.add("Jasna");
        ElementsGetter getter = col.createElementsGetter();
        col.remove("Ivo");
        assertThrows(ConcurrentModificationException.class, getter::getNextElement);
    }
}
