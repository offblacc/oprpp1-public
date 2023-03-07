package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class that represents a variable, inheriting Element an overriding.
 */
public class ElementVariable extends Element {
    /**
     * Name of the variable.
     */
    private final String name;

    /**
     * Constructor that takes a name of the variable.
     * 
     * @param name - name of the variable.
     */
    public ElementVariable(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the name variable
     * 
     * @return name of the variable
     */
    @Override
    public String asText() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return name.equals(((ElementVariable) o).name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
