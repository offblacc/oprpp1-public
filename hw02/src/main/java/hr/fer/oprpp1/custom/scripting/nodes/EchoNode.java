package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * Class that represents a command which generates some textual output
 * dynamically, inheriting Node.
 */
public class EchoNode extends Node {
    /**
     * Used to store elements belonging to the echo node.
     */
    private final Element[] elements;

    /**
     * Constructor that takes an array of elements.
     * 
     * @param elements - array of elements
     */
    public EchoNode(Element[] elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("= ");
        for (Element element : elements) {
            sb.append(element.asText()).append(" ");
        }
        return sb.toString();
    }

    /**
     * Getter for the elements array.
     * 
     * @return - elements array
     */
    public Element[] getElements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        for (int i = 0; i < elements.length; i++) {
            if (!elements[i].equals(((EchoNode) o).elements[i]))
                return false;
        }
        return true;
    }
}