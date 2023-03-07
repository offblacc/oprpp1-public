package hr.fer.oprpp1.custom.collections;

/**
 * Class that represents a stack structure, last-in-first-out (LIFO) principle.
 * Implemented using an ArrayIndexedCollection.
 */
public class ObjectStack<T> {
    /**
     * ArrayIndexedCollection used to store the elements of the stack.
     */
    final ArrayIndexedCollection<T> array;

    /**
     * Default constructor. Creates an empty stack.
     */
    public ObjectStack() {
        array = new ArrayIndexedCollection<>();
    }

    /**
     * Checks if the stack is empty.
     * Empty stack means it contains no elements.
     * 
     * @return true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return array.isEmpty();
    }

    /**
     * Returns the number of elements currently stored in the stack.
     * 
     * @return the number of elements currently stored in the stack.
     */
    public int size() {
        return array.size();
    }

    /**
     * Pushes the given value onto the stack.
     * 
     * @param value - value to be pushed onto the stack.
     */
    public void push(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        array.add(value);
    }

    /**
     * Removes the last value pushed onto the stack and returns it.
     * 
     * @return the last value pushed onto the stack.
     * @throws EmptyStackException if the stack is empty
     */
    public Object pop() {
        if (array.size() == 0) {
            throw new EmptyStackException();
        }

        T object = array.get(array.size() - 1);
        array.remove(array.size() - 1);
        return object;
    }

    /**
     * Returns the last value pushed onto the stack without removing it, 'peeking'
     * at it.
     * Like pop(), only without removing it.
     * 
     * @return the last value pushed onto the stack.
     */
    public Object peek() {
        if (array.size() == 0) {
            throw new EmptyStackException();
        }

        return array.get(array.size() - 1);
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        array.clear();
    }
}
