package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing complex numbers.
 */
public class Complex {
    /**
     * Real part.
     */
    private final double re;

    /**
     * Imaginary part.
     */
    private final double im;

    /**
     * Constant representing zero.
     */
    public static final Complex ZERO = new Complex(0, 0);

    /**
     * Constant representing one.
     */
    public static final Complex ONE = new Complex(1, 0);

    /**
     * Constant representing negative one.
     */
    public static final Complex ONE_NEG = new Complex(-1, 0);

    /**
     * Constant representing imaginary unit.
     */
    public static final Complex IM = new Complex(0, 1);

    /**
     * Constant representing negative imaginary unit.
     */
    public static final Complex IM_NEG = new Complex(0, -1);

    /**
     * Default constructor, setting real and imaginary part to zero.
     */
    public Complex() {
        this(0, 0);
    }

    /**
     * Constructor setting real and imaginary part to given values.
     *
     * @param re real part
     * @param im imaginary part
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Returns module of this complex number.
     *
     * @return module of this complex number
     */
    public double module() {
        return Math.sqrt(re * re + im * im);
    }

    /**
     * Returns new complex number which is multiplication of this and given complex number.
     *
     * @param c complex number to multiply with
     * @return new complex number which is multiplication of this and given complex number
     */
    public Complex multiply(Complex c) {
        if (c == null) {
            throw new IllegalArgumentException("Given complex number must not be null.");
        }
        return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
    }

    /**
     * Returns new complex number which is division of this and given complex number.
     *
     * @param c complex number to divide with
     * @return new complex number which is division of this and given complex number
     */
    public Complex divide(Complex c) {
        if (c == null) {
            throw new IllegalArgumentException("Given complex number must not be null.");
        }
        if (c.equals(ZERO)) {
            throw new IllegalArgumentException("Given complex number must not be zero.");
        }
        Complex numerator = this.multiply(new Complex(c.re, -c.im)); // equal to this * c conjugated
        double denominator = c.re * c.re + c.im * c.im;
        return new Complex(numerator.re / denominator, numerator.im / denominator);
    }

    /**
     * Returns new complex number which is addition of this and given complex number.
     *
     * @param c complex number to add with
     * @return new complex number which is addition of this and given complex number
     */
    public Complex add(Complex c) {
        if (c == null) {
            throw new IllegalArgumentException("Given complex number must not be null.");
        }
        return new Complex(re + c.re, im + c.im);
    }

    /**
     * Returns new complex number which is subtraction of this and given complex number.
     *
     * @param c complex number to subtract with
     * @return new complex number which is subtraction of this and given complex number
     */
    public Complex sub(Complex c) {
        if (c == null) {
            throw new IllegalArgumentException("Given complex number must not be null.");
        }
        return new Complex(re - c.re, im - c.im);
    }

    /**
     * Returns new complex number which is a negative of this complex number.
     *
     * @return new complex number which is a negative of this complex number
     */
    public Complex negate() {
        return new Complex(-re, -im);
    }

    /**
     * Returns new complex number which is a power of this complex number.
     *
     * @param n power
     * @return new complex number which is a power of this complex number
     */
    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Given power must be non-negative integer.");
        }
        double module = Math.pow(module(), n);
        double angle = calcAngle();
        angle *= n;
        return new Complex(module * Math.cos(angle), module * Math.sin(angle));

    }

    private double calcAngle() {
        double angle;
        if (re == 0) {
            angle = im > 0 ? Math.PI / 2 : -Math.PI / 2;
        } else {
            angle = Math.atan(im / re);
            if (re < 0) {
                angle += Math.PI;
            }
        }
        return angle;
    }


    /**
     * Returns list of complex numbers which are roots of this complex number.
     *
     * @param n the root to be taken (e.g. n=2 means square root)
     * @return list of complex numbers which are roots of this complex number
     */
    public List<Complex> root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Given root must be positive integer.");
        }
        double module = Math.pow(module(), 1.0 / n);
        double angle = calcAngle();
        angle /= n;
        List<Complex> roots = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            roots.add(new Complex(module * Math.cos(angle), module * Math.sin(angle)));
            angle += 2 * Math.PI / n;
        }
        return roots;
    }

    /**
     * Returns String representation of this complex number.
     *
     * @return String representation of this complex number
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (re != 0) {
            sb.append(re);
        }
        if (im != 0) {
            if (im > 0) {
                if (re != 0) {
                    sb.append("+");
                }
            } else {
                sb.append("-");
            }
            if (Math.abs(im) != 1) {
                sb.append(Math.abs(im));
            }
            sb.append("i");
        }
        if (sb.length() == 0) {
            sb.append("0.0");
        }
        return sb.toString();
    }

    /**
     * Parses given String to complex number, the string be in the format "a+ib" or "a-ib" or "a" or "ib" or "-a+ib" or
     * "-a-ib" or "-a" or "-ib". If b is omitted, it is assumed to be 1. If a is omitted, it is assumed to be 0.
     *
     * @param s String to parse
     * @return Complex number parsed from given String
     */
    public static Complex parse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Given string must not be null.");
        }
        s = s.trim();
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Given string must not be empty.");
        }
        double re = 0;
        double im = 0;
        StringBuilder sb = new StringBuilder();
        int sLen = s.length();
        for (int i = 0; i < sLen; i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                continue;
            }
            sb.append(c);
        }
        s = sb.toString();
        if (s.contains("+")) {
            String[] parts = s.split("\\+");
            if (parts.length != 2) {
                throw new IllegalArgumentException("");
            }
            re = Double.parseDouble(parts[0]);
            if (parts[1].trim().startsWith("i")) {
                im = Double.parseDouble(parts[1].substring(1));
            } else {
                throw new IllegalArgumentException("");
            }
        } else if (s.contains("-")) {
            // minus can be the first character and it can be between two numbers
            if (s.startsWith("-")) {
                if (s.length() == 1) {
                    throw new IllegalArgumentException("");
                }
                if (s.charAt(1) == 'i') {
                    if (s.length() == 2) {
                        im = -1;
                    } else {
                        im = -Double.parseDouble(s.substring(2));
                    }
                } else {
                    String[] parts = s.split("-");
                    if (parts.length != 3) {
                        throw new IllegalArgumentException("");
                    }
                    re = -Double.parseDouble(parts[1]);
                    im = -Double.parseDouble(parts[2].substring(1)); // to account for the i
                }
            } else {
                String[] parts = s.split("-");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("");
                }
                re = Double.parseDouble(parts[0]);
                if (parts[1].startsWith("i")) {
                    im = -Double.parseDouble(parts[1].substring(1));
                } else {
                    im = -Double.parseDouble(parts[1]);
                }
            }
        } else if (s.startsWith("i")) {
            if (s.length() == 1) {
                im = 1;
            } else {
                im = Double.parseDouble(s.substring(1));
            }
        } else {
            re = Double.parseDouble(s);
        }
        return new Complex(re, im);
    }


    /**
     * Checks if given object is equal to this complex number.
     *
     * @param o object to check
     * @return true if given object is equal to this complex number, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.re, re) == 0 && Double.compare(complex.im, im) == 0;
    }

    /**
     * Returns hash code of this complex number, calculated using re and im.
     *
     * @return hash code of this complex number
     */
    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }
}
