package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.CalcModelImpl;

import javax.swing.*;

/**
 * Class that represents a push button.
 */
public class PushButton extends JButton {
    /**
     * Constructor for the class.
     * @param model reference to the model to which the button is connected
     */
    public PushButton(CalcModelImpl model) {
        super("push");
        setFont(getFont().deriveFont(20f));
        addActionListener(e -> model.push());
    }
}
