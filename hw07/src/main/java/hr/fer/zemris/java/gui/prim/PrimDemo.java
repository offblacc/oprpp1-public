package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;

/**
 * Demonstration of the {@link PrimListModel} and {@link JList} classes.
 *
 * @author offblacc
 */
public class PrimDemo extends JFrame {
    /**
     * Entry point of the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
    }

    /**
     * Constructor that creates a new PrimDemo object.
     */
    public PrimDemo() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PrimDemo");
        setSize(500, 500);
        setLocationRelativeTo(null);
        initGUI();
    }

    /**
     * Method that initializes the GUI.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        PrimListModel model = new PrimListModel();
        JList<Integer> list1 = new JList<>(model);
        JList<Integer> list2 = new JList<>(model);
        var center = new JPanel(new GridLayout(1, 0));
        center.add(new JScrollPane(list1));
        center.add(new JScrollPane(list2));
        cp.add(center, BorderLayout.CENTER);
        JButton next = new JButton("Next");
        next.addActionListener(e -> model.next());
        cp.add(next, BorderLayout.SOUTH);
    }
}
