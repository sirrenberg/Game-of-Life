package gol.model;

import java.util.Collection;

/**
 * Interface between GameBoard and Game. More accurately, for Game of Life this is an interface
 * between the grid of cells and the user interface.
 */
public interface Grid {

  /**
   * Gets the status of a cell (alive or dead).
   *
   * @param col x-position.
   * @param row y-position.
   * @return <code>true</code> if the cell is alive, <code>false</code> otherwise.
   */
  boolean isCellAlive(int col, int row);

  /**
   * Sets a cell alive.
   *
   * @param col x-position.
   * @param row y-position.
   */
  void setCellAlive(int col, int row);

  /**
   * Puts a cell into a dead state.
   *
   * @param col x-position.
   * @param row y-position.
   */
  void setCellDead(int col, int row);

  /**
   * Resizes the cell grid in x and y direction.
   *
   * @param cols New number of columns.
   * @param rows New number of rows.
   */
  void resize(int cols, int rows);

  /**
   * Gets the dimension of the cell grid in x direction.
   *
   * @return Number of columns.
   */
  int getColumns();

  /**
   * Gets the dimension of the cell grid in y direction.
   *
   * @return Number of rows.
   */
  int getRows();

  /**
   * Gets all living cells.
   *
   * @return Set of all cells which are alive.
   */
  Collection<Cell> getPopulation();

  /**
   * Clears the grid.
   */
  void clear();

  /**
   * Computes the next generation.
   */
  void next();

  /**
   * Gets the number of generations in this game.
   *
   * @return The current generation.
   */
  int getGenerations();

  /**
   * Gets the string representation of the current game state.
   *
   * @return The matrix as string.
   */
  String toString();

}
