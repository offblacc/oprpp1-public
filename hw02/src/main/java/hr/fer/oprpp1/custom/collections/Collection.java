package hr.fer.oprpp1.custom.collections;

/**
 * An interface that defines a set of methods every collection should implement.
 */
public interface Collection {
    /**
     * Returns true if the collection has no objects stored, false otherwise.
     *
     * @return true if the collection has no objects stored, false otherwise.
     */
    public default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns the number of currently stored objects.
     * 
     * @return the number of currently stored objects.
     */
    public int size();

    /**
     * Adds the given object into this collection.
     * 
     * @param value - value to be added to the collection.
     */
    public void add(Object value);

    /**
     * Checks whether the collection contains the given object.
     * 
     * @param value - value to be checked if it is in the collection.
     * @return true if the collection contains the given value, false otherwise.
     */
    public boolean contains(Object value);

    /**
     * Removes the first occurrence of the given object from the collection.
     * 
     * @param value - value to be removed from the collection.
     * @return true if the collection contains the given value, false otherwise.
     */
    public boolean remove(Object value);

    /**
     * Converts the collection into an array of objects.
     * 
     * @return an array of objects that are currently in the collection.
     */
    public Object[] toArray();

    /**
     * Calls the processor's process method for each element of this collection.
     * 
     * @param processor - processor that will process each element of the
     *                  collection, defined by the user.
     */
    public default void forEach(Processor processor) {
        ElementsGetter eg = this.createElementsGetter();
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
    public default void addAll(Collection other) {
        class LocalProcessor implements Processor {
            public void process(Object value) {
                add(value);
            }
        }
        other.forEach(new LocalProcessor());
    }

    /**
     * Empties the collection.
     */
    public void clear();

    /**
     * Creates and returns an ElementsGetter object for this collection.
     * 
     * @return an ElementsGetter object for this collection.`
     */
    public ElementsGetter createElementsGetter();

    /**
     * Adds all elements from another collection that satisfy tester's test to the
     * collection calling this method.
     * 
     * @param col    - Collection object whose elements are added provided they pass
     *               the tester.
     * @param tester - Tester object whose test method gets called and determines
     *               whether an object from col is added to the calling method.
     */
    public default void addAllSatisfying(Collection col, Tester tester) {
        ElementsGetter eg = col.createElementsGetter();
        while (eg.hasNextElement()) {
            Object elem = eg.getNextElement();
            if (tester.test(elem)) {
                this.add(elem);
            }
        }
    }
}
