package hr.fer.oprpp1.custom.collections;

public interface Tester<T> {
    /**
     * Tests the given object.
     * 
     * @param obj - object to be tested.
     * @return true if the object passes the test, false otherwise.
     */
    boolean test(T obj);
}
