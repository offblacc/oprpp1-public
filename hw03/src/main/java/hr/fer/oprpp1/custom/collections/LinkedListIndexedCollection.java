package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Class that represents a resizable linked list collection.
 * Duplicate elements are allowed. Storage of null references, however, is not
 * allowed.
 * The order of elements is determined by the order in which the elements were
 * added.
 */
public class LinkedListIndexedCollection<T> implements List<T> {
    /**
     * Number of elements currently stored in the collection.
     */
    private int size;
    /**
     * Reference to the first node in the collection.
     */
    private ListNode<T> first;
    /**
     * Reference to the last node in the collection.
     */
    private ListNode<T> last;

    /**
     * Keeping track of every modification of the collection, so that the iterator
     * can throw an exception if the collection is modified while the iterator is in
     * use.
     */
    private int modificationCount = 0;

    /**
     * Default constructor. Sets endnodes to null and size to 0.
     */
    public LinkedListIndexedCollection() {
        first = last = null;
        size = 0;
    }

    /**
     * Constructor that makes a new collection from a given one.
     * 
     * @param other - collection to be copied. The given collection will not be
     *              modified.
     * @throws NullPointerException if the given collection is null.
     */
    public LinkedListIndexedCollection(Collection<? extends T> other) {
        if (other == null) {
            throw new NullPointerException();
        }
        addAll(other);
    }

