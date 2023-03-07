package hr.fer.oprpp1.custom.collections;

/**
 * An interface that defines a set of methods every collection should implement.
 */
public interface Collection<T> {
    /**
     * Returns true if the collection has no objects stored, false otherwise.
     *
     * @return true if the collection has no objects stored, false otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns the number of currently stored objects.
     * 
     * @return the number of currently stored objects.
     */
    int size();

    /**
     * Adds the given object into this collection.
     * 
     * @param value - value to be added to the collection.
     */
    void add(T value);

    /**
     * Checks whether the collection contains the given object.
     * 
     * @param value - value to be checked if it is in the collection.
     * @return true if the collection contains the given value, false otherwise.
     */
    boolean contains(T value);

    /**
     * Removes the first occurrence of the given object from the collection.
     * 
     * @param value - value to be removed from the collection.
     * @return true if the collection contains the given value, false otherwise.
     */
    boolean remove(T value);

    /**
     * Converts the collection into a T array.
     * 
     * @return an array of objects that are currently in the collection.
     */
    T[] toArray();

    /**
     * Calls the processor's process method for each element of this collection.
     * 
     * @param processor - processor that will process each element of the
     *                  collection, defined by the user.
     */
    default void forEach(Processor<? super T> processor) {
        ElementsGetter<T> eg = this.createElementsGetter();
        while (eg.hasNextElement()) {
            processor.process(eg.getNextElement());
        }
    }

    /**
     * Adds all elements from the given collection into this collection. The given
     * collection is not modified.
     * 
     * @param other - collection from which all elements will be added to the
     *              current collection. Collection other is not changed.
     */
    default void addAll(Collection<? extends T> other) {
        class LocalProcessor implements Processor<T> {
            public void process(T value) {
                add(value);
            }
        }
        other.forEach(new LocalProcessor());
    }

    /**
     * Empties the collection.
     */
    void clear();

    /**
     * Creates and returns an ElementsGetter object for this collection.
     * 
     * @return an ElementsGetter object for this collection.
     */
    ElementsGetter<T> createElementsGetter();

    /**
     * Adds all elements from another collection that satisfy tester's test to the
     * collection calling this method.
     * 
     * @param col    - Collection object whose elements are added provided they pass
     *               the tester.
     * @param tester - Tester object whose test method gets called and determines
     *               whether an object from col is added to the calling method.
     */
    default void addAllSatisfying(Collection<? extends T> col, Tester<T> tester) {
        ElementsGetter<? extends T> eg = col.createElementsGetter();
        while (eg.hasNextElement()) {
            T elem = eg.getNextElement();
            if (tester.test(elem)) {
                this.add(elem);
            }
        }
    }
}
