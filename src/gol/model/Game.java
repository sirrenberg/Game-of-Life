package gol.model;

import static java.util.Objects.requireNonNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Contains the grid, the current shape and the Game Of Life algorithm that changes it.
 *
 * <p>The universe of the Game of Life is an infinite two-dimensional orthogonal
 * grid of square cells, each of which is in one of two possible states, alive or dead. Every cell
 * interacts with its eight neighbors, which are the cells that are directly horizontally,
 * vertically, or diagonally adjacent. At each step in time, the following transitions (= genetic
 * rules) occur:
 *
 * <ol>
 * <li>Deaths. Every cell with none or one alive neighboring cells dies (is
 * removed) due to isolation for the next generation. Every cell with four or
 * more alive neighboring cells dies due to overcrowding for the next
 * generation.</li>
 * <li>Survivals. Every cell with two or three alive neighboring cells survives
 * (stays) for the next generation.</li>
 * <li>Births. Each dead cell adjacent to exactly three alive neighboring cells
 * - no more, no fewer - is born (added) for the next generation.</li>
 * </ol>
 *
 * <p>The initial pattern constitutes the 'seed' of the system. The first
 * generation is created by applying the above rules simultaneously to every
 * cell in the seed in which births and deaths happen simultaneously, and the
 * discrete moment at which this happens is sometimes called a tick. (In other
 * words, each generation is a pure function of the one before.) The rules
 * continue to be applied repeatedly to create further generations.
 */
public class Game implements Model {

  /**
   * Initial number of columns when the game is shown.
   */
  private static final int INITIAL_COLUMNS = 50;

  /**
   * Initial number of rows when the game is shown.
   */
  private static final int INITIAL_ROWS = 30;

  // Staying alive in this range
  private static final int STAY_ALIVE_MIN_NEIGHBORS = 2;
  private static final int STAY_ALIVE_MAX_NEIGHBORS = 3;

  // Condition for getting born
  private static final int NEWBORN_NEIGHBORS = 3;

  private final PropertyChangeSupport support;

  // LinkedHashSet provides add, contains, remove in O(1) and iteration over its elements in O(n)
  private final Set<Cell> population = new LinkedHashSet<>();

  // size of the grid, optional as the grid array knows its dimensions
  private int cols;
  private int rows;
  private int generations = 0;

  /*
   * Every cell on the grid is a Cell object. The first dimension are the columns, the second the
   * rows.
   *
   * <p>This object provides a convenient way of accessing cells in a quick manner. Note that it
   * does not store the state of a cell, i.e. whether it is dead or alive (this is done in the
   * population-collection).
   */
  private Cell[][] board;

  /**
   * Constructs a new game with a default size of {@link Game#INITIAL_COLUMNS} and  of {@link
   * Game#INITIAL_ROWS} consisting solely of dead cells.
   */
  public Game() {
    this(INITIAL_COLUMNS, INITIAL_ROWS);
  }

