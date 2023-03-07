package hr.fer.zemris.java.gui.layouts;

import java.awt.*;

/**
 * Functional interface for getting the size of a component, be it preferred, minimum or maximum.
 */
@FunctionalInterface
public interface ISizeGetter {
    /**
     * Returns the component's size.
     *
     * @param comp Component.
     * @return Component's size.
     */
    public Dimension get(Component comp);
}
