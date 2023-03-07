package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;

/**
 * Class that represents a button that clears the calculator.
 *
 * @author offblacc
 */
public class ClearButton extends JButton {
    /**
     * Creates a new button.
     *
     * @param model The calculator model. Must not be null.
     */
    public ClearButton(CalcModel model) {
        super("clr");
        setFont(getFont().deriveFont(20f));
        addActionListener(e -> model.clear());
    }
}
