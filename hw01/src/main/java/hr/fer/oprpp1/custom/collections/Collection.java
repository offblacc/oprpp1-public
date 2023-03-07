package hr.fer.oprpp1.custom.collections;

/**
 * (Effectively abstract) class that serves as a base class for any collection
 * of objects. All methods that are documented as abstract are not in fact, in
 * Java sense, abstract, however they are meant to be abstract and must be
 * implemented in classes that extend this one.
 */
public class Collection {
    /**
     * Default constructor.
     */
    public Collection() {
    }

    /**
     * Returns true if the collection has no objects stored, false otherwise.
     * 
     * @return true if the collection has no objects stored, false otherwise.
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns the number of currently stored objects.
     *
     * @return the number of currently stored objects.
     */
    public int size() {
        return 0;
    }

    /**
     * An abstract method. All subclases that implement this method add the
     * parameter value to the collection.
     * 
     * @param value - value to be added to the collection.
     */
    public void add(Object value) {
    }

    /**
     * An abstract method. All subclases that implement this method check if the
     * collection contains the parameter value.
     * 
     * @param value - value to be checked if it is in the collection.
     * @return true if the collection contains the given value, false otherwise.
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * An abstract method. All subclases that implement this method check if the
     * collection contains the parameter value and removes the first occurrence of
     * it.
     * 
     * @param value - value to be removed from the collection.
     * @return true if the collection contains the given value, false otherwise.
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * An abstract method. All subclases that implement this method return an array
     * of objects that are currently in the collection.
     * 
     * @return an array of objects that are currently in the collection.
     * @throws UnsupportedOperationException if the method is not implemented.
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * An abstract method. All subclases that implement this method call the
     * processor's process() method for each element of the collection.
     * 
     * @param processor - processor that will process each element of the
     *                  collection, defined by the user.
     */
    public void forEach(Processor processor) {

    }

    /**
     * An abstract method. All subclases that implement this method add all elements
     * from the given collection to the current collection.
     * 
     * @param other - collection from which all elements will be added to the
     *              current collection. Collection other is not changed.
     */
    public void addAll(Collection other) {
        class LocalProcessor extends Processor {
            public void process(Object value) {
                add(value);
            }
        }
        other.forEach(new LocalProcessor());
    }

    /**
     * An abstract method. All subclases that implement this method remove all
     * elements from the current collection.
     */
    public void clear() {

    }
}
