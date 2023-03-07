package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.CalcModelImpl;

import javax.swing.*;

/**
 * Class that represents a pop button.
 */
public class PopButton extends JButton {
    /**
     * Constructor for the class.
     *
     * @param model reference to the model to which the button is connected
     */
    public PopButton(CalcModelImpl model) {
        super("pop");
        setFont(getFont().deriveFont(20f));
        addActionListener(e -> {
            try {
                model.pop();
            } catch (Exception e1) {
                // this.getParent() gets the button's parent, which is the panel, so the message is centered
                JOptionPane.showMessageDialog(this.getParent(), "Stack is empty.");
            }
        });
    }
}
