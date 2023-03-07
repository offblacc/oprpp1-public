package hr.fer.zemris.math;

import java.util.Arrays;

/**
 * Class that represents a polynomial with complex coefficients.
 */
public class ComplexPolynomial {
    /**
     * Polynomial's factors.
     */
    private final Complex[] factors;

    /**
     * Constructor that takes factors of the polynomial.
     * The first factor is zeroth power, the second is first power, etc.
     *
     * @param factors Factors of the polynomial.
     */
    public ComplexPolynomial(Complex... factors) {
        this.factors = factors;
    }

    /**
     * Returns order of this polynomial; eg. For (7+2i)z^3+2z^2+5z+1 returns 3.
     *
     * @return Order of this polynomial.
     */
    public short order() {
        return (short) (factors.length - 1);
    }

    /**
     * Computes a new polynomial this*p.
     *
     * @param p Polynomial to multiply with.
     * @return Result of multiplication.
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Complex[] newFactors = new Complex[factors.length + p.factors.length - 1];
        Arrays.fill(newFactors, Complex.ZERO);
        for (int i = 0; i < factors.length; i++) {
            for (int j = 0; j < p.factors.length; j++) {
                newFactors[i + j] = newFactors[i + j].add(factors[i].multiply(p.factors[j]));
            }
        }
        return new ComplexPolynomial(newFactors);
    }

    /**
     * Computes first derivative of this polynomial
     *
     * @return First derivative of this polynomial.
     */
    public ComplexPolynomial derive() {
        Complex[] newFactors = new Complex[factors.length - 1];
        for (int i = 1; i < factors.length; i++) {
            newFactors[i - 1] = factors[i].multiply(new Complex(i, 0));
        }
        return new ComplexPolynomial(newFactors);
    }

    /**
     * Computes polynomial value at given point z.
     *
     * @param z Point at which the value is computed.
     * @return Value of the polynomial at point z.
     */
    public Complex apply(Complex z) {
        Complex result = Complex.ZERO;
        for (int i = 0; i < factors.length; i++) {
            result = result.add(factors[i].multiply(z.power(i)));
        }
        return result;
    }


    /**
     * Returns the string representation of this polynomial in
     * the form of (7+2i)z^3+(2)z^2+(5-3i)z+(1)z^0
     *
     * @return String representation of this polynomial.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = factors.length - 1; i >= 0; i--) {
            if (factors[i].equals(Complex.ZERO)) {
                continue;
            }
            if (i != factors.length - 1) {
                sb.append(" + ");
            }
            sb.append('(').append(factors[i]).append(')').append("z^").append(i);
        }
        return sb.toString();
    }
}
