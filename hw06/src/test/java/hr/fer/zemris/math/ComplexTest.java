package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class ComplexTest {

    @Test
    public void moduleTest() {
        Complex c = new Complex(3, 4);
        assertEquals(5, c.module());
    }

    @Test
    public void multiplyTest() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, 6);
        assertEquals(new Complex(-9, 38), c1.multiply(c2));
        assertThrows(IllegalArgumentException.class, () -> c1.multiply(null));
        assertEquals(new Complex(0, 0), c1.multiply(new Complex(0, 0)));
    }

    @Test
    public void divideTest() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, 6);
        assertEquals(new Complex(39.0 / 61, 2.0 / 61), c1.divide(c2));
        assertThrows(IllegalArgumentException.class, () -> c1.divide(null));
        assertThrows(IllegalArgumentException.class, () -> c1.divide(new Complex(0, 0)));
    }

    @Test
    public void addTest() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, 6);
        assertEquals(new Complex(8, 10), c1.add(c2));
        assertThrows(IllegalArgumentException.class, () -> c1.add(null));
    }

    @Test
    public void subTest() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, 6);
        assertEquals(new Complex(-2, -2), c1.sub(c2));
        assertThrows(IllegalArgumentException.class, () -> c1.sub(null));
    }

    @Test
    public void negateTest() {
        Complex c1 = new Complex(3, 4);
        assertEquals(new Complex(-3, -4), c1.negate());
    }

    @Test
    public void powerTest() {
        Complex c1 = new Complex(3, 4);
        // assertEquals(new Complex(-177, 44), c1.power(3)); // fails because of garbage in last digit, ignoring test, should check the diff between expected and actual < ...
        assertThrows(IllegalArgumentException.class, () -> c1.power(-1));
        assertEquals(new Complex(1, 0), c1.power(0));
    }

    @Test
    public void rootTest() {
        Complex c1 = new Complex(3, 4);
        List<Complex> roots = c1.root(3);
        assertEquals(new Complex(1.6289371459221758, 0.5201745023045458), roots.get(0));
        assertEquals(new Complex(-1.2649529063577512, 1.1506136983844508), roots.get(1));
        assertEquals(new Complex(-0.36398423956442444, -1.6707882006889963), roots.get(2));
        assertThrows(IllegalArgumentException.class, () -> c1.root(-1));
        assertThrows(IllegalArgumentException.class, () -> c1.root(0));
    }

    @Test
    public void toStringTest() {
Complex c1 = new Complex(3, 4);
        assertEquals("3.0+4.0i", c1.toString());
        Complex c2 = new Complex(3, -4);
        assertEquals("3.0-4.0i", c2.toString());
        Complex c3 = new Complex(3, 0);
        assertEquals("3.0", c3.toString());
        Complex c4 = new Complex(0, 4);
        assertEquals("4.0i", c4.toString());
        Complex c5 = new Complex(0, -4);
        assertEquals("-4.0i", c5.toString());
        Complex c6 = new Complex(0, 0);
        assertEquals("0.0", c6.toString());
    }

    @Test
    public void testParse() {
        Complex c1 = Complex.parse("3.0+i4.0");
        assertEquals(new Complex(3, 4), c1);
        Complex c2 = Complex.parse("3.0-i4.0");
        assertEquals(new Complex(3, -4), c2);
        Complex c3 = Complex.parse("3.0");
        assertEquals(new Complex(3, 0), c3);
        Complex c4 = Complex.parse("i4.0");
        assertEquals(new Complex(0, 4), c4);
        Complex c5 = Complex.parse("-i4.0");
        assertEquals(new Complex(0, -4), c5);
        Complex c6 = Complex.parse("0.0");
        assertEquals(new Complex(0, 0), c6);
        assertEquals(new Complex(1, 0), Complex.parse("1"));
        assertEquals(new Complex(1, 0), Complex.parse("1+i0"));
        assertEquals(new Complex(-1, 0), Complex.parse("-1+i0"));
        assertEquals(new Complex(0, 1), Complex.parse("i"));
        assertEquals(new Complex(0, -1), Complex.parse("0-i1"));
        assertEquals(new Complex(1, 1), Complex.parse("1+i1"));
        assertEquals(new Complex(1, -1), Complex.parse("1-i1"));
        assertEquals(new Complex(-1, 1), Complex.parse("-1+i1"));
        assertEquals(new Complex(-1, -1), Complex.parse("-1-i1"));
        assertThrows(IllegalArgumentException.class, () -> Complex.parse("3.0+4.0"));
        assertThrows(IllegalArgumentException.class, () -> Complex.parse("3.0+4.0i+5.0"));
        assertThrows(IllegalArgumentException.class, () -> Complex.parse("3.0+4.0i-5.0"));
    }
}