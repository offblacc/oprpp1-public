package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;

/**
 * Class that represents a reset button.
 *
 * @author offblacc
 */
public class ResButton extends JButton {
    /**
     * Constructor for the class.
     * @param model reference to the model to which the button is connected
     */
    public ResButton(CalcModel model) {
        super("res");
        setFont(getFont().deriveFont(20f)); // TODO make a class inbetween called CalcButton, and put this there
        addActionListener(e -> model.clearAll());
    }
}
