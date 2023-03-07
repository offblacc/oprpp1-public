package hr.fer.oprpp1.custom.collections;

/**
 * Exception that is thrown when the stack is empty and the user tries to pop an element.
 */
public class EmptyStackException extends RuntimeException {
    public EmptyStackException(String message) {
        super(message);
    }
    public EmptyStackException() {
        super();
    }
}
