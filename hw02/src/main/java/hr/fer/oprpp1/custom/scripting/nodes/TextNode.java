package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.*;

/**
 * Class that represents a text node, inheriting Node.
 */
public class TextNode extends Node {
    /**
     * Text of the text node.
     */
    private final ElementString text;

    /**
     * Constructor that takes a text.
     * 
     * @param text - text of the text node
     */

    public TextNode(ElementString text) {
        this.text = text;
    }

    /**
     * Returns the text of the text node.
     *
     * @return text of the text node
     */
    public ElementString getText() {
        return text;
    }

    @Override
    public String toString() {
        return text.asText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return text.equals(((TextNode) o).text);
    }

}
