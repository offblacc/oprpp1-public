package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Class that represents a bar chart.
 *
 * @author offblacc
 */
public class BarChart {
    /**
     * List of {@link XYValue} objects to be displayed on the chart.
     */
    private List<XYValue> values;

    /**
     * Description of the x-axis.
     */
    private String xDescription;

    /**
     * Description of the y-axis.
     */
    private String yDescription;

    /**
     * Minimum value of the y-axis.
     */
    private int yMin;

    /**
     * Maximum value of the y-axis.
     */
    private int yMax;

    /**
     * Distance between two consecutive y-axis values.
     */
    private int yStep;

    /**
     * Constructor that creates a new {@link BarChart} object. Throws IllegalArgumentException if the
     * yMin < 0, yMax <= yMin, yStep <= 0 or yMax - yMin is not divisible by yStep.
     *
     * @param values       list of {@link XYValue} objects to be displayed on the chart.
     * @param xDescription description of the x-axis.
     * @param yDescription description of the y-axis.
     * @param yMin         minimum value of the y-axis, must be >= 0.
     * @param yMax         maximum value of the y-axis, must be > yMin.
     * @param yStep        distance between two consecutive y-axis values.
     *                     Must be greater than 0.
     * @throws IllegalArgumentException if the yMin < 0, yMax <= yMin, yStep <= 0
     *                                  or yMax - yMin is not divisible by yStep.
     */
    public BarChart(List<XYValue> values, String xDescription, String yDescription, int yMin, int yMax, int yStep) {
        if (yMin < 0 || yMax <= yMin || yStep < 0) throw new IllegalArgumentException("Invalid arguments.");
        if ((yMax - yMin) % yStep != 0)
            throw new IllegalArgumentException("Invalid arguments, yMax - yMin must be divisible by yStep.");
        for (var value : values)
            if (value.getY() < yMin) throw new IllegalArgumentException("Invalid arguments, value is less than yMin.");

        this.values = values;
        this.xDescription = xDescription;
        this.yDescription = yDescription;
        this.yMin = yMin;
        this.yMax = yMax;
        this.yStep = yStep;
    }

    /**
     * Creates and returns a new {@link BarChartComponent} object, which is a graphical representation
     * of the bar chart. The component is created using the data from this {@link BarChart} object.
     * @return a new {@link BarChartComponent} object.
     */
    public BarChartComponent getComponent() {
        return new BarChartComponent(this);
    }

    /**
     * Returns the list of {@link XYValue} objects to be displayed on the chart.
     * @return the list of {@link XYValue} objects to be displayed on the chart.
     */
    public List<XYValue> getValues() {
        return values;
    }

    /**
     * Returns the description of the x-axis.
     * @return the description of the x-axis.
     */
    public String getxDescription() {
        return xDescription;
    }

    /**
     * Returns the description of the y-axis.
     * @return the description of the y-axis.
     */
    public String getyDescription() {
        return yDescription;
    }

    /**
     * Returns the minimum value of the y-axis.
     * @return the minimum value of the y-axis.
     */
    public int getMinY() {
        return yMin;
    }

    /**
     * Returns the maximum value of the y-axis.
     * @return the maximum value of the y-axis.
     */
    public int getMaxY() {
        return yMax;
    }

    /**
     * Returns the distance between two consecutive y-axis values.
     * @return the distance between two consecutive y-axis values.
     */
    public int getStepY() {
        return yStep;
    }
}
