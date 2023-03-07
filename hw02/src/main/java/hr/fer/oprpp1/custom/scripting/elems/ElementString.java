package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class that represents a string constant, inheriting Element and overriding.
 */
public class ElementString extends Element {
    /**
     * Value of the string.
     */
    private final String value;

    private final boolean isTagString;

    /**
     * Constructor that sets the value of the string.
     *
     * @param value - value of the string
     */
    public ElementString(String value) {
        this(value, false);
    }

    /**
     * Constructor that sets the value of the string. Parameter isTagString is used
     * in SmartScriptParser and DocumentNode to ease the process of constructing the
     * original document.
     *
     * @param value       - value of the string
     * @param isTagString - used to determine if the string is in a tag
     */
    public ElementString(String value, boolean isTagString) {
        this.value = value;
        this.isTagString = isTagString;
    }

    /**
     * Return the value of the string as a string.
     *
     * @return value of the string
     */
    @Override
    public String asText() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public boolean isTagString() {
        return isTagString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return value.equals(((ElementString) o).value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