    /**
     * Returns the number of elements currently stored in the collection.
     * 
     * @return the number of elements currently stored in the collection.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given object to the collection, appending it at the end.
     * 
     * @param value - object to be added to the collection.
     * @throws NullPointerException if the given object is null.
     */
    @Override
    public void add(T value) {
        if (value == null)
            throw new NullPointerException();

        ListNode<T> newNode = new ListNode<>(value);
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
        }
        last = newNode;
        size++;
        modificationCount++;
    }

    /**
     * Inserts the given value at the given position in the collection.
     * 
     * @param value    - value to be inserted.
     * @param position - position at which the value is to be inserted. Must be
     *                 between 0 and size.
     * @throws NullPointerException      if the given value is null.
     * @throws IndexOutOfBoundsException if the given position is not between 0 and
     *                                   size.
     */
    public void insert(T value, int position) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException();
        }

        if (value == null) {
            throw new NullPointerException();
        }

        if (position == size) {
            add(value);
            return;
        }

        ListNode<T> newNode = new ListNode<>(value);
        for (ListNode<T> node = first; node != null; node = node.next) {
            if (position == 0) {
                newNode.next = node;
                newNode.prev = node.prev;
                if (node.prev != null) {
                    node.prev.next = newNode;
                }
                node.prev = newNode;
                if (node == first) {
                    first = newNode;
                }
                break;
            }
            position--;
        }
        size++;
        modificationCount++;
    }

    /**
     * Returns the object that is stored in the collection at the given position.
     * Average complexity is O(n/2).
     * 
     * @param index - position of the object to be returned. Must be between 0 and
     *              size-1.
     * @return the object that is stored in the collection at the given position.
     * @throws IndexOutOfBoundsException if the given position is not between 0 and
     *                                   size-1.
     */
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }

        ListNode<T> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.value;
    }

    /**
     * Returns the index of the first occurrence of the given value.
     * Returns -1 if the value is not found.
     * 
     * @param value - value to be searched for.
     * @return the index of the first occurrence of the given value or -1 if the
     *         value is not found.
     */
    public int indexOf(T value) {
        if (value == null) {
            return -1;
        }
        ListNode<T> node = first;
        for (int i = 0; node != null; i++, node = node.next) {
            if (node.value.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks whether the collection contains the given object.
     * 
     * @param value - object to be searched for.
     * @return true if the collection contains the given object, false otherwise.
     */
    @Override
    public boolean contains(T value) {
        ListNode<T> node = first;
        while (node != null) {
            if (node.value == value) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    /**
     * Removes the first occorrence of the given object from the collection.
     * 
     * @param value - object to be removed.
     * @return true if the object was found and removed, false otherwise.
     */
    @Override
    public boolean remove(Object value) {
        ListNode<T> node = first;
        while (node != null) {
            if (node.value == value) {
                if (node.prev != null) {
                    (node.prev).next = node.next;
                }
                if (node.next != null) {
                    (node.next).prev = node.prev;
                }
                size--;
                modificationCount++;
                return true;
            }

            node = node.next;
        }
        return false;
    }

    /**
     * Removes the object at the given position from the collection.
     * 
     * @param index - position of the object to be removed. Must be between 0 and
     *              size - 1.
     * @throws IndexOutOfBoundsException if the given position is not between 0 and
     *                                   size - 1.
     */
    public void remove(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }

        ListNode<T> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }

        if (node.prev != null) {
            (node.prev).next = node.next;
        }
        if (node.next != null) {
            (node.next).prev = node.prev;
        }
        modificationCount++;
        size--;
    }

    /**
     * Allocates new array with size set to the size of the collection, fills it
     * with
     * collection content and returns the newly created array.
     * 
     * @return array of objects from this collection.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] arr = (T[]) new Object[size];
        ListNode<T> node = first;
        int i = 0;
        while (node != null) {
            arr[i++] = node.value;
            node = node.next;
        }
        return arr;
    }

    /**
     * Adds all elements of the given collection to this collection.
     * Collection other is not modified.
     * 
     * @param other - collection whose elements are to be added to this collection.
     */
    @Override
    public void addAll(Collection<? extends T> other) {
        class LocalProcessor implements Processor<T> {
            public void process(T value) {
                add(value);
            }
        }
        other.forEach(new LocalProcessor());
    }

    /**
     * Removes all elements from this collection, setting first and last node
     * references to null, letting the garbage collector clean it up, essentially
     * emptying the collection or 'forgetting' about it.
     */
    @Override
    public void clear() {
        first = last = null;
        size = 0;
        modificationCount++;
    }

    /**
     * Local class that represents a node in a linked list.
     * Each node has a reference to the previous and next node in the list.
     */
    private static class ListNode<T> {
        /**
         * Reference to the previous node in the list.
         */
        private ListNode<T> prev;
        /**
         * Reference to the next node in the list.
         */
        private ListNode<T> next;
        /**
         * Value of the node.
         */
        private final T value;

        /**
         * Default constructor, setting all references and the node's value to null.
         */
        private ListNode() {
            this(null, null, null);
        }

        /**
         * Constructor that sets the node's value to the given value.
         * The previous and next node references are set to null.
         * 
         * @param value - value of the node.
         */
        private ListNode(T value) {
            this(null, null, value);
        }

        /**
         * Constructor that sets the previous and next node references to the given
         * values, and the node's value to the given value.
         * 
         * @param prev  - reference to the previous node in the list.
         * @param next  - reference to the next node in the list.
         * @param value - value of the node.
         */
        private ListNode(ListNode<T> prev, ListNode<T> next, T value) {
            this.prev = prev;
            this.next = next;
            this.value = value;
        }

        /**
         * Constructor that given a ListNode creates a new one with the same parameters.
         * 
         * @param node - the node to be copied into a new one.
         */
        private ListNode(ListNode<T> node) {
            this(node.prev, node.next, node.value);
        }
    }

    /**
     * Local class that implements ElementsGetter interface.
     * It is used to iterate over the collection returning elements one by one.
     */
    private static class LinkedListElementsGetter<T> implements ElementsGetter<T> {
        /**
         * Node that is the current position of the iterator.
         */
        private ListNode<T> node;

        /**
         * Modification count of the collection instance at the time of ElementsGetter
         * creation.
         */
        private final int savedModificationCount;

        /**
         * Reference to the collection instance we're iterating through.
         */
        private final LinkedListIndexedCollection<T> col;

        /**
         * Constructor that sets the current position of the iterator to the first
         * node in the list storing it's reference.
         * 
         * @param first - first node in the list.
         */
        private LinkedListElementsGetter(LinkedListIndexedCollection<T> col, ListNode<T> first, int modificationCount) {
            node = new ListNode<>(null, first, null);
            savedModificationCount = modificationCount;
            this.col = col;
        }

        /**
         * Returns true if the collection contains at least one more element, false
         * otherwise.
         * 
         * @return true if there are more elements, false otherwise.
         * @throws ConcurrentModificationException if the collection has been modified
         *                                         since creation of the ElementsGetter.
         */
        @Override
        public boolean hasNextElement() {
            if (savedModificationCount != col.modificationCount) {
                throw new ConcurrentModificationException();
            }
            return node.next != null;
        }

        /**
         * Returns the next element in the collection.
         * 
         * @return next element in the collection.
         * @throws NoSuchElementException          if there are no more elements in the
         *                                         collection.
         * @throws ConcurrentModificationException if the collection has been modified
         *                                         since creation of the ElementsGetter.
         */
        @Override
        public T getNextElement() {
            if (savedModificationCount != col.modificationCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNextElement()) {
                throw new NoSuchElementException();
            }
            node = node.next;
            return node.value;
        }

    }

    /**
     * Creates a new ElementsGetter for this collection.
     * 
     * @return ElementsGetter for this collection.
     */
    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new LinkedListElementsGetter<>(this, first, modificationCount);
    }
}