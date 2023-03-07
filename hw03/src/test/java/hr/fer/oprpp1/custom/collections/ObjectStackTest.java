package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectStackTest {
    @Test
    public void testConstructor() {
        ObjectStack<Integer> stack = new ObjectStack<>();
        assertEquals(0, stack.size());
    }

    @Test
    public void testIsEmpty() {
        ObjectStack<Integer> stack = new ObjectStack<>();
        assertTrue(stack.isEmpty());
        stack.push(1);
        assertFalse(stack.isEmpty());
    }

    @Test
    public void testSize() {
        ObjectStack<Integer> stack = new ObjectStack<>();
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
        ObjectStack<Integer> stack = new ObjectStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
    }

    @Test
    public void testPop() {
        ObjectStack<Integer> stack = new ObjectStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    public void testPopFromEmptyStack() {
        ObjectStack<Integer> stack = new ObjectStack<>();
        assertThrows(EmptyStackException.class, () -> stack.pop());
    }

    @Test
    public void testPeek() {
        ObjectStack<Integer> stack = new ObjectStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.peek());
        assertEquals(3, stack.peek());
        assertEquals(3, stack.peek());
    }

    @Test
    public void testPeekFromEmptyStack() {
        ObjectStack<Integer> stack = new ObjectStack<>();
        assertThrows(EmptyStackException.class, () -> stack.peek());
    }

    @Test
    public void testClear() {
        ObjectStack<Integer> stack = new ObjectStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.clear();
        assertEquals(0, stack.size());
        assertThrows(EmptyStackException.class, () -> stack.peek());
    }

}
