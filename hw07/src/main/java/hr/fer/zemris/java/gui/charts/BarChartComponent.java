package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Graphical component that draws a bar chart from a {@link BarChart} object.
 *
 * @author offblacc
 */
public class BarChartComponent extends JComponent {
    /**
     * Reference to the {@link BarChart} object that this component draws.
     */
    private BarChart chart;

    /**
     * Width of the shadow of the bars.
     */
    private static final short SHADOW_WIDTH = 3;

    /**
     * A margin constant used throughout the class.
     */
    private static final short MARGIN = 20;

    /**
     * List of {@link XYValue} objects that this component draws, taken from the {@link BarChart} object.
     */
    private List<XYValue> values;

    /**
     * Number of values that this component draws, taken from the {@link BarChart} object.
     */
    private int valuesCount;

    /**
     * Creates a new {@link BarChartComponent} object.
     * @param chart The {@link BarChart} object that this component draws.
     */
    public BarChartComponent(BarChart chart) {
        this.chart = chart;
        setLayout(null);
        setVisible(true);
        values = chart.getValues();
        valuesCount = values.size();
    }

    /**
     * Paints the components, overridden from {@link JComponent}.
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        int fmAscent = fm.getAscent();
        var values = chart.getValues();
        int parentWidth = getParent().getWidth();
        int parentHeight = getParent().getHeight();
        int valuesStartX = MARGIN + fm.getHeight();
        String xDescription = chart.getxDescription();
        int maxY = chart.getMaxY();

        // draw y axis name
        g2d.rotate(-Math.PI / 2);
        g2d.drawString(chart.getyDescription(), -parentHeight / 2, MARGIN);
        g2d.rotate(Math.PI / 2);
        g2d.drawString(xDescription, parentWidth / 2 - fm.stringWidth(xDescription) / 2, (int) (parentHeight - MARGIN * 1.5));
        int maxYValueStringWidth = fm.stringWidth(Integer.toString(maxY));
        int numOfGaps = maxY / chart.getStepY();
        int xGridStep = (parentWidth - valuesStartX - 2 * MARGIN) / valuesCount;

        g2d.setColor(Color.decode("#EEDDBF"));
        g2d.setStroke(new BasicStroke(2)); // widen the stroke width a bit

        for (int i = 0; i <= numOfGaps; i++) { // drawing horizontal lines
            int y = (int) Math.round(MARGIN + i * (parentHeight - 4 * MARGIN) / (double) numOfGaps);
            g2d.drawLine(2 * MARGIN + maxYValueStringWidth, y, parentWidth - MARGIN + 5, y);
        }

        for (int i = 0; i <= valuesCount; i++) { // drawing vertical lines
            g2d.drawLine(2 * MARGIN + maxYValueStringWidth + i * xGridStep, parentHeight - MARGIN * 3, 2 * MARGIN + maxYValueStringWidth + i * xGridStep, MARGIN - 5);
        }

        g2d.setColor(Color.BLACK); // reset color to black for drawing text
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));
        for (int i = maxY; i >= 0; i -= chart.getStepY()) { // draw y axis values
            int y = (int) Math.round(MARGIN + i * (parentHeight - 4 * MARGIN) / (double) maxY);
            String stringToRender = Integer.toString(maxY - i);
            g2d.drawString(stringToRender, 2 * MARGIN + maxYValueStringWidth - fm.stringWidth(stringToRender) - 3, y + fmAscent / 2 - 1);
        }
        // now for the x-axis values
        for (int i = 0; i < valuesCount; i++) {
            String stringToRender = Integer.toString(values.get(i).getX());
            g2d.drawString(stringToRender, 2 * MARGIN + maxYValueStringWidth + i * xGridStep - fm.stringWidth(stringToRender) / 2 + xGridStep / 2, parentHeight - MARGIN * 2 - 5);
        }

        g2d.setColor(new Color(0, 0, 0, 0.2f)); // bar shadows, semitransparent light grey
        // drawing each bar's shadow
        for (int i = 0; i < valuesCount; i++) {
            int barHeight = (int) Math.round(values.get(i).getY() * (parentHeight - 4 * MARGIN) / (double) maxY) - 5;
            g2d.fillRect(2 * MARGIN + maxYValueStringWidth + i * xGridStep + xGridStep + 1, parentHeight - MARGIN * 3 - barHeight, SHADOW_WIDTH, barHeight);
        }

        g2d.setColor(Color.decode("#F37747")); // color for the bars
        // now drawing the bars themselves
        for (int i = 0; i < valuesCount; i++) {
            int barHeight = (int) Math.round(values.get(i).getY() * (parentHeight - 4 * MARGIN) / (double) maxY);
            g2d.fillRect(2 * MARGIN + maxYValueStringWidth + i * xGridStep + 1, parentHeight - MARGIN * 3 - barHeight, xGridStep - 2, barHeight);
        }

        g2d.setColor(Color.BLACK); // reset color to black for the main axis lines
        g2d.drawLine(2 * MARGIN + maxYValueStringWidth, MARGIN - 5, 2 * MARGIN + maxYValueStringWidth, parentHeight - MARGIN * 3); // vertical, y-axis
        g2d.drawLine(2 * MARGIN + maxYValueStringWidth, parentHeight - MARGIN * 3, parentWidth - MARGIN + 5, parentHeight - MARGIN * 3); // horizontal, x-axis

        Polygon arrow = new Polygon(); // x-axis arrow
        arrow.addPoint(parentWidth - MARGIN + 10, parentHeight - MARGIN * 3);
        arrow.addPoint(parentWidth - MARGIN + 5, parentHeight - MARGIN * 3 - 5);
        arrow.addPoint(parentWidth - MARGIN + 5, parentHeight - MARGIN * 3 + 5);
        g2d.fillPolygon(arrow);
        arrow = new Polygon(); // y-axis arrow
        arrow.addPoint(2 * MARGIN + maxYValueStringWidth, MARGIN - 10);
        arrow.addPoint(2 * MARGIN + maxYValueStringWidth - 5, MARGIN - 5);
        arrow.addPoint(2 * MARGIN + maxYValueStringWidth + 5, MARGIN - 5);
        g2d.fillPolygon(arrow);
    }
}
