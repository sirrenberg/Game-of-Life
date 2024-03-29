package gol.view;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gol.model.Model;
import gol.model.ShapeCollection;
import gol.model.Shape;

/**
 * The view of the Game Of Life program. All data of GOL that is shown to the user is depicted as
 * specified in this class.
 */
public class GameOfLifeView extends JFrame implements View {


  @Serial
  private static final long serialVersionUID = 1L;

  private final Model model;
  private final Controller controller;

  private final DrawBoard drawBoard;

  private boolean isStepping = false;

  private static final int STEP_SPEED_SUPER_SLOW = 1000;
  private static final int STEP_SPEED_SLOW = 200;
  private static final int STEP_SPEED_MEDIUM = 100;
  private static final int STEP_SPEED_FAST = 50;

  private JComboBox<String> shapesComboBox;
  private JButton nextButton;
  private JButton startButton;
  private JButton clearButton;
  private JComboBox<String> speedComboBox;
  private JComboBox<String> sizeComboBox;
  private JLabel generationLabel;

  /**
   * Construct a new view with no living cells and default settings for size and animation speed.
   *
   * @param model      The model which data the view displays.
   * @param controller The controller with which the view interacts with the model.
   */
  public GameOfLifeView(Model model, Controller controller) {
    super("Game of Life");
    this.model = requireNonNull(model);
    this.controller = requireNonNull(controller);

    drawBoard = new DrawBoard(model, controller);
    model.addPropertyChangeListener(drawBoard);

    defineBehaviourOnResize();
    initializeControls();
    JPanel controlsPanel = arrangeControls();

    Container contentPane = this.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(drawBoard, BorderLayout.CENTER);
    contentPane.add(controlsPanel, BorderLayout.SOUTH);

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setMinimumSize(new Dimension(900, 100));
    this.pack();
    centre();
  }

  @Override
  public void showGame() {
    this.setVisible(true);
  }

  @Override
  public void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error!",
        JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void startStepping() {
    isStepping = true;
    startButton.setText("stop");
    nextButton.setEnabled(false);
    shapesComboBox.setEnabled(false);
  }

  @Override
  public void stopStepping() {
    isStepping = false;
    startButton.setText("start");
    nextButton.setEnabled(true);
    shapesComboBox.setEnabled(true);
  }

  @Override
  public void dispose() {
    drawBoard.dispose();
    controller.dispose();
    model.removePropertyChangeListener(this);
    super.dispose();
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    SwingUtilities.invokeLater(() -> handleChangeEvent(event));
  }

