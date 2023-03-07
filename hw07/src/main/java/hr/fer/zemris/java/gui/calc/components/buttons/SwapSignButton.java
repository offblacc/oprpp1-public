package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;

/**
 * Class that represents a swap sign button.
 *
 * @author offblacc
 */
public class SwapSignButton extends JButton {
    /**
     * Constructor for the class.
     * @param model reference to the model
     */
    public SwapSignButton(CalcModel model) {
        super("+/-");
        setFont(getFont().deriveFont(20f));
        addActionListener(e -> model.swapSign());
    }
}
