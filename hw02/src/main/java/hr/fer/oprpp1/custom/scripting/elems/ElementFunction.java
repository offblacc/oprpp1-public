package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class that represents a function, inheriting Element and overriding.
 */
public class ElementFunction extends Element {
    /**
     * Name of the function.
     */
    private final String name;

    /**
     * Constructor that takes a name of the function.
     * 
     * @param name - name of the function.
     */
    public ElementFunction(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the function.
     * 
     * @return name of the function
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
        return name.equals(((ElementFunction) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
