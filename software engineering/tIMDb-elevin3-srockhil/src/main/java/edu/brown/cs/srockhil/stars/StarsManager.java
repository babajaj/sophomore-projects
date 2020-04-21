package edu.brown.cs.srockhil.stars;

import edu.brown.cs.srockhil.KDTree.KDTree;
import edu.brown.cs.srockhil.KDTree.TreeNode;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class contains some of the functionality for Radius and Neighbors class.
 * It helps with checking input when those classes' methods are called.
 * It also creates a KD tree objects and gives Radius and Neighbors access to the
 * tree roots. Lastly, it contains a string "result" that stores error messages or
 * lists of stars to be outputted (to GUI) if storeOutput is true.
 */
public class StarsManager {

  private KDTree<Star, Comparator<TreeNode<Star>>> tree;
  private ArrayList<Comparator<TreeNode<Star>>> comparators;
  private boolean storeOutput;
  private String result;

  /**
   * The constructor sets the results variable to an empty string.
   * This results string is updated with whatever string should be given to the GUI as output.
   *
   * @param storeOutput A boolean that tells this object whether to store error messages or print
   *                    them out to the terminal.
   */
  public StarsManager(boolean storeOutput) {
    comparators = new ArrayList<>();
    //there are three comparators, one for each dimension
    //the comparators index in the array corresponds to the dimension that it will compare with
    //ex: comparators.get(1) will compare stars using the y dimension.
    for (int i = 0; i < 3; i++) {
      comparators.add(new StarComparator<>(i));

    }
    this.storeOutput = storeOutput;
    result = "";

  }

  /**
   * Instantiates a new tree using the list of comparators previously created.
   *
   * @return The new KDtree.
   */
  public KDTree<Star, Comparator<TreeNode<Star>>> newTree() {

    this.tree = new KDTree<>(3, comparators);
    return tree;
  }

  /**
   * Getter method for the root of the KD tree.
   *
   * @return The root if there is a valid tree.
   */
  public TreeNode<Star> getRoot() {
    if (tree != null) {
      return tree.getRoot();
    }
    return null;
  }

  /**
   * Tells the caller if there is a valid tree to search.
   *
   * @return Null if there is no tree or if the tree was created with invalid data. Returns
   * true if there is a valid tree to search on.
   */
  public boolean validTree() {
    if (tree == null || !tree.isValid()) {
      return false;
    }
    return true;
  }

  /**
   * Calculates the straight line distance between a star and a point.
   *
   * @param star  Star with x, y and z value.
   * @param point Point with x, y and z value.
   * @return the double distance from the star to the point.
   */
  public Double distance(Star star, Double[] point) {
    Double total = 0.0;
    for (int i = 0; i < 3; i++) {
      total += Math.pow((Math.abs(star.getVal(i) - point[i])), 2);
    }
    total = Math.sqrt(total);
    return total;
  }

  /**
   * Makes sure arguments are fit for use by Radius and Neighbors objects.
   * Combines star names into one string, checks number and validity of arguments.
   *
   * @param args Array of strings to be checked.
   * @return A new array of strings with a the star name as one string if
   * necessary, otherwise returns
   * the same array. Returns null if there is an error.
   */
  public String[] parseArgs(String[] args) {
    int numArgs = args.length;
    String[] finalArgs;
    // if the third argument (could be the beginning of a star name) begins with
    //a quotation mark but does not end with a quotation mark, we may need to merge
    // multiple strings into one
    //argument in the array.
    if (numArgs > 2 && args[2].startsWith("\"") && !args[2].endsWith("\"")) {
      //check all following arguments for a closing quotation mark.
      for (int i = 3; i < args.length; i++) {
        //concat the star name with the current string
        args[2] = args[2].concat(" " + args[i]);
        //replace current string spot with null
        args[i] = null;
        //these two strings technically are part of one argument, so decrement argument count.
        numArgs--;
        // if we have found the end quotation mark, we can stop.
        if (args[2].endsWith("\"")) {
          break;
        }
      }
      // now we need to put the resulting string in a new final array the size of numArgs.
      finalArgs = new String[numArgs];
      for (int i = 0; i < args.length; i++) {
        //copy over the initial array without the null spots.
        if (args[i] != null) {
          finalArgs[i] = args[i];
        }
      }
    } else {
      // this is the case where there is no star name, so the array doesnt need to be
      // modified to become final.
      finalArgs = args;
    }
    //if our final array doesnt have three or five arguments, then the arguments are invalid
    if (finalArgs.length != 3 & finalArgs.length != 5) {
      printError("ERROR: incorrect number of arguments");
      return null;
    }
    return finalArgs;
  }

  /**
   * Helps Radius and Neighbors object get the star object specified by the user
   * in the inputed arguments.
   *
   * @param name The star name string inputed by the user
   * @return The star object mapped to by this name string.
   */
  public Star getStar(String name) {
    //if the inputed array is empty or it doesnt start and end with quotes, it is invalid.
    if (!name.startsWith("\"") || !name.endsWith("\"") || name.length() < 3) {
      printError("ERROR: star name must be one or more characters surrounded by quotes");
      return null;
    }
    //get rid of quotation marks before using the KD trees hashtable to look up the node object
    //that contains the desired star object.
    name = name.substring(1, name.length() - 1);
    TreeNode<Star> targetNode = tree.getNodefromName(name);
    // if the star doesnt exist, return null
    if (targetNode == null) {
      printError("ERROR: target star not found");
      return null;
    }
    //otherwise return the star
    return targetNode.getElement();
  }

  /**
   * This method checks if the inputed coordinates stings represent doubles.
   *
   * @param args Argument array inputed by user.
   * @return An array of Double points.
   */
  public Double[] parseCoords(String[] args) {
    Double[] point = new Double[3];
    //there will be three points, loop through them
    for (int i = 0; i < 3; i++) {
      try {
        point[i] = Double.parseDouble(args[i + 2]);
      } catch (NumberFormatException e) {
        //if the strings are not parsable as doubles, they are invalid.
        printError("ERROR: coordinates be numeric");
        return null;
      }
    }
    return point;

  }

  /**
   * This method either prints error messages to the terminal or stores them in a sting
   * for GUI output.
   *
   * @param message The string to be printed or stored.
   */
  public void printError(String message) {
    if (storeOutput) {
      result = message;
    } else {
      System.out.println(message);
    }
  }

  /**
   * This method clears the result string (that is given to the GUI).
   */
  public void clearResult() {
    result = "";
  }

  /**
   * This method prints a string representing a closest star or stores a string
   * in result to be outputted to the GUI.
   *
   * @param star     The star being outputted as a closest star
   * @param distance The stars distance from the target point
   */
  public void addStarToResult(Star star, Double distance) {
    if (storeOutput) {
      result += ("Star ID: " + star.getID() + ",    Name: " + star.getName() + ",   Distance: "
              + distance + " units <br>");
    } else {
      System.out.println(star.getID());
    }
  }

  /**
   * Getter method for result of executable command.
   *
   * @return the result string.
   */
  public String getResult() {
    return result;
  }
}
