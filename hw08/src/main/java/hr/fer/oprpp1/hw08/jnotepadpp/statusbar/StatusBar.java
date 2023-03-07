package hr.fer.oprpp1.hw08.jnotepadpp.statusbar;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Class that represents a status bar.
 */
public class StatusBar extends JPanel implements CaretListener, WindowListener, MultipleDocumentListener {
    /**
     * Label that shows the length of the document.
     */
    private JLabel lengthLabel;

    /**
     * Label showing the caret position.
     */
    private JLabel positionLabel;

    /**
     * Clock label.
     */
    private Clock clock;

    /**
     * Model of the multiple document.
     */
    private MultipleDocumentModel model;

    /**
     * Constructor that initializes the status bar.
     */
    public StatusBar(MultipleDocumentModel parentModel) {
        super(new GridLayout(1, 0));
        this.model = parentModel;
        lengthLabel = new JLabel();
        positionLabel = new JLabel();
        clock = new Clock();
        add(lengthLabel);
        add(positionLabel);
        add(clock);
    }

    /**
     * {@inheritDoc} Updates the position label.
     */
    @Override
    public void caretUpdate(CaretEvent e) {
        JTextComponent textArea = (JTextComponent) e.getSource();
        update(textArea);
    }

    private void update(JTextComponent textArea) {
        var caret = textArea.getCaret();
        lengthLabel.setText("Length: " + textArea.getText().length());
        Element root = textArea.getDocument().getDefaultRootElement();
        int pos = caret.getDot();
        int row = root.getElementIndex(pos);
        int col = pos - root.getElement(row).getStartOffset();
        int sel = Math.abs(caret.getDot() - caret.getMark());
        String selection = sel == 0 ? "" : ", Sel: " + sel;
        positionLabel.setText("Ln: " + (row + 1) + ", Col: " + (col + 1) + selection);
    }


    /**
     * {@inheritDoc} Stops the clock thread.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        clock.stop();
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        update(currentModel.getTextComponent());
    }

    @Override
    public void documentAdded(SingleDocumentModel currentModel) {
        update(currentModel.getTextComponent());
    }

    @Override
    public void documentRemoved(SingleDocumentModel prevModel) {
        update(model.getCurrentDocument().getTextComponent());
    }

    /* Empty methods */
    /* ------------------------------ */

    @Override
    public void windowOpened(WindowEvent e) {}


    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
    /* ------------------------------ */
}
