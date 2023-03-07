package hr.fer.zemris.java.gui.layouts;

import java.awt.*;

/**
 * Method references to methods that calculate the size of a component.
 */
public class LayoutSizeType {
    /**
     * Calculates the preferred size of a component.
     */
    public static final ISizeGetter PREF = Component::getPreferredSize;

    /**
     * Calculates the minimum size of a component.
     */
    public static final ISizeGetter MIN = Component::getMinimumSize;

    /**
     * Calculates the maximum size of a component.
     */
    public static final ISizeGetter MAX = Component::getMaximumSize;
}
