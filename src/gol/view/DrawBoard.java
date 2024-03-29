package gol.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serial;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gol.model.Cell;
import gol.model.Model;

/**
 * The tool visualizing the living and dead cells.
 */
public class DrawBoard extends JPanel implements PropertyChangeListener {

  @Serial
  private static final long serialVersionUID = 1L;

  private Model model;
  private Controller controller;

  private Color colorLivingCell = new Color(255, 50, 0);
  private Color colorDeadCell = Color.BLACK;
  private Color colorBackground = new Color(20, 06, 96);
  private int spaceBetweenCells = 2;

  public static final int CELL_SIZE_SMALL_PXL = 5;
  public static final int CELL_SIZE_MEDIUM_PXL = 10;
  public static final int CELL_SIZE_BIG_PXL = 20;

  boolean mousePressed = false;
  boolean pressedCellAlive = false;

  private int cellSizeInPxl = 10;

  /**
   * Create a new Grid that represents the data from the model and communicates with the controller
   * if it gets changed.
   *
   * @param model      the model containing the information and data of the current game.
   * @param controller the controller validating input and handling the input it gets from the view.
   */
  public DrawBoard(Model model, Controller controller) {
    this.model = model;
    this.controller = controller;
    resize();
    addEventListener();
  }

  /**
   * Return the size of the cells that are shown on the DrawBoard.
   *
   * @return size of the cells measured in pixels.
   */
  public int getCellSizeInPxl() {
    return cellSizeInPxl;
  }

  /**
   * Set the size of the cells that are shown on the DrawBoard.
   *
   * @param cellSizeInPxl new size of the cells measured in pixels.
   */
  public void setCellSizeInPxl(int cellSizeInPxl) {
    this.cellSizeInPxl = cellSizeInPxl;
  }

  /**
   * Add a Listener to the DrawBoard that handles the interactions the user can have with the
   * DrawBoard. <br>
   * That is: <br>
   * <li>Clicking on a cell in order to turn it from dead to alive or vice versa </li>
   * <li>Holding the left mouse button down and moving over the field. If the first cell the cursor
   * hovered on was alive, every cell it hovers on is set dead. Else, every cell the cursor hovers
   * on is set alive.</li>
   */
  private void addEventListener() {
    addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        int col = point.x / (cellSizeInPxl + spaceBetweenCells);
        int row = point.y / (cellSizeInPxl + spaceBetweenCells);
        if (model.isCellAlive(col, row)) {
          controller.setCellAlive(col, row, false);
        } else {
          controller.setCellAlive(col, row, true);
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        mousePressed = true;
        Point point = e.getPoint();
        int col = point.x / (cellSizeInPxl + spaceBetweenCells);
        int row = point.y / (cellSizeInPxl + spaceBetweenCells);
        if (model.isCellAlive(col, row)) {
          pressedCellAlive = true;
        } else {
          pressedCellAlive = false;
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        mousePressed = false;
      }
    });

    addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        if (mousePressed) {
          Point point = e.getPoint();
          int col = point.x / (cellSizeInPxl + spaceBetweenCells);
          int row = point.y / (cellSizeInPxl + spaceBetweenCells);
          if ((col < model.getColumns()) && (row < model.getRows()) && (col >= 0) && (row >= 0)) {
            if (pressedCellAlive) {
              if (model.isCellAlive(col, row)) {
                model.setCellDead(col, row);
              }
            } else {
              if (!model.isCellAlive(col, row)) {
                model.setCellAlive(col, row);
              }
            }
          }
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;

    g2D.setColor(colorBackground);
    g2D.fillRect(0, 0, getWidth(), getHeight());

    for (int rowIterator = 0; rowIterator < model.getRows(); rowIterator++) {
      for (int colIterator = 0; colIterator < model.getColumns(); colIterator++) {
        if (model.getPopulation().contains(new Cell(colIterator, rowIterator))) {
          drawCell(colIterator, rowIterator, g2D, true);
        } else {
          drawCell(colIterator, rowIterator, g2D, false);
        }
      }
    }
  }

  /**
   * Set the color of a cell according to its state.
   *
   * @param col   column number of the cell that should be dyed.
   * @param row   row number of the cell that should be dyed.
   * @param g2D   the graphics object used to draw on the DrawBoard.
   * @param alive the state of the cell, i.e. dead or alive.
   */
  private void drawCell(int col, int row, Graphics2D g2D, boolean alive) {
    if (alive) {
      g2D.setColor(colorLivingCell);
    } else {
      g2D.setColor(colorDeadCell);
    }
    int horizontalPosInPxls = col * (cellSizeInPxl + spaceBetweenCells);
    int verticalPosInPxls = row * (cellSizeInPxl + spaceBetweenCells);
    g2D.fillRect(horizontalPosInPxls, verticalPosInPxls, cellSizeInPxl, cellSizeInPxl);
  }

  /**
   * Return the number of pixels separating two cells from each other.
   *
   * @return the number of pixels between two cells.
   */
  public int getSpaceBetweenCells() {
    return spaceBetweenCells;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    SwingUtilities.invokeLater(() -> handleChangeEvent(event));
  }

  /**
   * Change the size of the DrawBoard and draw it again.
   * This method is invoked if the size of the model is changed, so that the model contains more
   * or less cells. This change of number in cells has to be shown on the DrawBoard.
   *
   * @param event the event that contains the information about the changed size in the model.
   */
  private void handleChangeEvent(PropertyChangeEvent event) {
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      resize();
      repaint();
    }
  }

  /**
   * Change the number of cells in horizontal and/or vertical extent based on the size of the model.
   */
  private void resize() {
    int sizeX = model.getColumns() * cellSizeInPxl + (model.getColumns() - 1) * spaceBetweenCells;
    int sizeY = model.getRows() * cellSizeInPxl + (model.getRows() - 1) * spaceBetweenCells;
    this.setPreferredSize(new Dimension(sizeX, sizeY));
  }

  /**
   * Remove the DrawBoard as PropertyChangeListener.
   */
  public void dispose() {
    model.removePropertyChangeListener(this);
  }
}



