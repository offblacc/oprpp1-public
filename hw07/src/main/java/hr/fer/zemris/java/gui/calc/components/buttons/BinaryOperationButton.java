package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.BinaryOperators;
import hr.fer.zemris.java.gui.calc.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;
import java.util.function.DoubleBinaryOperator;

/**
 * A button that represents a binary operation.
 *
 * @author offblacc
 */
public class BinaryOperationButton extends JButton {
    /**
     * Method reference to the binary operation.
     */
    private DoubleBinaryOperator operator;

    /**
     * Creates a new button.
     *
     * @param name  The name of the operation. Used as the button's text. Used as a key to retrieve the method reference
     *              from the {@link BinaryOperators} class. Must not be null.
     * @param model The calculator model. Must not be null.
     * @param inv   Reference to the inverse button.
     */
    public BinaryOperationButton(String name, CalcModel model, InvertedToggleButton inv) {
        super(name);
        setFont(getFont().deriveFont(20f)); // looks weird with this font increased as well
        this.operator = BinaryOperators.getOperatorsMap().get(name);
        if (operator == null) throw new UnsupportedOperationException("No such operator. Try checking your spelling.");
        addActionListener(e -> {
            // if there is already a pending operation, perform it
            if (model.isActiveOperandSet()) {
                model.setActiveOperand(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
                // display the temporary result
                model.setPendingBinaryOperation(operator);
                model.setValue(0);
            }


            model.setPendingBinaryOperation(operator);
            if (inv != null && name.equals("x^n") && inv.isSelected())
                operator = BinaryOperators.ROOT; // not pretty, but this is an exception anyhow
            if (!model.isActiveOperandSet()) {
                model.setActiveOperand(model.getValue());
                model.clear();
            }
        });
    }

    /**
     * Constructor for the button that does not have an inverse button. Sets it to null.
     *
     * @param name  The name of the operation. Used as the button's text. Used as a key to retrieve the method reference
     *              from the {@link BinaryOperators} class.
     * @param model The calculator model. Must not be null.
     */
    public BinaryOperationButton(String name, CalcModel model) {
        this(name, model, null);
    }
}