  /**
   * Constructs a new game consisting solely of dead cells.
   *
   * @param cols Number of columns.
   * @param rows Number of rows.
   */
  public Game(int cols, int rows) {
    if (cols <= 0 || rows <= 0) {
      throw new IllegalArgumentException("Number of columns and rows must be positive");
    }

    support = new PropertyChangeSupport(this);

    this.cols = cols;
    this.rows = rows;

    board = new Cell[cols][rows];
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        board[i][j] = new Cell(i, j); // each cell has unique coordinates
      }
    }
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    requireNonNull(pcl);
    support.addPropertyChangeListener(pcl);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    requireNonNull(pcl);
    support.removePropertyChangeListener(pcl);
  }

  /**
   * Invokes the model to fire a new event, such that any attached observer (i.e., {@link
   * PropertyChangeListener}) gets notified about a change in this model.
   */
  private void notifyListeners() {
    support.firePropertyChange(STATE_CHANGED, null, this);
  }

  @Override
  public synchronized void resize(int cols, int rows) {
    if (this.cols == cols && this.rows == rows) {
      return; // nothing to do
    }

    // create a new board, reusing existing Cells
    Cell[][] board = new Cell[cols][rows];
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        if (i < this.cols && j < this.rows) {
          board[i][j] = this.board[i][j];
        } else {
          board[i][j] = new Cell(i, j);
        }
      }
    }

    // Remove cells from the current population if they are no longer in range.
    population.removeIf(cell -> cell.getColumn() >= cols || cell.getRow() >= rows);

    this.board = board;
    this.cols = cols;
    this.rows = rows;

    notifyListeners();
  }

  @Override
  public synchronized int getColumns() {
    return cols;
  }

  @Override
  public synchronized int getRows() {
    return rows;
  }

  @Override
  public synchronized int getGenerations() {
    return generations;
  }

  @Override
  public synchronized void clear() {
    generations = 0;
    population.clear();

    notifyListeners();
  }

  @Override
  public synchronized boolean isCellAlive(int col, int row) {
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    } else if (col >= cols || row >= rows) {
      throw new IllegalArgumentException("Parameters for column and row may not exceed "
          + "the maximum number of columns and rows");
    }

    return population.contains(board[col][row]);
  }

  @Override
  public synchronized Collection<Cell> getPopulation() {
    return new LinkedList<>(population);
  }

  @Override
  public void setCellAlive(int col, int row) {
    setCellWithoutNotification(col, row, true);
    notifyListeners();
  }

  @Override
  public void setCellDead(int col, int row) {
    setCellWithoutNotification(col, row, false);
    notifyListeners();
  }

  /**
   * Put a cell into either a living or dead state.
   *
   * <p>This method does not make a call to {@link #notifyListeners()}, use the methods
   * {@linkplain Game#setCellAlive(int, int) setCellAlive(int, int)} or {@linkplain
   * Game#setCellDead(int, int) setCellDead(int, int)}instead</p>
   *
   * @param col   The column of the cell
   * @param row   The row of the cell
   * @param alive if <code>true</code>, the cell is set alive; otherwise it is put into its dead
   *              state
   */
  private synchronized void setCellWithoutNotification(int col, int row, boolean alive) {
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    } else if (col >= cols || row >= rows) {
      throw new IllegalArgumentException("Parameters for column and row may not exceed "
          + "the maximum number of columns and rows");
    }

    Cell cell = board[col][row];
    if (alive) {
      population.add(cell); // no addition if already present
    } else {
      population.remove(cell); // no removal if cell not present
    }
  }

  // The core algorithm. Creates the population of the next generation.
  @Override
  public synchronized void next() {
    // Stores all cells including the amount of living neighbors which might belong to the next
    // population. This also includes cells with too few or too many neighbors
    Map<Cell, Integer> potentialCandidate = new HashMap<>();

    // Only cells are taken into account that already have living cells as neighbors.
    // It is useless to check (dead) cells that are surrounded only by dead neighbors, since they
    // will remain in their dead state anyway.
    for (Cell cell : population) {
      for (int colOffset = -1; colOffset <= 1; colOffset++) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
          // The current cell is alive, since it has been in the population collection.
          // We next increment the counter of all neighbor cells by one to account for this living
          // cell.
          if (!(colOffset == 0 && rowOffset == 0)) {
            addNeighbor(potentialCandidate, cell.getColumn() + colOffset,
                cell.getRow() + rowOffset);
          }
        }
      }
    }

    // Living cells without any (living) neighbors are not considered in the above loop and thus do
    // not get updated accordingly. They are added now to prevent them from staying alive when
    // in fact they need to get buried in the next round.
    for (Cell cell : population) {
      potentialCandidate.putIfAbsent(cell, 0);
    }

    for (Entry<Cell, Integer> entry : potentialCandidate.entrySet()) {
      Cell cell = entry.getKey();
      Integer aliveNeighbors = entry.getValue();

      // bury the dead (genetic rule 1)
      if (aliveNeighbors < STAY_ALIVE_MIN_NEIGHBORS || aliveNeighbors > STAY_ALIVE_MAX_NEIGHBORS) {
        setCellWithoutNotification(cell.getColumn(), cell.getRow(), false);
      }

      // the others in the current population stay alive (genetic rule 2)

      // genetic rule (3)
      if (aliveNeighbors == NEWBORN_NEIGHBORS) {
        setCellWithoutNotification(cell.getColumn(), cell.getRow(), true);
      }
    }

    generations++;

    notifyListeners();
  }

  /**
   * Accounts for a neighboring cell of a currently inspected cell.
   *
   * @param potentialNextPopulation Collection of all cells whose neighbors have been alive in the
   *                                previous round.
   * @param cellCol                 The column of the neighboring cell on the board
   * @param cellRow                 The row of the neighboring cell on the board
   */
  private void addNeighbor(Map<Cell, Integer> potentialNextPopulation, int cellCol, int cellRow) {
    if (cellCol < 0 || cellCol >= cols || cellRow < 0 || cellRow >= rows) {
      // Cell is not on the board
      return;
    }

    Cell cell = board[cellCol][cellRow];
    if (potentialNextPopulation.containsKey(cell)) {
      int neighbours = potentialNextPopulation.get(cell);
      potentialNextPopulation.put(cell, neighbours + 1);
    } else {
      potentialNextPopulation.put(cell, 1);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < rows; ++row) {
      for (int col = 0; col < cols; ++col) {
        sb.append((isCellAlive(col, row)) ? "X" : ".");
      }

      if (row < rows - 1) {
        sb.append(System.lineSeparator());
      }
    }

    return sb.toString();
  }
}

