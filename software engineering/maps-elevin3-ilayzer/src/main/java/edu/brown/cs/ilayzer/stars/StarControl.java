package edu.brown.cs.ilayzer.stars;

import edu.brown.cs.ilayzer.repl.Executable;
import edu.brown.cs.ilayzer.kdtree.KDTree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class that provides evaluation functionality to 'plug in' to generalized
 * repl for the Stars project.
 */
public class StarControl {

  private final int dimensions; // the dimension of r-space we are acting in
  private KDTree<Star> tree; // the kd tree to be filled/overwritten on stars <file>
  private Map<String, Star> starNameMap; // a map from star proper id to star

  /**
   * Constructor.
   */
  public StarControl() {
    this.dimensions = 3;
    this.starNameMap = new HashMap<>(); // will be filled on parsing csv
  }

  /**
   * Gets the kd tree used to store Cartesian data.
   * @return the kd tree
   */
  public KDTree<Star> getTree() {
    return tree;
  }

  /**
   * Gets the map from star name to star.
   * @return the starNameMap
   */
  public Map<String, Star> getStarNameMap() {
    return starNameMap;
  }

  /**
   * Parses an array of coordinates in string form to a double array.
   * @param stringCoordinates array of string coordinates
   * @return array of double coordinates
   */
  private static double[] parseCoordinates(String[] stringCoordinates) {
    double[] coords = new double[stringCoordinates.length];
    // fill coords by parsing string to double
    for (int i = 0; i < coords.length; i++) {
      coords[i] = Double.parseDouble(stringCoordinates[i]);
    }
    return coords;
  }

  /**
   * Method that 'recovers' word that was in double quotes in original input
   * but potentially may have been split when tokenizing input by whitespace
   * ex: input: neighbors 5 "Sagittarius B" would be split during tokenizing
   * to be {neighbors, 5, "Sagittarius, B"} and this method would return the
   * string "Sagittarius B" without double quotes.
   * @param args an array of user input arguments
   * @return the string of a name in double quotes if it existed, null otherwise
   */
  public String returnQuotedName(String[] args) {
    // create regex pattern
    String openQuote = "\"(\\w+)+"; // matches "Example
    String closeQuote = "(\\w+)+\""; // matches Name"
    String singleQuotedWord = "\"(\\w+)+\"";

    String toReturn = "";
    boolean beginConcatenating = false;
    for (String s: args) {
      if (!beginConcatenating) {
        // we haven't found an opening quote yet
        if (s.matches(singleQuotedWord)) {
          // we found the single word inside quotations
          return s.substring(1, s.length() - 1);
        } else if (s.matches(openQuote)) {
          // found the beginning of a multi word quoted name
          beginConcatenating = true;
          toReturn += s;
        }
      } else {
        toReturn += " " + s;
        if (s.matches(closeQuote)) {
          // we found the end of a multi word quoted name
          return toReturn.substring(1, toReturn.length() - 1);
        }
      }
    }
    return null;
  }

  /**
   * Shared evaluate functionality between NeighborsCommand and RadiusCommand.
   * @param args the user inputted args array
   * @return the string result of evaluation
   */
  private String starsEvaluate(String[] args) {
    // make sure there is star data loaded into kd tree
    if (tree == null) {
      return "ERROR: No star data loaded.";
    }

    String command = args[0];
    String letter = "k";
    if (command.equals("radius")) {
      letter = "r";
    }
    String usageMessage = "Usage: " + command + " <" + letter
        + "> <Coordinates | Star Proper Id>";

    // check that there are enough arguments
    if (args.length < 3) {
      return "ERROR: " + command + ": not enough arguments. " + usageMessage;
    }
    // check that there are not too many arguments
    if (args.length > 2 + dimensions) {
      return "ERROR: " + command + ": too many arguments. " + usageMessage;
    }
    try {
      // get k or radius depending on command
      int kOrRadius = Integer.parseInt(args[1]);
      // make sure positive
      if (kOrRadius < 0) {
        return "ERROR: argument 'radius' must be a positive number, but found negative number";
      }
      // get quoted star name (ex: Sol in  <neighbors 5 "Sol">) or null if not exists
//      String starName = returnQuotedName(args);
      String starName = null;
      if (args.length == 3) {
        starName = args[2];
        starName = starName.replaceAll("\"", "");
      }
      // Search by quoted star name
      if (starName != null) {
        // we found a quoted name
        // check if we have parsed star of this proper id
        if (starNameMap.containsKey(starName)) {
          // get star and coordinates
          Star star = starNameMap.get(starName);
          double[] targetPoint = star.getCoordinates();

          // list for neighbors
          List<Star> stars = new LinkedList<>();
          if (command.equals("neighbors")) {
            // get nearest k + 1 neighbors (we don't want to include the star)
            stars = tree.nearestNeighbors(new Star(targetPoint), kOrRadius + 1);
          } else if (command.equals("radius")) {
            stars = tree.radiusSearch(new Star(targetPoint), kOrRadius);
          }
          // we want to find neighbors of this star, not itself so remove
          stars.remove(star);
          return starListString(stars);
        } else {
          return "ERROR: " + command + ": unknown star proper id: " + starName;
        }
      } else {
        // Search by coordinates
        String[] stringCoords = Arrays.copyOfRange(args, 2, args.length);
        double[] targetPoint = parseCoordinates(stringCoords);

        List<Star> stars = new LinkedList<>();
        if (command.equals("neighbors")) {
          // get nearest neighbors
          stars = tree.nearestNeighbors(new Star(targetPoint), kOrRadius);
        } else {
          // get neighbors within radius
          stars = tree.radiusSearch(new Star(targetPoint), kOrRadius);
        }
        return starListString(stars);
      }
    } catch (NumberFormatException n) {
      return "ERROR: " + command + ": error parsing " + letter + ", "
          + usageMessage;
    }
  }

