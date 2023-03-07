package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;

/**
 * Class that represents an equals button.
 *
 * @author offblacc
 */
public class EqualsButton extends JButton {
    public EqualsButton(CalcModel model) {
        super("=");
        addActionListener(e -> {
            if (model.getPendingBinaryOperation() != null) {
                model.setValue(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
                model.clearActiveOperand();
            }
        });
    }
}
