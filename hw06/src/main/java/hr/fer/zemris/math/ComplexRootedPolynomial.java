package hr.fer.zemris.math;

/**
 * Class that represents a complex rooted polynomial.
 */
public class ComplexRootedPolynomial {
    /**
     * Polynomial's constant.
     */
    private final Complex constant;

    /**
     * Roots of the polynomial.
     */
    private final Complex[] roots;

    /**
     * Constructor that takes a constant and roots of the polynomial.
     *
     * @param constant Constant of the polynomial.
     * @param roots    Roots of the polynomial.
     */
    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        this.constant = constant;
        this.roots = roots;
    }


    /**
     * Method that computes polynomial value at given point z.
     *
     * @param z Point at which the value is computed.
     * @return Value of the polynomial at point z.
     */
    public Complex apply(Complex z) {
        Complex result = constant;
        for (Complex root : roots) {
            result = result.multiply(z.sub(root));
        }
        return result;
    }

    /**
     * Method that converts this representation to ComplexPolynomial type.
     * @return ComplexPolynomial representation of this polynomial.
     */
    public ComplexPolynomial toComplexPolynomial() {
        ComplexPolynomial result = new ComplexPolynomial(constant);
        for (Complex root : roots) {
            result = result.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
        }
        return result;
    }

    /**
     * Roots getter.
     * @return Roots of the polynomial as an array.
     */
    public Complex[] getRoots() {
        return roots;
    }

    /**
     * Returns string representation of this polynomial in the format of
     * (z0)*(z-z1)*(z-z2)*...*(z-zn)
     * @return String representation of this polynomial.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(constant).append(")");
        for (Complex root : roots) {
            sb.append("*(z-(").append(root).append("))");
        }
        return sb.toString();
    }

    /**
     * Method that finds index of the closest root for given complex number z that is within treshold.
     * @param z Complex number.
     * @param treshold Treshold.
     * @return Index of the closest root.
     */
    @SuppressWarnings("SpellCheckingInspection")
    public int indexOfClosestRootFor(Complex z, double treshold) {
        int index = -1;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < roots.length; i++) {
            double distance = z.sub(roots[i]).module();
            if (distance < minDistance && distance < treshold) {
                minDistance = distance;
                index = i;
            }
        }
        return index;
    }
}
