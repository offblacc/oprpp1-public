package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectStackTest {
    @Test
    public void testConstructor() {
        ObjectStack stack = new ObjectStack();
        assertEquals(0, stack.size());
    }

    @Test
    public void testIsEmpty() {
        ObjectStack stack = new ObjectStack();
        assertTrue(stack.isEmpty());
        stack.push(1);
        assertFalse(stack.isEmpty());
    }

    @Test
    public void testSize() {
        ObjectStack stack = new ObjectStack();
        assertEquals(0, stack.size());
        stack.push(1);
        assertEquals(1, stack.size());
        stack.push(2);
        assertEquals(2, stack.size());
        stack.push(3);
        assertEquals(3, stack.size());
    }

    @Test
    public void testPush() {
        ObjectStack stack = new ObjectStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
    }

    @Test
    public void testPop() {
        ObjectStack stack = new ObjectStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    public void testPopFromEmptyStack() {
        ObjectStack stack = new ObjectStack();
        assertThrows(EmptyStackException.class, () -> stack.pop());
    }

    @Test
    public void testPeek() {
        ObjectStack stack = new ObjectStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.peek());
        assertEquals(3, stack.peek());
        assertEquals(3, stack.peek());
    }

    @Test
    public void testPeekFromEmptyStack() {
        ObjectStack stack = new ObjectStack();
        assertThrows(EmptyStackException.class, () -> stack.peek());
    }

    @Test
    public void testClear() {
        ObjectStack stack = new ObjectStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.clear();
        assertEquals(0, stack.size());
        assertThrows(EmptyStackException.class, () -> stack.peek());
    }

}
