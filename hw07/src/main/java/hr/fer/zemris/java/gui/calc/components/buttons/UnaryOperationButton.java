package hr.fer.zemris.java.gui.calc.components.buttons;

import hr.fer.zemris.java.gui.calc.UnaryOperators;
import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;
import java.util.function.DoubleUnaryOperator;

/**
 * Class that represents a unary operation button.
 *
 * @author offblacc
 */
public class UnaryOperationButton extends JButton {
    /**
     * Reference to the model.
     */
    private CalcModel model;
    /**
     * Reference to the toggle button representing the inverse operation.
     */
    private InvertedToggleButton inv;

    /**
     * Constructor for the class.
     * @param text text to be displayed on the button, also used to determine the operation
     * @param model reference to the model
     * @param inv reference to the toggle button, telling us if we need to perform the inverse operation
     */
    public UnaryOperationButton(String text, CalcModel model, InvertedToggleButton inv) {
        super(text);
        this.model = model;
        this.inv = inv;
        setFont(getFont().deriveFont(20f));
        addActionListener(e -> {
            DoubleUnaryOperator operator = (inv != null && inv.isSelected()) ? UnaryOperators.getInvOperatorsMap().get(text) : UnaryOperators.getOperatorsMap().get(text);
            model.setValue(operator.applyAsDouble(model.getValue()));
        });
    }

    /**
     * Constructor for the class, omitting the inverted toggle button.
     * @param text text to be displayed on the button, also used to determine the operation
     * @param model reference to the model
     */
    public UnaryOperationButton(String text, CalcModel model) {
        this(text, model, null);
    }
}
