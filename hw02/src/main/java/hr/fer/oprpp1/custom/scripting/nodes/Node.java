package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * Base class for all graph nodes.
 */
public class Node {
    /**
     * Internally managed collection of this node's children.
     */
    ArrayIndexedCollection children;

    /**
     * Adds child node to this node. Only creates a new collection if it doesn't
     * exist, conserving memory and processing power.
     * 
     * @param child - node to be added as a child
     */
    public void addChildNode(Node child) {
        if (children == null) {
            children = new ArrayIndexedCollection();
        }
        children.add(child);
    }

    /**
     * Returns the number of direct children nodes.
     * 
     * @return the number of direct children nodes
     */
    public int numberOfChildren() {
        return children == null ? 0 : children.size();
    }

    /**
     * Returns child at given index. Throws IndexOutOfBoundsException if index is
     * invalid. Index should be between 0 and numberOfChildren - 1.
     * 
     * @param index - index of child to be returned
     * @return child at given index
     */
    public Node getChild(int index) {
        if (index < 0 || index > numberOfChildren() - 1) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        return (Node) children.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("Can't compare nodes that are base class Node.");
    }
}