  /**
   * The observable (= model) has just published that its state has changed. The GUI needs to be
   * updated accordingly here.
   *
   * @param event The event that was fired by the model.
   */
  private void handleChangeEvent(PropertyChangeEvent event) {
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      generationLabel.setText("Generation: " + model.getGenerations());
    }
  }

  /**
   * Create a ComboBox from which the user can select predefined shapes. The selected shape is then
   * forwarded to the controller in order to be shown on the screen.
   */
  private void initializeShapesComboBox() {
    shapesComboBox = new JComboBox<>(
        new String[] {"Block", "Boat", "Blinker", "Toad", "Glider", "Spaceship", "Pulsar",
            "Small Exploder", "Exploder", "10 Cell Row", "Tumbler", "Gosper Glider Gun",
            "r-Pentomino", "pi-Heptomino", "232P7H3V0", "Lightweight Spaceship"});
    shapesComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String shapeName = (String) shapesComboBox.getSelectedItem();
        Shape shape = ShapeCollection.getShapeByName(shapeName).orElseThrow();
        controller.setShape(shape);
      }
    });
  }

  /**
   * If the size of the window is changed, the controller gets informed to change the size of the
   * model accordingly.
   */
  private void defineBehaviourOnResize() {
    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        resizeModel();
      }
    });
  }

  /**
   * Create the button that enables the user to start a simulation of Game of Life, where
   * generations are computed and shown to the user until he presses the button again.
   * The speed in which these generations are computed is defined by the selected speed in the
   * speedComboBox.
   */
  private void initializeStartButton() {
    startButton = new JButton("start");
    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (isStepping) {
          controller.stopStepping();
          stopStepping();
        } else {
          //set the default speed so it matches the speed on the speedComboBox
          setSpeedOnController((String) speedComboBox.getSelectedItem());
          controller.stepIndefinitely();
          startStepping();
        }
      }
    });
  }

  /**
   * Create the button that enables the user to compute one new generation of the game of life.
   */
  private void initializeNextButton() {
    nextButton = new JButton("next");
    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.step();
      }
    });
  }

  /**
   * Create the button that enables the user to set all cells on the board to the state 'dead'.
   */
  private void initializeClearButton() {
    clearButton = new JButton("clear");
    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.clearBoard();
      }
    });
  }

  /**
   * Create the ComboBox from which the user can select predefined speed settings that describes
   * how fast the next generation is computed.
   */
  private void initializeSpeedComboBox() {
    speedComboBox = new JComboBox<>(new String[] {"super slow", "slow", "medium speed", "fast"});
    speedComboBox.setSelectedIndex(2);
    speedComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String selected = (String) speedComboBox.getSelectedItem();
        setSpeedOnController(selected);
      }
    });
  }

  /**
   * Set the speed that describes how fast the next generation is computed.
   *
   * @param speed the name of the predefined speed.
   */
  private void setSpeedOnController(String speed) {
    switch (speed) {
      case "super slow" -> controller.setStepSpeed(STEP_SPEED_SUPER_SLOW);
      case "slow" -> controller.setStepSpeed(STEP_SPEED_SLOW);
      case "medium speed" -> controller.setStepSpeed(STEP_SPEED_MEDIUM);
      case "fast" -> controller.setStepSpeed(STEP_SPEED_FAST);
      default -> throw new AssertionError("Unknown step speed.");
    }
  }

  /**
   * Create the ComboBox from which the user can select predefined sizes that describe how big the
   * cells are.
   */
  private void initializeSizeComboBox() {
    sizeComboBox = new JComboBox<>(new String[] {"small", "medium sized", "big"});
    sizeComboBox.setSelectedIndex(1);
    sizeComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String selectedSpeed = (String) sizeComboBox.getSelectedItem();
        switch (selectedSpeed) {
          case "small" -> drawBoard.setCellSizeInPxl(DrawBoard.CELL_SIZE_SMALL_PXL);
          case "medium sized" -> drawBoard.setCellSizeInPxl(DrawBoard.CELL_SIZE_MEDIUM_PXL);
          case "big" -> drawBoard.setCellSizeInPxl(DrawBoard.CELL_SIZE_BIG_PXL);
          default -> throw new AssertionError("Unknown cell size.");
        }
        resizeModel();
      }
    });
  }

  /**
   * Create the label that states in which generation the model currently is in.
   */
  private void initializeGenerationLabel() {
    generationLabel = new JLabel("Generation " + model.getGenerations());
  }

  /**
   * Initialize all control elements that are needed to operate the game of life.
   */
  private void initializeControls() {
    initializeShapesComboBox();
    initializeStartButton();
    initializeNextButton();
    initializeClearButton();
    initializeSpeedComboBox();
    initializeSizeComboBox();
    initializeGenerationLabel();
  }

  /**
   * Arrange all control elements from left to right on a JPanel and return that JPanel.
   *
   * @return the reference to the JPanel on which all control elements are arranged.
   */
  private JPanel arrangeControls() {
    JPanel controlsPanel = new JPanel(new FlowLayout());
    controlsPanel.add(shapesComboBox);
    controlsPanel.add(nextButton);
    controlsPanel.add(startButton);
    controlsPanel.add(clearButton);
    controlsPanel.add(speedComboBox);
    controlsPanel.add(sizeComboBox);
    controlsPanel.add(generationLabel);
    return controlsPanel;
  }

  /**
   * Center the game window, so it doesn't always appear in the top left corner of the screen.
   */
  private void centre() {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth()) - this.getWidth()) / 2;
    int y = (int) ((dimension.getHeight()) - this.getHeight()) / 2;
    this.setLocation(x, y);
  }

  /**
   * Resize the model, i.e. change the number of columns and rows of the model, so they fit onto
   * the DrawBoard.
   */
  private void resizeModel() {
    int colsFittingInDrawBoard = (drawBoard.getWidth() - drawBoard.getSpaceBetweenCells())
        / (drawBoard.getCellSizeInPxl() + drawBoard.getSpaceBetweenCells());
    int rowsFittingInDrawBoard = (drawBoard.getHeight() - drawBoard.getSpaceBetweenCells())
        / (drawBoard.getCellSizeInPxl() + drawBoard.getSpaceBetweenCells());
    if ((colsFittingInDrawBoard != model.getColumns())
        || (rowsFittingInDrawBoard != model.getRows())) {
      controller.resize(colsFittingInDrawBoard + 1, rowsFittingInDrawBoard + 1);
    }
  }
}