  /**
   * Makes a printable string from a list of stars.
   * @param stars a list of stars
   * @return a printable string of newline separated star ids
   */
  private String starListString(List<Star> stars) {
    if (stars.isEmpty()) {
      return null;
    }
    String str = "";
    for (Star s: stars) {
      str += s.getStarId() + "\n";
    }
    // trim off newline at end
    if (str.length() > 0) {
      str = str.substring(0, str.length() - 1);
    }
    return str;
  }

  /**
   * Command class for "stars" command.
   */
  public class StarsCommand implements Executable {
    /**
     * Handles parsing the provided csv filename by calling csv parser and
     * then parseStars().
     * @param args an array containing user input; args[0] is the command keyword
     *             itself, so in this case args[0] = "stars" or else the repl
     *             would not have invoked StarsCommand.evaluate() and
     *             args[1] should be the filename
     * @return a string to be printed that tells the user whether or not
     * parsing the csv file was successful
     */
    @Override
    public String execute(String[] args) {
      if (args.length == 2) {
        List<String[]> rows = CSVParser.parseFile(args[1]);
        // if rows is null, there was an error
        if (rows == null) {
          return "ERROR: could not read file " + args[1];
        }
        // further parse data into star array
        Star[] stars = parseStars(rows);
        // overwrite tree
        tree = new KDTree<Star>(stars, 3);
        return "Read " + stars.length + " stars from " + args[1];
      } else {
        return "ERROR: Stars: incorrect arguments. Usage: stars <filename>";
      }
    }

    /**
     * Parses a list of string arrays into stars by calling parseRow()
     * on each array.
     * @param rows a list of string arrays with star data
     * @return an array of Star objects storing the data from the csv file
     */
    private Star[] parseStars(List<String[]> rows) {
      if (rows.isEmpty()) {
        return new Star[0];
      }
      String[] header = rows.get(0);
      rows.remove(0);
      Star[] stars = new Star[rows.size()];
      int r = 0;
      for (String[] row: rows) {
        try {
          stars[r] = parseRow(row);
          r++;
        } catch (Exception e) {
          System.out.println("Error reading line " + (r + 2));
        }
      }
      return stars;
    }

    /**
     * Method that reads in a string array row and parses info into a new star.
     * @param row a string array containing star info
     * @return the star object containing the same info
     */
    private Star parseRow(String[] row) {
      // get current star information
      int starId = Integer.parseInt(row[0]);
      String properName = row[1];
      double[] coords = new double[3];
      coords[0] = Double.parseDouble(row[2]);
      coords[1] = Double.parseDouble(row[3]);
      coords[2] = Double.parseDouble(row[4]);
      // instantiate new star object filled with star info
      Star s = new Star(starId, properName, coords);
      // add star to star name map
      starNameMap.put(properName, s);
      return s;
    }
  }

  /**
   * Command class for "neighbors" command.
   */
  public class NeighborsCommand implements Executable {

    /**
     * Evaluates a "neighbors" command (ex: neighbors 5 0 0 0 or neighbors 5 "Sol")
     * by passing input to shared functionality function.
     * @param args user input array
     * @return a string to be displayed with the StarIds of the k nearest
     * neighbor stars to a certain point or star
     */
    @Override
    public String execute(String[] args) {
      return starsEvaluate(args);
    }
  }

  /**
   * Command class for "radius" command.
   */
  public class RadiusCommand implements Executable {
    /**
     * Evaluates user input by passing input to shared functionality function.
     * @param args user input array
     * @return a string to be displayed with the StarIds of all the stars
     * within a certain radius of a certain point or star
     */
    @Override
    public String execute(String[] args) {
      return starsEvaluate(args);
    }
  }
}
