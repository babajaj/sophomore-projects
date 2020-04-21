package edu.brown.cs.srockhil.stars;

import edu.brown.cs.srockhil.comparator.DistanceComparator;
import edu.brown.cs.srockhil.KDTree.TreeNode;
import edu.brown.cs.srockhil.executable.Executable;

import java.util.ArrayList;

/**
 * Object contains methods that find all the stars within
 * a certain radius of a given point, using a KD tree.
 */
public class Radius implements Executable {

  private StarsManager manager;
  private ArrayList<Star> withinR;
  private DistanceComparator<Star> comparator;
  private Double[] point;
  private Star targetStar;

  /**
   * The constructor sets up a global variable representing the point to be searched around.
   * It stores a StarsManager which contains helper methods and a KD tree.
   *
   * @param manager The StarsManager object
   */
  public Radius(StarsManager manager) {
    this.manager = manager;
    point = new Double[3];

  }

  @Override
  public String execute(String[] args) {
    //result string is the string outputted to the GUI
    manager.clearResult();
    //first check if the tree is valid.
    if (!manager.validTree()) {
      //this "printError" method sends this string to the manager to either be
      //outputted to the GUI or printed to the terminal.
      manager.printError("ERROR: stars not loaded. Please use command Stars <filename> to "
              + "load stars");
      return null;
    }
    //manager makes sure arguments are valid
    args = manager.parseArgs(args);
    //manager returns null if they are invalid
    if (args == null) {
      return null;
    }
    double r = 0.0;
    try {
      //check if radius string is parsable as a double
      r = Double.parseDouble(args[1]);
    } catch (NumberFormatException e) {
      manager.printError("ERROR: radius must be a parsable double");
      return null;
    }
    //radius must be nonnegative
    if (r < 0) {
      manager.printError("ERROR: radius be non-negative");
      return null;
    }

    // if the user is searching for neighbors around a given coordinate
    if (args.length == 5) {
      //there is no target star, only a target point
      targetStar = null;
      //manager checks validity of coordinates
      //value of point is set (used later)
      point = manager.parseCoords(args);
      if (point == null) {
        return null;
      }
    }

    //if the user is searching for neighbors around a given star.
    if (args.length == 3) {
      //manager retrieves the star object using the name.
      targetStar = manager.getStar(args[2]);
      if (targetStar == null) {
        return null;
      }
      //extraction of target star coordinates.
      for (int i = 0; i < 3; i++) {
        point[i] = targetStar.getVal(i);
      }
    }

    withinR = new ArrayList<>();
    comparator = new DistanceComparator<>(point);
    this.radius(manager.getRoot(), r);
    showRadius();
    return null;
  }

  /**
   * This is the recursive function that searches the KD tree.
   *
   * @param node Current node that will be considered for a within radius position.
   * @param r    The radius we are searching within.
   */
  public void radius(TreeNode<Star> node, double r) {
    // if radius is 0, we are searching for a single star
    if (r == 0 && !withinR.isEmpty()) {
      return;
    }
    int relAx = node.getDepth() % 3;
    double dist = manager.distance(node.getElement(), point);
    double relAxDist = relAxDistance(node.getElement(), relAx);
    TreeNode<Star> lchild = node.getLchild();
    TreeNode<Star> rchild = node.getRchild();
    //if the distance from the star to the target point is within radius
    if (dist <= r) {
      withinR.add(node.getElement());
      //recur on both children
      if (lchild != null) {
        radius(lchild, r);
      }
      if (rchild != null) {
        radius(rchild, r);
      }
      //if it is not within the radius:
      //if the relevant axis distance is less than or equal to R:
    } else if (relAxDist <= r) {
      //then recur on both children
      if (lchild != null) {
        radius(lchild, r);
      }
      if (rchild != null) {
        radius(rchild, r);
      }
      //otherwise we recur on only one child
    } else if (relAxDist > r) {
      // if the relevant axis value is less than the value for the target point recur on right child
      if (node.getElement().getVal(relAx) < point[relAx]) {
        if (rchild != null) {
          radius(rchild, r);
        }
        //otherwise recur on left
      } else {
        if (lchild != null) {
          radius(lchild, r);
        }
      }
    }
  }

  //calculates the distance on one specified axis from a star to the target point
  private double relAxDistance(Star star, int relAx) {
    return Math.abs(star.getVal(relAx) - point[relAx]);
  }


  /**
   * This method either prints the neighbors list to the terminal or adds it to
   * the manager's result string to be outputted to the GUI.
   */
  public void showRadius() {
    if (targetStar != null) {
      //if the user was searching around a target star, it should be removed from the list.
      withinR.remove(targetStar);
    }
    withinR.sort(comparator);
    for (Star star : withinR) {
      manager.addStarToResult(star, manager.distance(star, point));
    }
  }


}
