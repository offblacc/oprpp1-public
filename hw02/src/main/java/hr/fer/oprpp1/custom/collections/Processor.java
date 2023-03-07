package hr.fer.oprpp1.custom.collections;

/**
 * An interface that defines a single method process which takes an object and
 * does something with it.
 */
public interface Processor {
    /**
     * Processes the given object.
     *  
     * @param value - object to be processed.
     */
    public void process(Object value);
}