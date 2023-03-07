package hr.fer.zemris.java.gui.charts;

/**
 * Class representing a single value in a {@link BarChart} object, with an x and y value that are read-only.
 */
public class XYValue {
    /**
     * X value.
     */
    private int x;
    /**
     * Y value.
     */
    private int y;

    /**
     * Constructor for the XYValue object.
     * @param x x value.
     * @param y y value.
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the x value.
     * @return x value.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the y value.
     * @return y value.
     */
    public int getY() {
        return y;
    }
}
