package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class that represents an operator, inheriting Element and overriding.
 */
public class ElementOperator extends Element {
    /**
     * Operator.
     */
    private final String symbol;

    /**
     * Constructor that takes an operator as a string.
     * 
     * @param symbol - operator as a string
     */
    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the symbol of the operator.
     * 
     * @return symbol of the operator
     */
    @Override
    public String asText() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return symbol.equals(((ElementOperator) o).symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}
