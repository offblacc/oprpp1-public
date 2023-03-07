package hr.fer.zemris.java.gui.calc.components.buttons;

import javax.swing.*;

/**
 * Class that represents an inverse toggle button, or a checkbox.
 *
 * @author offblacc
 */
public class InvertedToggleButton extends JCheckBox {
    /**
     * Creates a new button.
     */
    public InvertedToggleButton() {
        super("Inv");
        setFont(getFont().deriveFont(20f));
    }

}
