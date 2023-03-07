package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * Class that represents a for loop node, inheriting Node.
 */
public class ForLoopNode extends Node {
    /**
     * Variable of the for loop.
     */
    ElementVariable variable;
    /**
     * Start expression of the for loop.
     */
    Element startExpression;
    /**
     * End expression of the for loop.
     */
    Element endExpression;
    /**
     * Step expression of the for loop. Can be null, in which case it is assumed to
     * be 1.
     */
    Element stepExpression;

    /**
     * Constructor that takes a variable, start expression, end expression and step
     * expression.
     * 
     * @param variable        - variable of the for loop
     * @param startExpression - start expression of the for loop
     * @param endExpression   - end expression of the for loop
     * @param stepExpression  - step expression of the for loop
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
            Element stepExpression) {
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    /**
     * Constructor that takes a variable, start expression and end expression, and
     * sets step expression to null.
     * 
     * @param variable        - variable of the for loop
     * @param startExpression - start expression of the for loop
     * @param endExpression   - end expression of the for loop
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
        this(variable, startExpression, endExpression, null);
    }

    /**
     * Returns ElementVariable variable.
     * 
     * @return - ElementVariable variable
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * Returns Element startExpression.
     * 
     * @return - Element startExpression
     */
    public Element getStartExpression() {
        return startExpression;
    }

    /**
     * Returns Element endExpression.
     * 
     * @return - Element endExpression
     */
    public Element getEndExpression() {
        return endExpression;
    }

    /**
     * Returns Element stepExpression.
     * 
     * @return - Element stepExpression
     */
    public Element getStepExpression() {
        return stepExpression;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ForLoopNode)) {
            return false;
        }
        ForLoopNode other = (ForLoopNode) obj;
        if (!this.variable.equals(other.variable)) {
            return false;
        }
        if (!startExpression.equals(other.startExpression)) {
            return false;
        }
        if (!endExpression.equals(other.endExpression)) {
            return false;
        }
        if (stepExpression == null && other.stepExpression == null) {
            return true;
        }
        if (stepExpression == null && other.stepExpression != null) {
            return false;
        }
        if (stepExpression != null && other.stepExpression == null) {
            return false;
        }
        if (!stepExpression.equals(other.stepExpression)) {
            return false;
        }
        for (int i = 0; i < this.numberOfChildren(); i++) {
            if (!this.getChild(i).equals(other.getChild(i))) {
                return false;
            }
        }

        return true;
    }
}
