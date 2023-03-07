package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;


import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CalcLayoutTest {
    @Test
    public void testPDF0() {
        JPanel p = new JPanel(new CalcLayout(3));
        // da se iznimka baca ako se pokuša koristiti ograničenje (r,s) gdje je r<1 || r>5 ili s<1 || s>7
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(), new RCPosition(0, 1)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(), new RCPosition(6, 1)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(), new RCPosition(1, 0)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(), new RCPosition(1, 8)));
        // da se iznimka baca ako se pokuša koristiti ograničenje (1,s) gdje je s>1 && s<6,
        for (int s = 2; s < 6; s++) {
            int finalS = s;
            assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(), new RCPosition(1, finalS)));
        }
        // check that  1, 6 or 1, 7 throw nothing
        p.add(new JLabel(), new RCPosition(1, 6));
        p.add(new JLabel(), new RCPosition(1, 7));
    }


    @Test
    public void testPDF1() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel("");
        l1.setPreferredSize(new Dimension(10, 30));
        JLabel l2 = new JLabel("");
        l2.setPreferredSize(new Dimension(20, 15));
        p.add(l1, new RCPosition(2, 2));
        p.add(l2, new RCPosition(3, 3));
        Dimension dim = p.getPreferredSize();
        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    public void testPDF2() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel("");
        l1.setPreferredSize(new Dimension(108, 15));
        JLabel l2 = new JLabel("");
        l2.setPreferredSize(new Dimension(16, 30));
        p.add(l1, new RCPosition(1, 1));
        p.add(l2, new RCPosition(3, 3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }
}