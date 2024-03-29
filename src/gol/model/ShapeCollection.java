package gol.model;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Collection of shapes containing some of the more popular Game Of Life shapes.
 */
public final class ShapeCollection {

  private static final Map<String, Shape> SHAPES = new LinkedHashMap<>();

  static {
    HashSet<Cell> cells;

    cells = new HashSet<>();
    addPoint(cells, 0, 0);
    addPoint(cells, 0, 1);
    addPoint(cells, 1, 0);
    addPoint(cells, 1, 1);
    createShape(new Shape("Block", cells));

    cells = new HashSet<>();
    addPoint(cells, 0, 0);
    addPoint(cells, 0, 1);
    addPoint(cells, 1, 0);
    addPoint(cells, 1, 2);
    addPoint(cells, 2, 1);
    createShape(new Shape("Boat", cells));

    cells = new HashSet<>();
    addPoint(cells, 0, 0);
    addPoint(cells, 1, 0);
    addPoint(cells, 2, 0);
    createShape(new Shape("Blinker", cells));

    cells = new HashSet<>();
    addPoint(cells, 0, 1);
    addPoint(cells, 1, 0);
    addPoint(cells, 1, 1);
    addPoint(cells, 2, 0);
    addPoint(cells, 2, 1);
    addPoint(cells, 3, 0);
    createShape(new Shape("Toad", cells));

    cells = new HashSet<>();
    addPoint(cells, 0, 0);
    addPoint(cells, 0, 1);
    addPoint(cells, 1, 0);
    addPoint(cells, 1, 2);
    addPoint(cells, 2, 0);
    createShape(new Shape("Glider", cells));

    cells = new HashSet<>();
    addPoint(cells, 0, 1);
    addPoint(cells, 0, 2);
    addPoint(cells, 0, 3);
    addPoint(cells, 1, 0);
    addPoint(cells, 1, 3);
    addPoint(cells, 2, 3);
    addPoint(cells, 3, 3);
    addPoint(cells, 4, 0);
    addPoint(cells, 4, 2);
    createShape(new Shape("Spaceship", cells));

    // The 2nd generation needs 15x15 grid instead of 13x13 as defined,
    // otherwise it cannot oscillate in 3 phases.
    cells = new HashSet<>();
    String pulsar = """
        ..XX.....XX..
        ...XX...XX...
        X..X.X.X.X..X
        XXX.XX.XX.XXX
        .X.X.X.X.X.X.
        ..XXX...XXX..
        .............
        ..XXX...XXX..
        .X.X.X.X.X.X.
        XXX.XX.XX.XXX
        X..X.X.X.X..X
        ...XX...XX...
        ..XX.....XX..
        """;
    parseMultilineString(cells, pulsar);
    createShape(new Shape("Pulsar", cells));

    cells = new HashSet<>();
    addPoint(cells, 0, 1);
    addPoint(cells, 0, 2);
    addPoint(cells, 1, 0);
    addPoint(cells, 1, 1);
    addPoint(cells, 1, 3);
    addPoint(cells, 2, 1);
    addPoint(cells, 2, 2);
    createShape(new Shape("Small Exploder", cells));

    cells = new HashSet<>();
    addPoint(cells, 0, 0);
    addPoint(cells, 0, 1);
    addPoint(cells, 0, 2);
    addPoint(cells, 0, 3);
    addPoint(cells, 0, 4);
    addPoint(cells, 2, 0);
    addPoint(cells, 2, 4);
    addPoint(cells, 4, 0);
    addPoint(cells, 4, 1);
    addPoint(cells, 4, 2);
    addPoint(cells, 4, 3);
    addPoint(cells, 4, 4);
    createShape(new Shape("Exploder", cells));

    cells = new HashSet<>();
    addPoint(cells, 0, 0);
    addPoint(cells, 1, 0);
    addPoint(cells, 2, 0);
    addPoint(cells, 3, 0);
    addPoint(cells, 4, 0);
    addPoint(cells, 5, 0);
    addPoint(cells, 6, 0);
    addPoint(cells, 7, 0);
    addPoint(cells, 8, 0);
    addPoint(cells, 9, 0);
    createShape(new Shape("10 Cell Row", cells));

    cells = new HashSet<>();
    String tumbler = """
        .XX.XX.
        .XX.XX.
        ..X.X..
        X.X.X.X
        X.X.X.X
        XX...XX
        """;
    parseMultilineString(cells, tumbler);
    createShape(new Shape("Tumbler", cells));

    cells = new HashSet<>();
    String gliderGun = """
        .......................XX.........XX..
        ......................X.X.........XX..
        XX.......XX...........XX..............
        XX......X.X...........................
        ........XX......XX....................
        ................X.X...................
        ................X.....................
        ...................................XX.
        ...................................X.X
        ...................................X..
        ......................................
        ......................................
        ........................XXX...........
        ........................X.............
        .........................X............
        """;
    parseMultilineString(cells, gliderGun);
    createShape(new Shape("Gosper Glider Gun", cells));

    cells = new HashSet<>();
    String rpentomino = """
        .XX
        XX.
        .X.
        """;
    parseMultilineString(cells, rpentomino);
    createShape(new Shape("r-Pentomino", cells));

    cells = new HashSet<>();
    String piHeptomino = """
        XXX
        X.X
        X.X
        """;
    parseMultilineString(cells, piHeptomino);
    createShape(new Shape("pi-Heptomino", cells));

    cells = new HashSet<>();
    String lightweightSpaceship = """
        .....................X...
        ..................XXXX...
        .............X..X.XX.....
        .............X...........
        XXXX........X...X.XX.....
        X...X.....XX.XX.X.X.XXXXX
        X.........XX.X.X.X..XXXXX
        .X..X..XX..X...XXX..X.XX.
        ......X..X.XX............
        ......X....XX............
        ......X..X.XX............
        .X..X..XX..X...XXX..X.XX.
        X.........XX.X.X.X..XXXXX
        X...X.....XX.XX.X.X.XXXXX
        XXXX........X...X.XX.....
        .............X...........
        .............X..X.XX.....
        ..................XXXX...
        .....................X...
        """;
    parseMultilineString(cells, lightweightSpaceship);
    createShape(new Shape("Lightweight Spaceship", cells));

    cells = new HashSet<>();
    String spaceship232P7H3V0 = """
        ...................XXX.........XXX.................
        ..................X...X.......X...X................
        .................X.X...X.....X...X.X...............
        .................X...XX.......XX...X...............
        .................XXX...X.....X...XXX...............
        ................X...XX.XXX.XXX.XX...X..............
        ................XX..X...XX.XX...X..XX..............
        ...............XXX...XXXXX.XXXXX...XXX.............
        .......................X.....X.....................
        ....................X...........X..................
        ...............X....X...........X....X.............
        ...............X....XXXX.....XXXX....X.............
        ...................X....X...X....X.................
        ..................XX.XXX.....XXX.XX................
        ..................XX.X...X.X...X.XX................
        ..............XXX.......XX.XX.......XXX............
        .............X...XX....X.X.X.X....XX...X...........
        ............X...X...................X...X..........
        ............X.........XXX...XXX.........X..........
        ................X......XX...XX......X..............
        ...........X............X...X............X.........
        ...........X..XX.....X.........X.....XX..X.........
        ............XX........X.......X........XX..........
        ..........X............X.....X............X........
        .........XXX.............................XXX.......
        ........XX..X...........................X..XX......
        ...........XX...........................XX.........
        ...........X.............................X.........
        ...................................................
        ........X...................................X......
        .........XX...............................XX.......
        .......X..X...............................X..X.....
        ......X.......................................X....
        .....XX.......................................XX...
        ....XXXX.....................................XXXX..
        ...X.............................................X.
        ...XXX.........................................XXX.
        ..X...............................................X
        ....XX.........................................XX..
        ......X.......................................X....
        ....XX.........................................XX..
        .....X.........................................X...
        ....X...........................................X..
        ....X...........................................X..
        """;
    parseMultilineString(cells, spaceship232P7H3V0);
    createShape(new Shape("232P7H3V0", cells));
  }

