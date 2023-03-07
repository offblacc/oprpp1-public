package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class that represents a double constant, inheriting Element and overriding.
 */
public class ElementConstantDouble extends Element {
    /**
     * Value of the double.
     */
    private final double value;

    /**
     * Constructor that sets the value of the double.
     * 
     * @param value - value of the double
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * Return the value of the double as a string.
     * 
     * @return value of the double
     */
    @Override
    public String asText() {
        return Double.toString(value);
    }

    // override hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ElementConstantDouble that = (ElementConstantDouble) o;

        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }

    /**
     * Returns the value of the double.
     * 
     * @return - value of the double
     */
    public Object getValue() {
        return value;
    }
}
