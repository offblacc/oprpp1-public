package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class that represents an integer constant, inheriting Element and overriding.
 */
public class ElementConstantInteger extends Element {
    /**
     * Value of the integer.
     */
    private final int value;

    /**
     * Constructor that sets the value of the integer.
     * 
     * @param value - value of the integer
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * Return the value of the integer as a string.
     * 
     * @return value of the integer
     */
    @Override
    public String asText() {
        return Integer.toString(value);
    }

    /**
     * Returns the value of the integer.
     * @return - value of the integer
     */
    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return value == ((ElementConstantInteger) o).value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