  /**
   * Utility class constructor.
   */
  private ShapeCollection() {
    // private constructor to prevent the class from being instantiated
  }

  /**
   * Helper method that adds a given x- and y-coordinate to a set of cells.
   *
   * @param cells The set to which a new cell gets added.
   * @param x     The x-coordinate of the new cell.
   * @param y     The y-coordinate of the new cell.
   */
  private static void addPoint(Set<Cell> cells, int x, int y) {
    cells.add(new Cell(x, y));
  }

  /**
   * A convenience helper method that parses a multiline string for a shape. The coordinates of
   * living cells are turned into {@link Cell}-objects and put into the given set.
   *
   * @param cells           The set that living cells are put into
   * @param multilineString The string that is to be parsed.
   */
  private static void parseMultilineString(Set<Cell> cells, String multilineString) {
    String[] lines = multilineString.split("\\r?\\n");
    int rowIndex = 0;
    while (rowIndex < lines.length) {
      int colIndex = 0;
      char[] array = lines[rowIndex].trim().toCharArray();
      while (colIndex < array.length) {
        if (array[colIndex] == 'x' || array[colIndex] == 'X') {
          addPoint(cells, colIndex, rowIndex);
        }
        colIndex++;
      }
      rowIndex++;
    }
  }

  /**
   * Add a shape to {@link ShapeCollection#SHAPES}.
   *
   * @param shape The new shape.
   */
  private static void createShape(Shape shape) {
    SHAPES.put(shape.toString(), shape);
  }

  /**
   * Gets all available shapes. Needed for the combo box of the gui application.
   *
   * @return An array of shapes ordered by their insertion into {@link ShapeCollection#SHAPES}.
   */
  public static Shape[] getShapes() {
    return SHAPES.values().toArray(new Shape[SHAPES.size()]);
  }

  /**
   * Gets a shape by name.
   *
   * @param name The name of the shape.
   * @return An optional containing either the shape object, or empty if it does not exist.
   */
  public static Optional<Shape> getShapeByName(String name) {
    return Optional.ofNullable(SHAPES.get(name));
  }

  /**
   * Checks if a certain shape exists.
   *
   * @param name The name of the shape to check for.
   * @return {@code true} iff shape exists.
   */
  public static boolean shapeExists(String name) {
    return SHAPES.containsKey(name);
  }

}

