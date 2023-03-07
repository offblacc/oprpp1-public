package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;

/**
 * Class that represents an entire document, inheriting Node.
 */
public class DocumentNode extends Node {

    /**
     * A toString method that returns the original document from the parsed text.
     * 
     * @return - original document
     */
    @Override
    public String toString() {
        return buildDocument(this);
    }

    /**
     * A method that builds the original document from the parsed text recursively.
     * 
     * @param node - node that is being processed
     * @return - original document
     */
    private String buildDocument(Node node) {
        StringBuilder sb = new StringBuilder();

        if (node instanceof TextNode) {
            sb.append(addEscapeCharacterToTextNode(((TextNode) node).getText()));
        } else if (node instanceof ForLoopNode) {
            sb.append(buildForLoopNode((ForLoopNode) node));
        } else if (node instanceof EchoNode) {
            sb.append(buildEchoNode((EchoNode) node));
        }

        for (int i = 0; i < node.numberOfChildren(); i++) {
            sb.append(buildDocument(node.getChild(i)));
        }
        if (node instanceof ForLoopNode) {
            sb.append("{$END$}");
        }
        return sb.toString();
    }

    /**
     * A method that builds the original document from the parsed text for an
     * EchoNode.
     * 
     * @param node - node that is being processed
     * @return - original document
     */
    private String buildEchoNode(EchoNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("{$");
        for (Element element : node.getElements()) {
            if (element instanceof ElementString && ((ElementString) element).isTagString())
                sb.append("\"");
            sb.append(addEscapeCharacterToTagString(element));
            if (element instanceof ElementString && ((ElementString) element).isTagString())
                sb.append("\"");
            sb.append(" ");

        }
        sb.append("$}");

        return sb.toString();
    }

    /**
     * A method that builds the original document from the parsed text for a
     * ForLoopNode.
     * 
     * @param node - node that is being processed
     * @return - original document
     */
    private String buildForLoopNode(ForLoopNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("{$ FOR ");
        sb.append(addEscapeCharacterToTagString(node.getVariable())).append(" ");
        sb.append(addEscapeCharacterToTagString(node.getStartExpression())).append(" ");
        sb.append(addEscapeCharacterToTagString(node.getEndExpression())).append(" ");
        if (node.getStepExpression() != null) {
            sb.append(node.getStepExpression().asText()).append(" ");
        }
        sb.append("$}");

        return sb.toString();
    }

    /**
     * A method that adds escape characters to a text node where necessary.
     * 
     * @param element - element that potentially requires escape characters
     * @return - text node with escape characters
     */
    private String addEscapeCharacterToTextNode(Element element) {
        if (!(element instanceof ElementString)) {
            return element.asText();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < element.asText().length(); i++) {
            if (element.asText().charAt(i) == '\\') {
                sb.append("\\\\");
            } else if (element.asText().charAt(i) == '{') {
                sb.append("\\");
                sb.append(element.asText().charAt(i));
            } else {
                sb.append(element.asText().charAt(i));
            }
        }
        return sb.toString();

    }

    /**
     * A method that adds escape characters to a tag string where necessary.
     * 
     * @param element - element that potentially requires escape characters
     * @return - tag string with escape characters
     */
    private String addEscapeCharacterToTagString(Element element) {
        if (!(element instanceof ElementString)) {
            return element.asText();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < element.asText().length(); i++) {
            if (element.asText().charAt(i) == '\\') {
                sb.append("\\\\");
            } else if (element.asText().charAt(i) == '"') {
                sb.append("\\");
                sb.append(element.asText().charAt(i));
            } else {
                sb.append(element.asText().charAt(i));
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DocumentNode documentNode = (DocumentNode) o;
        if (this.numberOfChildren() != documentNode.numberOfChildren())
            return false;
        for (int i = 0; i < this.numberOfChildren(); i++) {
            if (!this.getChild(i).equals(documentNode.getChild(i))) {
                return false;
            }
        }
        return true;
    }
}
