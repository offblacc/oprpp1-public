package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.*;

/**
 * Layout manager for a simple calculator.
 *
 * @author offblacc
 */
public class CalcLayout implements LayoutManager2 { // TODO need to alternate width...
    /**
     * Gap in pixels between components.
     */
    int gap;
    /**
     * Number of rows in the layout.
     */
    private static final int ROWS = 5;

    /**
     * Number of columns in the layout.
     */
    private static final int COLUMNS = 7;
    /**
     * The default gap between components.
     */
    private static final int DEFAULT_GAP = 0;

    /**
     * Array of components in the layout.
     */
    private final Component[][] components = new Component[ROWS][COLUMNS];

    /**
     * Map containing positions of components in the layout that take more than one x coordinate, the value being the
     * number of columns the component takes.
     */
    private static final HashMap<RCPosition, Integer> specialPositions = new HashMap<>();

    static {
        specialPositions.put(new RCPosition(1, 1), 5);
    }

    /**
     * Creates a new {@link CalcLayout} with the given gap.
     *
     * @param gap the gap between components
     */
    public CalcLayout(int gap) {
        this.gap = gap;
    }

    /**
     * Creates a new {@link CalcLayout} with the default gap.
     */
    public CalcLayout() {
        this(DEFAULT_GAP);
    }


    /**
     * Adds the specified component with the specified name to the layout.
     *
     * @param comp                  the component to be added
     * @param RCPositionConstraints where to add the component. Can be a {@link RCPosition} or a {@link String} in the
     *                              form "row,column".
     */
    @Override
    public void addLayoutComponent(Component comp, Object RCPositionConstraints) {
        if (RCPositionConstraints == null || comp == null) {
            throw new NullPointerException("Component or RCPositionConstraints is null.");
        }
        RCPosition pos;
        if (!(RCPositionConstraints instanceof RCPosition)) {
            if (!(RCPositionConstraints instanceof String strPos)) {
                throw new CalcLayoutException("RCPositionConstraints must be an instance of RCPosition, or a string in the format \"row,column\".");
            }
            pos = RCPosition.parse(strPos);
        } else pos = (RCPosition) RCPositionConstraints;

        int row = pos.getRow();
        int column = pos.getColumn();
        if (row < 1 || row > ROWS) {
            throw new CalcLayoutException("Row must be between 1 and 5, got " + row);
        }
        if (column < 1 || column > COLUMNS) {
            throw new CalcLayoutException("Column must be between 1 and 7, got " + column);
        }
        if (row == 1 && column > 1 && column < 6) {
            throw new CalcLayoutException("Invalid position.");
        }
        if (components[row - 1][column - 1] != null) {
            throw new CalcLayoutException("Position (" + row + ", " + column + ") is already occupied.");
        }
        components[row - 1][column - 1] = comp;
    }

    /**
     * Unused.
     */
    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    /**
     * Unused.
     */
    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    /**
     * Unused.
     */
    @Override
    public void invalidateLayout(Container target) {
    }

    /**
     * Throws an exception, unsupported operation.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("This method is not supported.");
    }

    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (components[i][j] == comp) {
                    components[i][j] = null;
                    return;
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(parent, LayoutSizeType.MIN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension maximumLayoutSize(Container parent) {
        return getLayoutSize(parent, LayoutSizeType.MAX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return getLayoutSize(parent, LayoutSizeType.PREF);
    }

    /**
     * Lays out the specific container.
     *
     * @param parent the container to be laid out
     */
    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int width = parent.getWidth() - insets.left - insets.right;
        int height = parent.getHeight() - insets.top - insets.bottom;
        int[] widths = new int[COLUMNS];
        int[] heights = new int[ROWS];
        int componentWidth = (width - (COLUMNS - 1) * gap) / COLUMNS;
        int componentHeight = (height - (ROWS - 1) * gap) / ROWS;
        Arrays.fill(heights, componentHeight);
        Arrays.fill(widths, componentWidth);
        int lastColumnEnd = COLUMNS * componentWidth + (COLUMNS - 1) * gap;
        int lastRowEnd = ROWS * componentHeight + (ROWS - 1) * gap;
        int endGapRight = width - lastColumnEnd;
        int endGapBottom = height - lastRowEnd;

        {
            int i = 0;
            while (endGapRight != 0) {
                widths[i++]++;
                endGapRight--;
                i++;
                if (i > widths.length) i %= widths.length;
            }
            i = 0;
            while (endGapBottom != 0) {
                heights[i++]++;
                endGapBottom--;
                i++;
                if (i > heights.length) i %= heights.length;
            }
        }

        for (int row = 0; row < ROWS; row++){
            for (int col = 0; col < COLUMNS; col++) {
                if (components[row][col] == null) continue;
                int x = (col - 1) * gap + col * componentWidth + gap;
                int y = (row - 1) * gap + row * componentHeight + gap;
                int widthMul = specialPositions.getOrDefault(new RCPosition(row + 1, col + 1), 1);
                components[row][col].setBounds(x, y, widthMul * widths[col], heights[row]);
            }
        }
    }


    /**
     * Returns the size of the layout, whether it's the minimum, maximum or preferred size.
     *
     * @param parent   the container to be laid out
     * @param sizeType the type of size to be returned, either minimum, maximum or preferred, as defined in
     *                 {@link LayoutSizeType}
     * @return the size of the layout
     */
    public Dimension getLayoutSize(Container parent, ISizeGetter sizeType) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (components[i][j] == null) continue;
                Dimension d = sizeType.get(components[i][j]);
                RCPosition currRCPos = new RCPosition(i + 1, j + 1);
                int width = d.width;
                if (specialPositions.containsKey(currRCPos)) {
                    int specialWidthValue = specialPositions.get(currRCPos);
                    width = (d.width - (specialWidthValue - 1) * gap) / specialWidthValue;

                }
                if (x < width) x = width;
                if (d.height > y) y = d.height;
            }
        }
        return new Dimension(x * COLUMNS + gap * (COLUMNS - 1), y * ROWS + gap * (ROWS - 1));
    }
}
