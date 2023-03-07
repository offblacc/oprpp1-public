package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.lexer.*;
import hr.fer.oprpp1.custom.scripting.nodes.*;
import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import java.lang.Math;

/**
 * Parser for SmartScript. Uses the SmartScriptLexer for generation of tokens,
 * generating a document tree.
 */
public class SmartScriptParser {
    /**
     * Lexer used for token generation.
     */
    private final SmartScriptLexer lexer;
    /**
     * Document node of the document tree.
     */
    private final DocumentNode documentNode;
    /**
     * Stack used for storing nodes, especially useful when nesting tags is present.
     */
    private final ObjectStack stack;
    /**
     * The last generated token.
     */
    private SmartScriptToken token;
    /**
     * State of the parser.
     */
    private SmartScriptParserState parserState;

    /**
     * Constructor that takes a document body as string, and generates a document
     * tree.
     *
     * @param documentBody - body of the document as a String
     */
    public SmartScriptParser(String documentBody) {
        documentNode = new DocumentNode();
        lexer = new SmartScriptLexer(documentBody);
        stack = new ObjectStack();
        parserState = SmartScriptParserState.INIT;
        parse();
    }

    /**
     * Generates a document tree from the document body. Makes use of the lexer and
     * the stack, making decisions
     * based on the current state of the parser. This method is the entry point into
     * parsing, calling more specialised
     * methods for parsing the different types of tokens based on the current
     * context.
     */
    public void parse() {
        stack.push(documentNode);
        while ((token = lexer.nextToken()).getType() != SmartScriptTokenType.EOF) {
            if (parserState == SmartScriptParserState.INIT) {
                if (token.getType() == SmartScriptTokenType.BASIC) {
                    ((Node) stack.peek()).addChildNode(new TextNode((ElementString) token.getElement()));
                } else if (token.getType() == SmartScriptTokenType.BOUND) {
                    parserState = SmartScriptParserState.EXPECT_TAG_NAME; // switch state and go on to the next token
                } else {
                    throw new SmartScriptParserException("Unknown parsing exception.");
                }
            } else if (parserState == SmartScriptParserState.EXPECT_TAG_NAME) {
                processTag();
            } else if (parserState == SmartScriptParserState.UNCLOSED_FOR_TAG) {
                if (token.getValue().equals("$}")) {
                    parserState = SmartScriptParserState.INIT;
                } else {
                    throw new SmartScriptParserException("Unclosed for loop tag.");
                }
            }
        }
        if (parserState != SmartScriptParserState.INIT) {
            throw new SmartScriptParserException("Parsing error. Possible unclosed tags.");
        }
        if (stack.size() != 1) {
            throw new SmartScriptParserException("Parsing error. Possible unclosed tags.");
        }
    }

    /**
     * Processes the tag token. Based on the tag name, calls the appropriate method
     * for parsing the tag.
     *
     * @throws SmartScriptParserException if the tag name is not valid
     */
    private void processTag() {
        String tagName = token.getValue().toString();
        if (isForTag(tagName)) {
            processForLoop();
        } else if (isValidTagName(tagName)) {
            if (isEndTag(tagName)) {
                processEndTag();
            } else if (tagName.equals("=")) {
                processEchoTag();
            } else {
                throw new SmartScriptParserException("Invalid tag name.");
            }
        }
    }

    /**
     * Processes the ECHO tag. Generates an EchoNode and adds it to the stack.
     *
     * @throws SmartScriptParserException if the tag is not closed properly or there
     *                                    is an error in the tag body.
     */
    private void processEchoTag() {
        ArrayIndexedCollection elements = new ArrayIndexedCollection();
        while (token.getType() != SmartScriptTokenType.EOF && !token.getValue().equals("$}")) {
            throwExceptionIfEOF(token);
            if (token.getValue().toString().charAt(0) == '@'
                    && !isValidVariableName(token.getValue().toString().substring(1))) {
                throw new SmartScriptParserException("Invalid function name");
            }
            elements.add(token.getElement());
            token = lexer.nextToken();
        }

        Element[] elementsArray = new Element[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            elementsArray[i] = (Element) elements.get(i);
        }

        EchoNode echoNode = new EchoNode(elementsArray);
        ((Node) stack.peek()).addChildNode(echoNode);
        parserState = SmartScriptParserState.INIT;
    }

