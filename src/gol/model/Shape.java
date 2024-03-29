package gol.model;

import java.util.HashSet;
import java.util.Set;

/**
 * A shape contains the data for one (predefined) shape.
 */
public class Shape {

  private final String name;

  private final Set<Cell> shape;

  private final int cols;
  private final int rows;


  /**
   * Constructs a new shape.
   *
   * @param name  Name of the shape.
   * @param cells Shape data as cells.
   */
  public Shape(String name, Set<Cell> cells) {
    this.name = name;
    shape = new HashSet<>(cells);

    cols = cells.stream().mapToInt(Cell::getColumn).max().orElse(0) + 1;
    rows = cells.stream().mapToInt(Cell::getRow).max().orElse(0) + 1;
  }

  /**
   * Gets the width of the shape.
   *
   * @return The number of columns.
   */
  public int getColumns() {
    return cols;
  }

  /**
   * Gets the height of the shape.
   *
   * @return The number of rows.
   */
  public int getRows() {
    return rows;
  }

  /**
   * Get the shape data.
   *
   * @return A copy of a set containing the coordinates of members of this shape.
   */
  public Set<Cell> getCells() {
    return new HashSet<>(shape);
  }

  /**
   * Get the name of the shape.
   *
   * @return The name of the shape.
   */
  @Override
  public String toString() {
    return name;
  }

}
