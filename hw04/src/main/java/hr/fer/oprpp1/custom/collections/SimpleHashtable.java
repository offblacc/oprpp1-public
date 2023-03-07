package hr.fer.oprpp1.custom.collections;

import java.lang.Math;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A simple hashtable implementation.
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
    /**
     * Slots in the hashtable.
     */
    TableEntry<K, V>[] table;
    /**
     * Number of elements in the hashtable.
     */
    int size;

    /**
     * Number of modifications made to the hashtable.
     */
    private int modificationCount = 0;

    /**
     * The default capacity of the hashtable.
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * The default load factor of the hashtable.
     */
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    /**
     * Constructs a new hashtable with the default capacity.
     */
    public SimpleHashtable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new hashtable with the given capacity.
     * 
     * @param capacity the capacity of the hashtable
     * @throws IllegalArgumentException if the capacity is less than 1
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int capacity) {
        size = 0;
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }

        table = new TableEntry[((int) Math.pow(2, Math.ceil(Math.log(capacity) / Math.log(2))))];
    }

    /**
     * Adds the given key-value pair to the hashtable. If the given key already
     * exists replace the value. If the slot is full add the new entry to the end of
     * the linked list in the slot. Returns the old value if exists, otherwise null.
     * The key can't be null, otherwise NullPointerException will be thrown.
     * 
     * @param key   - the key of the new entry
     * @param value - the value of the new entry
     * @return - the old value if exists, otherwise null
     * @throws NullPointerException if the key is null
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("The key can't be null!");
        }

        if (size / table.length >= DEFAULT_LOAD_FACTOR) {
            resize();
        }

        int pos = Math.abs(key.hashCode()) % table.length;

        if (table[pos] == null) {
            table[pos] = new TableEntry<>(key, value);
            size++;
            modificationCount++;
            return null;
        }

        TableEntry<K, V> entry = table[pos];

        // checks the first in the slot
        if (entry.key.equals(key)) {
            V oldValue = entry.value;
            entry.value = value;
            modificationCount++;
            return oldValue;
        }

        // goes down the linked list in the slot
        while (entry.next != null) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                modificationCount++;
                return oldValue;
            }
            entry = entry.next;
        }
        entry.next = new TableEntry<>(key, value);
        modificationCount++;
        size++;
        return null;
    }

    /**
     * Returns the value of the entry with the given key. If the key doesn't exist
     * returns null. The given key can't be null, otherwise NullPointerException
     * will be thrown. Be careful, if the value is null this will also return null,
     * as it is the value.
     * 
     * @param key - the key of the entry
     * @return - the value of the entry with the given key, otherwise null
     * @throws NullPointerException if the key is null
     */
    public V get(Object key) {
        if (key == null) {
            throw new NullPointerException("The key can't be null!");
        }

        int pos = Math.abs(key.hashCode()) % table.length;

        if (table[pos] == null) {
            return null;
        }

        TableEntry<K, V> entry = table[pos];
        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    /**
     * Returns the number of elements in the hashtable.
     * 
     * @return - the number of elements in the hashtable
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if the hashtable contains the given key, false otherwise.
     * Throws NullPointerException if the given key is null.
     * 
     * @param key - the key whose presence in the hashtable is to be determined.
     * @return - true if the key is in the hashtable, false otherwise
     * @throws NullPointerException if the given key is null
     */
    public boolean containsKey(Object key) {
        if (key == null)
            throw new NullPointerException("The key can't be null!");
        for (int i = 0; i < table.length; i++) {
            var entry = table[i];
            while (entry != null) {
                if (entry.key.equals(key)) {
                    return true;
                }
                entry = entry.next;
            }
        }
        return false;
    }

    /**
     * Returns true if the hashtable contains the given value, false otherwise.
     * 
     * @param value - the value whose presence in the hashtable is to be determined.
     * @return - true if the value is in the hashtable, false otherwise
     */
    public boolean containsValue(Object value) {
        boolean searchNull = (value == null);
        TableEntry<K, V> entry;
        if (searchNull) {
            for (TableEntry<K, V> kvTableEntry : table) {
                entry = kvTableEntry;
                while (entry != null) {
                    if (entry.getValue() == value) {
                        return true;
                    }
                    entry = entry.next;
                }
            }
        } else {
            for (TableEntry<K, V> kvTableEntry : table) {
                entry = kvTableEntry;
                while (entry != null) {
                    if (entry.getValue() != null && entry.getValue().equals(value)) {
                        return true;
                    }
                    entry = entry.next;
                }
            }
        }
        return false;
    }

    /**
     * Removes the entry with the given key from the hashtable, returns the value
     * just before removing the entry. If the key doesn't exist null is returned.
     * Throws NullPointerException if the given key is null.
     * 
     * @param key - the key of the entry to be removed
     * @return - the value of the removed entry, null if the key doesn't exist
     */
    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException("The key can't be null!");
        }

        int pos = Math.abs(key.hashCode()) % table.length;

        if (table[pos] == null) {
            return null;
        }

        TableEntry<K, V> entry = table[pos];
        TableEntry<K, V> prevEntry = null;
        while (entry != null) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                if (prevEntry == null) {
                    table[pos] = entry.next;
                } else if (prevEntry != null) {
                    prevEntry.next = entry.next;
                }
                size--;
                modificationCount++;
                return oldValue;
            }
            prevEntry = entry;
            entry = entry.next;
        }
        return null;
    }

    /**
     * Returns true if the hashtable is empty, false otherwise.
     * 
     * @return - true if the hashtable is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        modificationCount++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (TableEntry<K, V> kvTableEntry : table) {
            TableEntry<K, V> entry = kvTableEntry;
            while (entry != null) {
                sb.append(entry.key).append("=").append(entry.value).append(", ");
                entry = entry.next;
            }
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns an array of entries in the hashtable. The array is of type
     * TableEntry<K, V> and the length of the array is the number of entries in
     * the hashtable. The order of the entries is by slots, and for each slot the
     * order is the same as the order in which the entries were added.
     * 
     * @return - an array of entries in the hashtable
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K, V>[] toArray() {
        TableEntry<K, V>[] array = (TableEntry<K, V>[]) new TableEntry[size];
        int index = 0;
        for (TableEntry<K, V> kvTableEntry : table) {
            TableEntry<K, V> entry = kvTableEntry;
            while (entry != null) {
                array[index++] = entry;
                entry = entry.next;
            }
        }
        return array;
    }

    /**
     * Doubles the size of the hashtable and rehashes all the entries.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        TableEntry<K, V>[] newTable = (TableEntry<K, V>[]) new TableEntry[2 * table.length];
        TableEntry<K, V>[] entries = toArray();

        for (var entry : entries) {
            int index = Math.abs(entry.key.hashCode() % newTable.length);
            TableEntry<K, V> entryWalker = newTable[index];
            if (entryWalker == null) {
                newTable[index] = entry;
            } else {
                while (entryWalker.next != null) {
                    entryWalker = entryWalker.next;
                }
                entryWalker.next = entry;
            }
            entry.next = null;
        }
        table = newTable;
    }

    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl(modificationCount);
    }

    /**
     * Iterator implementation for the SimpleHashtable class.
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
        /**
         * Keeps track of how many objects have been iterated over. Used to determine if
         * all elements
         * have been iterated over.
         */
        int no_iterated = 0;

        /**
         * Keeps track of the current slot in the table during the iteration.
         */
        int current_slot;

        /**
         * Keeps track of the current entry in the table during the iteration.
         */
        TableEntry<K, V> current_entry;

        /**
         * Reference to the previously iterated-over entry. Used when removing entries
         * to re-link the list in a particular slot.
         */
        TableEntry<K, V> prev_entry;

        /**
         * Keeps track whether the table has been modified since the last next() call,
         * as
         * only one remove call is allowed per one next() call.
         */
        boolean removedThisIteration = false;

        /**
         * Keeps track of the modification count of the table at the time of the
         * iterator
         * creation.
         */
        private int savedModificationCount;

        private IteratorImpl(int modificationCount) {
            savedModificationCount = modificationCount;
        }

        /**
         * Returns true if the iteration has more elements, false otherwise.
         */
        public boolean hasNext() {
            if (savedModificationCount != modificationCount) {
                throw new ConcurrentModificationException("The hashtable was modified!");
            }
            return no_iterated < size; // size == SimpleHashtable.this.size
        }

        /**
         * Returns the next element in the iteration.
         * Throws NoSuchElementException if the iteration has no more elements.
         * 
         * @return - the next element in the iteration
         * @throws NoSuchElementException - if the iteration has no more elements
         */
        @SuppressWarnings({"rawtypes", "unchecked"})
        public SimpleHashtable.TableEntry next() {
            if (savedModificationCount != modificationCount) {
                throw new ConcurrentModificationException("The hashtable was modified!");
            }
            removedThisIteration = false;
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (current_entry == null) {
                while (current_slot < table.length && table[current_slot] == null) {
                    current_slot++;
                }
                prev_entry = current_entry;
                current_entry = table[current_slot];
            } else {
                prev_entry = current_entry;
                current_entry = current_entry.next;
                if (current_entry == null) {
                    current_slot++;
                    while (current_slot < table.length && table[current_slot] == null) {
                        current_slot++;
                    }
                    current_entry = table[current_slot];
                    prev_entry = current_entry;
                }
            }
            no_iterated++;
            return current_entry;
        }

        /**
         * Removes the last element returned by this iterator from the hashtable, making
         * sure that the iterator is not left in an invalid state, essentially making
         * sure the iterator doesn't break, by doing a controlled remove. The method can
         * be called only once per call to next(). The method throws
         * IllegalStateException if the next method has not yet been called, or the
         * remove method has already been called after the last call to the next method.
         * 
         * @throws IllegalStateException - if the next method has not yet been called,
         *                               or the remove method has already been called
         */
        public void remove() {
            if (current_entry == null) {
                throw new IllegalStateException("next() hasn't been called yet, nothing to remove");
            }
            if (removedThisIteration) {
                throw new IllegalStateException("remove() has already been called this iteration");
            }
            SimpleHashtable.this.remove(current_entry.key);
            savedModificationCount++;
            current_entry = prev_entry;
            removedThisIteration = true;
            no_iterated--;
        }
    }

    /**
     * Slot in the hashtable. Each slot can contain multiple elements - a linked
     * list.
     */
    public static class TableEntry<K, V> {
        private final K key;
        private V value;
        private TableEntry<K, V> next;

        /**
         * Constructs a new table entry with the given key and value.
         * 
         * @param key   the key of the entry
         * @param value the value of the entry
         */
        public TableEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key of the entry.
         * 
         * @return Key of the entry.
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value of the entry.
         * 
         * @return Value of the entry.
         */
        public V getValue() {
            return value;
        }

        /**
         * Sets the value of the entry.
         * 
         * @param value New value of the entry.
         */
        public void setValue(V value) {
            this.value = value;
        }
    }
}
