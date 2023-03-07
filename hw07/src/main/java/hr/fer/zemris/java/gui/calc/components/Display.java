package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

import javax.swing.*;

import java.awt.*;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * Class that represents a calculator display.
 *
 * @author offblacc
 */
public class Display extends JLabel implements CalcValueListener {
    /**
     * Constructor for the class.
     */
    public Display() {
        super("0");
        setFont(getFont().deriveFont(30f));
        setBackground(Color.YELLOW);
        setOpaque(true);
    }

    /**
     * Method that is called when the value in the model changes.
     * @param model reference to the model
     */
    @Override
    public void valueChanged(CalcModel model) {
        invokeLater(() -> setText(model.toString()));
    }
}