    /**
     * Processes the FOR tag. Generates a ForLoopNode and adds it to the stack. It
     * is popped from the stack
     * when the end tag is encountered. The node is also added to the parent node.
     * Stack is only utilized
     * to keep track of everyone's the parent nodes and to check for unclosed tags.
     */
    private void processForLoop() {
        if (!((token = lexer.nextToken()).getElement() instanceof ElementVariable)) {
            throw new SmartScriptParserException("Invalid for loop var.");
        }
        ElementVariable var = (ElementVariable) token.getElement();
        throwExceptionIfEOF(token);
        throwExceptionIfEndBound(var);
        throwExceptionIfInvalidVariableName(var.asText());

        Element startExpression = (token = lexer.nextToken()).getElement();
        throwExceptionIfEOF(token);
        throwExceptionIfEndBound(startExpression);
        throwExceptionIfInvalidForLoopParameter(startExpression);

        Element endExpression = (token = lexer.nextToken()).getElement();
        throwExceptionIfEOF(token);
        throwExceptionIfEndBound(endExpression);
        throwExceptionIfInvalidForLoopParameter(endExpression);

        Element stepExpression = (token = lexer.nextToken()).getElement();
        throwExceptionIfEOF(token);
        throwExceptionIfInvalidStepExpression(stepExpression);
        if (isEndBound(stepExpression)) {
            parserState = SmartScriptParserState.INIT; // because we exited the for loop tag
            stepExpression = null;
        } else {
            parserState = SmartScriptParserState.UNCLOSED_FOR_TAG;
        }

        ForLoopNode forLoopNode = new ForLoopNode(var, getIntegerFromElement(startExpression),
                getIntegerFromElement(endExpression), getIntegerFromElement(stepExpression));
        ((Node) stack.peek()).addChildNode(forLoopNode);
        stack.push(forLoopNode);
    }

    /**
     * Processes the END tag. Pops the stack.
     *
     * @throws SmartScriptParserException if the tag is not closed properly or there
     *                                    is an error in the tag body.
     */
    private void processEndTag() {
        if (stack.peek() instanceof DocumentNode) {
            throw new SmartScriptParserException("There are extra END tags.");
        }

        stack.pop();

        while (token.getValue() != "$}")

        {
            if ((token = lexer.nextToken()).getType() == SmartScriptTokenType.EOF) {
                throw new SmartScriptParserException("End tag has no right bound.");
            }
        }
        parserState = SmartScriptParserState.INIT;
    }

    /**
     * Generates an integer element a for loop parameter, since a for loop parameter
     * can be of any type Element.
     * 
     * @param element - the element to be converted to an integer
     * @return - the ElementConstantInteger object built from the element
     * @throws SmartScriptParserException if the element is not an integer
     */
    private ElementConstantInteger getIntegerFromElement(Element element) {
        if (element instanceof ElementConstantInteger)
            return (ElementConstantInteger) element;
        if (element instanceof ElementConstantDouble)
            return new ElementConstantInteger((int) Math.round(Double.parseDouble(element.asText())));

        if (element instanceof ElementString) {
            try {
                return new ElementConstantInteger((int) Math.round(Double.parseDouble(element.asText())));
            } catch (NumberFormatException ex) {
                throw new SmartScriptParserException("Invalid for loop parameter " + element.asText());
            } catch (ClassCastException e) {
                throw new SmartScriptParserException("Invalid for loop parameter " + element.asText());
            }
        }
        if (element == null)
            return null;
        throw new SmartScriptParserException("Invalid for loop parameter " + element.asText());
    }

    /**
     * Counts all the text nodes in the document node and returns the number of
     * them.
     * Used for testing.
     * 
     * @param node - the starting, root node
     * @return - the number of text nodes, including the root node if it is a text
     *         node
     */
    public int countTextNodesRecursively(Node node) {
        int textNodes = 0;
        for (int i = 0; i < node.numberOfChildren(); i++) {
            textNodes += countTextNodesRecursively(node.getChild(i));
        }
        if (node instanceof TextNode) {
            textNodes++;
        }
        return textNodes;
    }

