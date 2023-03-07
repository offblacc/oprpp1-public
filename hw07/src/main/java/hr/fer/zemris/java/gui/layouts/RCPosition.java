package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Class that represents a position in a (5 by 7) grid.
 *
 * @author offblacc
 */
public class RCPosition {
    /**
     * Row in the grid.
     */
    int row;

    /**
     * Column in the grid.
     */
    int column;

    /**
     * Constructor that creates a new RCPosition object.
     * @param row Row in the grid.
     * @param column Column in the grid.
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Getter for the row.
     * @return Row in the grid.
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for the column.
     * @return Column in the grid.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Method that parses a string and returns an RCPosition object.
     * String must be in the format "row,column".
     * @param text String to be parsed.
     * @return RCPosition object.
     */
    public static RCPosition parse(String text) {
        String[] list = text.split(",");
        if (list.length != 2) {
            throw new IllegalArgumentException("Invalid format.");
        }
        try {
            int row = Integer.parseInt(list[0].trim());
            int column = Integer.parseInt(list[0].trim());
            return new RCPosition(row, column);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid format.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RCPosition that = (RCPosition) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
