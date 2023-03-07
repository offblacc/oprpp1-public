package hr.fer.oprpp1.custom.collections;

import java.util.NoSuchElementException;

/**
 * An ElementsGetter object is an object that can be created for any collection,
 * making it easier to work with collections by essentially providing
 * iterator-like functionality.
 */
public interface ElementsGetter<T> {
    /**
     * Returns true if the collection contains at least one more element, false
     * otherwise.
     * 
     * @return true if the collection contains at least one more element, false
     *         otherwise.
     */
    boolean hasNextElement();

    /**
     * Returns the next element in the collection.
     * 
     * @return the next element in the collection.
     * @throws NoSuchElementException if the collection contains no more elements,
     *                                but the next one was requested by calling this
     *                                method.
     */
    T getNextElement();

    /**
     * Processes all remaining elements in the collection by calling the process
     * method of the given processor.
     * 
     * @param p - processor that will process each element of the collection,
     *          defined by the user.
     */
    default void processRemaining(Processor<T> p) {
        while (hasNextElement()) {
            p.process(getNextElement());
        }
    }
}
