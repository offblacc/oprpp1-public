package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Class that represents a resizable array-backed collection of objects.
 * Duplicate elements are allowed. Storage of null references, however, is not
 * allowed.
 * The order of elements is determined by the order in which the elements were
 * added.
 */
public class ArrayIndexedCollection implements List {
    /**
     * Number of elements currently stored in the collection.
     */
    private int size;
    /**
     * Array in which the elements of the collection are stored.
     */
    private Object[] elements;

    /**
     * Keeping track of every modification of the collection, so that the iterator
     * can throw an exception if the collection is modified while the iterator is in
     * use.
     */
    private int modificationCount = 0;

    /**
     * Default constructor. Sets the initial capacity to 16.
     */
    public ArrayIndexedCollection() {
        this(16);
    }

    /**
     * Constructor that sets the initial capacity to the given value.
     * 
     * @param initialCapacity - initial capacity of the collection. Must be at least
     *                        one.
     * @throws IllegalArgumentException if the given initial capacity is less than
     *                                  one.
     */
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Array size should be at least 1!");
        }

        size = 0;
        elements = new Object[initialCapacity];
    }

    /**
     * Constructor that makes a new collection from a given one.
     * 
     * @param other - collection to be copied.
     * @throws NullPointerException if the given collection is null.
     */
    public ArrayIndexedCollection(Collection other) {
        this(other, 16);
    }

    /**
     * Constructor that makes a new collection from a given one with a given initial
     * capacity.
     * If the given capacity is less than the size of the given collection, the
     * capacity is set
     * to the size of the given collection.
     * 
     * @param other           - collection to be copied.
     * @param initialCapacity - initial capacity of the collection. Must be at least
     *                        one.
     * @throws NullPointerException     if the given collection is null.
     * @throws IllegalArgumentException if the given initial capacity is less than
     *                                  one.
     */
    public ArrayIndexedCollection(Collection other, int initialCapacity) { // double check this
        if (other == null) {
            throw new NullPointerException();
        }

        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Array size should be at least 1!");
        }

        if (initialCapacity < other.size()) {
            initialCapacity = other.size();
        }

        elements = new Object[initialCapacity];
        addAll(other);
    }

    /**
     * Returns the number of elements in the collection.
     * 
     * @return number of elements in the collection.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given object to the collection.
     * 
     * @param value - object to be added to the collection.
     * @throws NullPointerException if the given object is null.
     */
    @Override
    public void add(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }

        int i = 0;
        if (size == elements.length) {
            Object[] newElements = new Object[2 * elements.length];
            for (Object v : elements) {
                newElements[i++] = v;
            }
            elements = newElements;
        }
        modificationCount++;
        elements[size++] = value;
    }

    /**
     * Returns true if the collection contains the given object.
     * 
     * @param value - object to be checked if it is in the collection.
     * @return true if the collection contains the given object, false otherwise.
     */
    @Override
    public boolean contains(Object value) {
        if (value == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index of the given object in the collection.
     * 
     * @param value - object to be checked for its index.
     * @return index of the given object in the collection.
     */
    public int indexOf(Object value) {
        if (value == null) {
            return -1;
        }

        for (int i = 0; i < size; i++) {
            if (elements[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns an array of objects in the collection.
     * 
     * @return array of objects in the collection.
     */
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = elements[i];
        }
        return arr;
    }

    /**
     * Empties the collection.
     */
    @Override
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) {
                break;
            }
            elements[i] = null;
        }
        modificationCount++;
        size = 0;
    }

    /**
     * Returns the object at the given index in the collection.
     * 
     * @param index - index of the object to be returned.
     * @return object at the given index in the collection.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     */
    public Object get(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }

        return elements[index];
    }

    /**
     * Removes the first occurrence of the object given it's value from the
     * collection.
     * 
     * @param value - value of the object to be removed.
     * @return true if the object was removed, false otherwise, in case it was not
     *         in the collection.
     */
    @Override
    public boolean remove(Object value) {
        int index = indexOf(value);

        if (index == -1) {
            return false;
        }
        shiftLeft(index);
        size--;
        modificationCount++;
        return true;
    }

    /**
     * Removes the object at the given index from the collection.
     * 
     * @param index - index of the object to be removed.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     */
    public void remove(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        shiftLeft(index);
        modificationCount++;
        size--;
    }

    /**
     * Shifts the elements of the collection to the left, starting from the given
     * index.
     * Used by methods that remove elements from the collection.
     * 
     * @param index - starting index of the shift. The element at index will be
     *              replaced by it's right neighbor.
     */
    private void shiftLeft(int index) {
        index++;
        for (; index < size; index++) {
            elements[index - 1] = elements[index];
        }
        elements[size - 1] = null;
    }

    /**
     * Inserts the given object at the given position in the collection.
     * 
     * @param value    - object to be inserted.
     * @param position - position at which the object will be inserted.
     * @throws NullPointerException      if the given object is null.
     * @throws IndexOutOfBoundsException if the given position is out of bounds.
     */
    public void insert(Object value, int position) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException();
        }

        if (value == null) {
            throw new NullPointerException();
        }

        if (elements[elements.length - 1] != null) {
            Object[] newElements = new Object[2 * elements.length];
            int i = 0;
            for (Object v : elements) {
                newElements[i++] = v;
            }
            elements = newElements;
        }

        for (int i = size; i > position; i--) {
            elements[i] = elements[i - 1];
        }

        elements[position] = value;
        size++;
        modificationCount++;
    }

    /**
     * Adds all elements of the given collection to this collection.
     * 
     * @param other - collection whose elements will be added to this collection.
     *              Collection other is not modified.
     */
    @Override
    public void addAll(Collection other) {
        class LocalProcessor implements Processor {
            public void process(Object value) {
                add(value);
            }
        }
        other.forEach(new LocalProcessor());
    }

    private static class ArrayIndexedCollectionElementsGetter implements ElementsGetter {
        /**
         * Initially equal to the number of elements in the collection. It is decreased
         * by 1 whenever the getNextElement() method is called. When it reaches 0 we
         * have iterated through all elements.
         */
        private int indexReverse;

        /**
         * Reference to the collection whose elements we are iterating through.
         */
        private final ArrayIndexedCollection col;

        /**
         * Modification count of the collection instance at the time of ElementsGetter
         * creation.
         * Used to check for concurrent modification.
         */
        private final int savedModificationCount;

        /**
         * Constructor for the ArrayIndexedCollectionElementsGetter class.
         * 
         * @param indexReverse           - initially equal to the number of elements in
         *                               the
         *                               collection, counting down to 0. When it reaches
         *                               zero we
         *                               have iterated through all elements.
         * @param col                    - reference to collection whose elements we are
         *                               iterating
         *                               through.
         *
         * @param savedModificationCount - modification count of the collection instance
         *
         */
        private ArrayIndexedCollectionElementsGetter(ArrayIndexedCollection col, int size, int modificationCount) {
            indexReverse = size;
            this.col = col;
            savedModificationCount = modificationCount;
        }

        /**
         * Returns true if the collection contains at least one more element, false
         * otherwise.
         * 
         * @return true if the collection contains at least one more element, false
         *         otherwise.
         * @throws ConcurrentModificationException if the collection has been modified
         *                                         since creation of the ElementsGetter.
         */
        @Override
        public boolean hasNextElement() {
            if (savedModificationCount != col.modificationCount) {
                throw new ConcurrentModificationException();
            }
            return indexReverse > 0;
        }

        /**
         * Returns the next element in the collection.
         * 
         * @return the next element in the collection.
         * @throws NoSuchElementException          if the collection contains no more
         *                                         elements,
         *                                         but the next one was requested by
         *                                         calling this
         *                                         method.
         * @throws ConcurrentModificationException if the collection has been modified
         *                                         since creation of the ElementsGetter.
         */
        @Override
        public Object getNextElement() {
            if (savedModificationCount != col.modificationCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNextElement()) {
                throw new NoSuchElementException();
            }
            return col.elements[col.size - (indexReverse--)];
        }

    }

    /**
     * Creates a new ElementsGetter for this collection.
     * 
     * @return new ElementsGetter for this collection.
     */
    @Override
    public ElementsGetter createElementsGetter() {
        return new ArrayIndexedCollectionElementsGetter(this, size, modificationCount);
    }
}
