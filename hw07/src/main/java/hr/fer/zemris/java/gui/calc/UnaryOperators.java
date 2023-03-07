package hr.fer.zemris.java.gui.calc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

public class UnaryOperators {
    /**
     * Reference to the 1/x operation.
     */
    public static final DoubleUnaryOperator RECIPROCAL = x -> (1 / x);

    /**
     * Reference to the log10 operation.
     */
    public static final DoubleUnaryOperator LOG = Math::log10;

    /**
     * Reference to the ln operation.
     */
    public static final DoubleUnaryOperator LN = Math::log;

    /**
     * Reference to the sin operation.
     */
    public static final DoubleUnaryOperator SIN = Math::sin;

    /**
     * Reference to the cos operation.
     */
    public static final DoubleUnaryOperator COS = Math::cos;

    /**
     * Reference to the tan operation.
     */
    public static final DoubleUnaryOperator TAN = Math::tan;

    /**
     * Reference to the ctg operation.
     */
    public static final DoubleUnaryOperator CTG = x -> (1 / Math.tan(x));

    // -- inverse trigonometric functions --
    /**
     * Reference to the operation that returns ten to the value of x. Inverse
     * of the log10 operation.
     */
    public static final DoubleUnaryOperator TENEXN = x -> Math.pow(10, x);

    /**
     * Reference to the operation that returns e to the value of x. Inverse
     * of the ln operation.
     */
    public static final DoubleUnaryOperator EXP = Math::exp;

    /**
     * Reference to the operation that returns the arc sine of x. Inverse
     * of the sin operation.
     */
    public static final DoubleUnaryOperator ASIN = Math::asin;

    /**
     * Reference to the operation that returns the arc cosine of x. Inverse
     * of the cos operation.
     */
    public static final DoubleUnaryOperator ACOS = Math::acos;

    /**
     * Reference to the operation that returns the arc tangent of x. Inverse
     * of the tan operation.
     */
    public static final DoubleUnaryOperator ATAN = Math::atan;

    /**
     * Reference to the operation that returns the arc cotangent of x. Inverse
     * of the ctg operation.
     */
    public static final DoubleUnaryOperator ACTG = x -> (Math.PI / 2 - Math.atan(x));


    /**
     * Returns an array of all available unary operators.
     *
     * @return an array of all available unary operators
     */
    public static DoubleUnaryOperator[] getOperators() {
        return new DoubleUnaryOperator[]{RECIPROCAL, LOG, LN, SIN, COS, TAN, CTG};
    }

    /**
     * Returns a map of available operators and their names.
     *
     * @return a map of available operators and their names
     */
    @SuppressWarnings("Duplicates")
    public static Map<String, DoubleUnaryOperator> getOperatorsMap() {
        LinkedHashMap<String, DoubleUnaryOperator> map = new LinkedHashMap<>();
        map.put("1/x", RECIPROCAL);
        map.put("log", LOG);
        map.put("ln", LN);
        map.put("sin", SIN);
        map.put("cos", COS);
        map.put("tan", TAN);
        map.put("ctg", CTG);
        return map;
    }

    /**
     * Returns a map of available inverse operators and their uninverted names.
     *
     * @return a map of available inverse operators and their uninverted names
     */
    @SuppressWarnings("Duplicates")
    public static Map<String, DoubleUnaryOperator> getInvOperatorsMap() {
        LinkedHashMap<String, DoubleUnaryOperator> map = new LinkedHashMap<>();
        map.put("log", TENEXN);
        map.put("ln", EXP);
        map.put("sin", ASIN);
        map.put("cos", ACOS);
        map.put("tan", ATAN);
        map.put("ctg", ACTG);
        return map;
    }
}
