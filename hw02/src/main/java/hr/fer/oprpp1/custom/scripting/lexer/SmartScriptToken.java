package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * Class that represents a token for the SmartScriptLexer. A token is made up of
 * a SmartScriptTokenType and an Element object.
 */
public class SmartScriptToken {
    Element element;
    SmartScriptTokenType type;

    /**
     * Constructor that takes a type and an element.
     *
     * @param type    - type of the token
     * @param element - element of the token
     */
    public SmartScriptToken(Element element, SmartScriptTokenType type) {
        this.element = element;
        this.type = type;
    }

    /**
     * Returns the type of the token.
     * 
     * @return - type of the token
     */
    public SmartScriptTokenType getType() {
        return type;
    }

    /**
     * Returns the element of the token.
     * 
     * @return - element of the token
     */
    public Element getElement() {
        return element;
    }

    /**
     * Returns the string representation of the token's element value.
     * 
     * @return
     */
    public Object getValue() {
        return element.asText();
    }
}
