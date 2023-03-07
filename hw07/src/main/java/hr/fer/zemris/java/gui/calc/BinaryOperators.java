package hr.fer.zemris.java.gui.calc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;

/**
 * Class that defines the binary operations.
 *
 * @author offblacc
 */
public class BinaryOperators {
    /**
     * Reference to the add operation.
     */
    public static final DoubleBinaryOperator ADD = Double::sum;

    /**
     * Reference to the subtract operation.
     */
    public static final DoubleBinaryOperator SUB = (a, b) -> a - b;

    /**
     * Reference to the multiply operation.
     */
    public static final DoubleBinaryOperator MUL = (a, b) -> a * b;

    /**
     * Reference to the divide operation.
     */
    public static final DoubleBinaryOperator DIV = (a, b) -> a / b;

    /**
     * Reference to the power operation.
     */
    public static final DoubleBinaryOperator POW = Math::pow;

    /**
     * Reference to the root operation.
     */
    public static final DoubleBinaryOperator ROOT = (a, b) -> Math.pow(a, 1 / b);


    /**
     * Returns an array of all available binary operators.
     *
     * @return an array of all available binary operators
     */
    public static DoubleBinaryOperator[] getOperators() {
        return new DoubleBinaryOperator[]{ADD, SUB, MUL, DIV};
    }

    /**
     * Returns a map of available operators and their names, where names
     * are keys and operators are values.
     *
     * @return a map of available operators and their names
     */
    public static Map<String, DoubleBinaryOperator> getOperatorsMap() {
        LinkedHashMap<String, DoubleBinaryOperator> map = new LinkedHashMap<>();
        map.put("+", ADD);
        map.put("-", SUB);
        map.put("*", MUL);
        map.put("/", DIV);
        map.put("x^n", POW);
        return map;
    }
}
