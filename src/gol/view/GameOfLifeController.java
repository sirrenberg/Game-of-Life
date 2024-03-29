package gol.view;

import static java.util.Objects.requireNonNull;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import gol.model.Cell;
import gol.model.Model;
import gol.model.Shape;

/**
 * The controller that takes the actions from the user and handles them accordingly. For this the
 * controller either invokes the necessary model-methods, or by directly telling the view to change
 * its graphical user-interface.
 */
public class GameOfLifeController implements Controller {

  private final Model model;
  private View view;
  private int stepSpeed = 200;
  private boolean stepping = false;

  /**
   * Create a new controller for the specific model that it gets passed.
   *
   * @param gameOfLife the model of Game of Life.
   */
  public GameOfLifeController(Model gameOfLife) {
    model = requireNonNull(gameOfLife);
  }

  @Override
  public void setView(View view) {
    this.view = requireNonNull(view);
  }

  @Override
  public void start() {
    view.showGame();
  }

  @Override
  public void clearBoard() {
    model.clear();
  }

  @Override
  public void setCellAlive(int column, int row, boolean alive) {
    if (cellIsOnBoard(column, row)) {
      model.setCellAlive(column, row);
    }
  }

  /**
   * Check if the cell defined by the column and row numbers is on the board/grid. Or if it's
   * somewhere outside.
   *
   * @param column horizontal position of the cell on the grid.
   * @param row    vertical position of the cell on the grid.
   * @return <code>true</code> if the cell is on the board. Else <code>false</code>.
   */
  private boolean cellIsOnBoard(int column, int row) {
    if ((column >= 0) && (row >= 0) && (column < model.getColumns()) && (row < model.getRows())) {
      return true;
    }
    return false;
  }

  @Override
  public boolean step() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            model.next();
          }
        });
        return null;
      }
    };
    worker.execute();
    return false;
  }

  @Override
  public void stepIndefinitely() {
    stepping = true;
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        while (stepping) {
          SwingUtilities.invokeLater(() -> {
            step();
          });
          try {
            Thread.sleep(stepSpeed);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        return null;
      }
    };
    worker.execute();
  }

  @Override
  public void setStepSpeed(int value) {
    this.stepSpeed = value;
  }

  @Override
  public void stopStepping() {
    stepping = false;
  }

  @Override
  public void setShape(Shape shape) {
    if (shape.getColumns() > model.getColumns() || shape.getRows() > model.getRows()) {
      view.showErrorMessage("Grid is too small for the selected shape!");
      return;
    } else {
      // center the shape
      // offsetX determines the distance from left border to the center of the board
      int offsetX = (model.getColumns() - shape.getColumns()) / 2;
      // offsetY determines the distance from top border to the center of the board
      int offsetY = (model.getRows() - shape.getRows()) / 2;

      clearBoard();

      // draw shape
      for (Cell coord : shape.getCells()) {
        setCellAlive(coord.getColumn() + offsetX, coord.getRow() + offsetY, true);
      }
    }
  }

  @Override
  public void resize(int width, int height) {
    if ((width > 0) && (height > 0)) {
      model.resize(width, height);
    }
  }

  @Override
  public void dispose() {
    stepping = false;
    model.removePropertyChangeListener(view);
  }
}