    /**
     * Checks if the parameter is a valid for loop parameter.
     * 
     * @param element - the element to be checked
     * @return - true if the element is a valid for loop parameter, false otherwise
     */
    private static boolean isValidForLoopParameter(Element element) {
        if (element instanceof ElementVariable)
            return true;
        if (element instanceof ElementConstantInteger)
            return true;
        if (element instanceof ElementConstantDouble)
            return true;
        return element instanceof ElementString;
    }

    /**
     * Checks if a string is a valid variable name.
     * 
     * @param variableName - the string to be checked
     * @return - true if the string is a valid variable name, false otherwise
     */
    private static boolean isValidVariableName(String variableName) {
        if (variableName.length() == 0)
            return false;
        if (!Character.isLetter(variableName.charAt(0)))
            return false;
        for (int i = 1; i < variableName.length(); i++) {
            char c = variableName.charAt(i);
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }

    /**
     * Throws an exception if the element is an end bound.ž
     * 
     * @param element - the element to be checked
     * @throws SmartScriptParserException if the element is an end bound
     */

    protected static void throwExceptionIfEndBound(Element element) {
        if (isEndBound(element)) {
            throw new SmartScriptParserException("Not enough for loop parameters.");
        }
    }

    /**
     * Throws an exception if the element is an invalid for loop parameter.
     * 
     * @param element - the element to be checked
     * @throws SmartScriptParserException if the element is an invalid for loop
     *                                    parameter
     */
    private static void throwExceptionIfInvalidForLoopParameter(Element element) {
        if (!isValidForLoopParameter(element)) {
            throw new SmartScriptParserException("Invalid for loop parameter");
        }
    }

    /**
     * Throws an exception if the element is an invalid variable name.
     * 
     * @param name - the name to be checked
     * @throws SmartScriptParserException if name is an invalid variable name
     */
    private static void throwExceptionIfInvalidVariableName(String name) {
        if (!isValidVariableName(name)) {
            throw new SmartScriptParserException(
                    String.format("Invalid variable name %s inside for loop", name));
        }
    }

    /**
     * Throws an exception if the current token is an EOF token.
     * 
     * @param token - the token to be checked
     * @throws SmartScriptParserException if the token is an EOF token
     */
    private static void throwExceptionIfEOF(SmartScriptToken token) {
        if (token.getType() == SmartScriptTokenType.EOF) {
            throw new SmartScriptParserException("Unexpected end of file.");
        }
    }

    /**
     * Throws an exception if the element is an invalid step expression.
     * 
     * @param element - the element to be checked
     * @throws SmartScriptParserException if the element is an invalid step
     *                                    expression
     */
    private static void throwExceptionIfInvalidStepExpression(Element element) {
        if (!isStepExpressionOrEndBound(element)) {
            throw new SmartScriptParserException("Invalid for loop parameter");
        }
    }

    /**
     * Checks if the element is an end bound or step expression.
     * 
     * @param element - the element to be checked
     * @return - true if the element is an end bound or a valid for loop parameter,
     *         false otherwise
     */
    private static boolean isStepExpressionOrEndBound(Element element) {
        return isValidForLoopParameter(element) || isEndBound(element);
    }

    /**
     * Checks if a string is a valid tag name.
     * 
     * @param variableName - the string to be checked
     * @return - true if the string is a valid tag name, false otherwise
     */
    private static boolean isValidTagName(String variableName) {
        return isValidVariableName(variableName) || (variableName.length() == 1 && variableName.equals("="));
    }

    /**
     * Checks if the string is equal to "for".
     * 
     * @param tagName - the string to be checked
     * @return - true if the string is equal to "for", false otherwise
     */
    private static boolean isForTag(String tagName) {
        return tagName.equalsIgnoreCase("for");
    }

    /**
     * Checks if the string is equal to "end".
     * 
     * @param tagName - the string to be checked
     * @return - true if the string is equal to "end", false otherwise
     */
    private static boolean isEndTag(String tagName) {
        return tagName.equalsIgnoreCase("end");
    }

    /**
     * Checks if the string is equal to "$}".
     * 
     * @param element - the string to be checked
     * @return - true if the string is equal to "$}", false otherwise
     */
    private static boolean isEndBound(Element element) {
        return element.asText().equals("$}");
    }

    /**
     * Document node getter.
     * 
     * @return - the document node
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }
}
