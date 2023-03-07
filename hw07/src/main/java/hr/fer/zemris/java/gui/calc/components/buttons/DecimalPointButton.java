package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;

/**
 * Decimal point button.
 *
 * @author offblacc
 */
public class DecimalPointButton extends JButton {
    /**
     * Reference to the calculator model instance.
     */
    private CalcModel model;

    /**
     * Creates a new button.
     *
     * @param model The calculator model. Must not be null.
     */
    public DecimalPointButton(CalcModel model) {
        super(".");
        setFont(getFont().deriveFont(30f));
        this.model = model;
        addActionListener(e -> model.insertDecimalPoint());
    }
}
