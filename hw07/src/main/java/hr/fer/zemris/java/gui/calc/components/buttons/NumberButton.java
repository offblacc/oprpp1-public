package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;

/**
 * Class that represents a number button.
 *
 * @author offblacc
 */
public class NumberButton extends JButton {
    /**
     * Digit on the button.
     */
    private int digit;

    /**
     * Calculator model.
     */
    private CalcModel model;

    /**
     * Constructor.
     *
     * @param text digit on the button, as a string
     * @param model calculator model
     */
    public NumberButton(String text, CalcModel model) {
        super(text);
        setFont(getFont().deriveFont(20f));
        this.model = model;
        try {
            digit = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid digit.");
        }
        addActionListener(e -> {
            if (model.getPendingBinaryOperation() != null && model.getValue() == 0) {
                model.clear();
                ((CalcModelImpl) model).unfreeze();
            }
            if (!model.isEditable()) return; // silently ignoring
            model.insertDigit(digit);
        });
    }
}