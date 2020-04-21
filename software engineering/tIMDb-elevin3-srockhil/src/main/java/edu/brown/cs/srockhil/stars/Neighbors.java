package edu.brown.cs.srockhil.stars;

import edu.brown.cs.srockhil.comparator.DistanceComparator;
import edu.brown.cs.srockhil.KDTree.Coordinate;
import edu.brown.cs.srockhil.KDTree.TreeNode;
import edu.brown.cs.srockhil.executable.Executable;

import java.util.ArrayList;

/**
 * Object contains methods that find the specified
 * number of nearest stars to a given point in 3d space,
 * using a KD tree.
 */
public class Neighbors implements Executable {


  private ArrayList<Star> closest;
  private StarsManager manager;
  private DistanceComparator<Star> comparator;
  private Double[] point;
  private Star targetStar;

  /**
   * The constructor sets up a global variable representing the point to be searched around.
   * It stores a StarsManager which contains helper methods and a KD tree.
   *
   * @param manager The StarsManager object
   */
  public Neighbors(StarsManager manager) {
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
    //k represents number of neighbors to search for
    int k = 0;
    try {
      //k must be an integer
      k = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      manager.printError("ERROR: neighbors must be an integer");
      return null;

    }
    //and it must be nonnegative
    if (k < 0) {
      manager.printError("ERROR: number of neighbors must be non-negative");
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
      //if we are searching for a given star, instead of changing the algorithm so the final
      //list does not include this star, I simply make sure to search for an additional neighbor
      //and remove the target star at the end.
      k++;
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

    closest = new ArrayList<>();
    //Distance comparator will be used to sort list of stars
    comparator = new DistanceComparator<>(point);
    //recursive search starting with the root
    this.neighbors(manager.getRoot(), k);
    showNeighbors();
    return null;
  }

  //calculates the euclidean (x + y + z) distance from a star to the target point
  private double euclideanDistance(Coordinate star) {
    double total = 0.0;
    for (int i = 0; i < 3; i++) {
      total += Math.abs(star.getVal(i) - point[i]);
    }
    return total;
  }

  //calculates the distance on one specified axis from a star to the target point
  private double relAxDistance(Coordinate star, int relAx) {
    return Math.abs(star.getVal(relAx) - point[relAx]);
  }

  /**
   * This is the recursive function that searches the KD tree.
   *
   * @param node Current node that will be considered for a nearest neighbor position
   * @param k    The number of neighbors we are searching for
   */
  public void neighbors(TreeNode<Star> node, int k) {
    if (k == 0) {
      return;
    }
    //find the relevant axis
    int relAx = node.getDepth() % 3;
    //find distance of node from target point
    double distance = manager.distance(node.getElement(), point);
    TreeNode<Star> lchild = node.getLchild();
    TreeNode<Star> rchild = node.getRchild();

    //if the neighbors list is not full, add the star and recur on both children.
    if (closest.size() < k) {
      closest.add(node.getElement());
      closest.sort(comparator);
      if (lchild != null) {
        neighbors(lchild, k);
      }
      if (rchild != null) {
        neighbors(rchild, k);
      }
      return;
    }

    //if the list is full and the star is closer than a star on the neighbors list.
    if (distance < manager.distance(closest.get(k - 1), point)) {
      closest.remove(k - 1);
      closest.add(node.getElement());
      closest.sort(comparator);
    }

    //if the furthest star on the list's euclidean distance from target point is greater
    // than the relevant axis distance from current node to target point:
    // recur on both children
    if (euclideanDistance(closest.get(k - 1)) > relAxDistance(node.getElement(), relAx)) {
      if (lchild != null) {
        neighbors(lchild, k);
      }
      if (rchild != null) {
        neighbors(rchild, k);
      }
    } else {
      //otherwise we only recur on one child.
      if (node.getElement().getVal(relAx) < point[relAx]) {
        //recur on the right child if the nodes relevant axis value is less than the
        //target points relevant axis value
        if (rchild != null) {
          neighbors(rchild, k);
        }
      } else {
        //otherwise recur on left child
        if (lchild != null) {
          neighbors(lchild, k);
        }
      }
    }
  }

  /**
   * This method either prints the neighbors list to the terminal or adds it to
   * the manager's result string to be outputted to the GUI.
   */
  public void showNeighbors() {
    if (targetStar != null) {
      //if the user was searching around a target star, it should be removed from the list.
      //since in this case we searched for k+1 neighbors, the resulting list has size k.
      closest.remove(targetStar);
    }
    for (Star star : closest) {
      manager.addStarToResult(star, manager.distance(star, point));
    }
  }


}
