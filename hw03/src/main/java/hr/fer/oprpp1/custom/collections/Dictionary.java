package hr.fer.oprpp1.custom.collections;

/**
 * A dictionary is a data structure that stores pairs of objects, key-and-value
 * pairs. The dictionary is indexed by the keys, which are unique. Each key can
 * map to at most one value.
 */
public class Dictionary<K, V> {
    /**
     * The underlying collection that stores the key-value pairs.
     */
    final ArrayIndexedCollection<Entry<K, V>> col;

    /**
     * Creates a new dictionary.
     */
    public Dictionary() {
        col = new ArrayIndexedCollection<>();
    }

    /**
     * Returns true if the dictionary contains no entries, false otherwise.
     * 
     * @return true if the dictionary contains no entries, false otherwise
     */
    public boolean isEmpty() {
        return col.isEmpty();
    }

    /**
     * Returns the number of entries in the dictionary.
     * 
     * @return the number of entries in the dictionary
     */
    public int size() {
        return col.size();
    }

    /**
     * Removes all entries from the dictionary.
     */
    public void clear() {
        col.clear();
    }

    /**
     * Adds a new entry to the dictionary. If the given key already exists in the
     * dictionary, the old value is replaced with the given value. Returns the 
     * old value.
     * 
     * @param key   the key of the new entry
     * @param value the value of the new entry
     */
    public V put(K key, V value) {
        if (key == null)
            throw new NullPointerException("Key cannot be null");
        for (int i = 0; i < col.size(); i++) {
            if (col.get(i).getKey().equals(key)) {
                V oldValue = col.get(i).getValue();
                col.get(i).setValue(value);
                return oldValue;
            }
        }
        col.add(new Entry<>(key, value));
        return value;
    }

    /**
     * Returns the value of the entry with the given key. If the key does not exist
     * in the dictionary, null is returned. Be careful as the value of an entry can
     * also be null, so you cannot be sure if the key exists or not.
     * 
     * @param key the key of the entry
     * @return the value of the entry with the given key
     */
    public V get(K key) {
        for (int i = 0; i < col.size(); i++) {
            if (col.get(i).getKey().equals(key))
                return col.get(i).getValue();
        }
        return null;
    }

    /**
     * Removes the value of the entry with the given key and returns it's value. If 
     * the key is not in the dictionary, null is returned. Be careful as the value of
     * an entry can also be null, so you cannot be sure if the key existed or not.
     * @param key - the key of the entry
     * @return the value of the entry with the given key before removing it
     */
    public V remove(K key) {
        for (int i = 0; i < col.size(); i++) {
            if (col.get(i).getKey().equals(key)) {
                V value = col.get(i).getValue();
                col.remove(i);
                return value;
            }
        }
        return null;
    }

    /**
     * An entry in the dictionary. It stores a key and a value.
     */
    private static class Entry<K, V> {
        /**
         * The key of the entry.
         */
        final K key;
        /**
         * The value of the entry.
         */
        V value;

        /**
         * Creates a new entry with the given key and value.
         * 
         * @param key   the key of the entry
         * @param value the value of the entry
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key of the entry.
         * 
         * @return the key of the entry
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value of the entry.
         * 
         * @return the value of the entry
         */
        public V getValue() {
            return value;
        }

        /**
         * Sets the value of the entry to the given value.
         * 
         * @param value the new value of the entry
         */
        public void setValue(V value) {
            this.value = value;
        }
    }
}
