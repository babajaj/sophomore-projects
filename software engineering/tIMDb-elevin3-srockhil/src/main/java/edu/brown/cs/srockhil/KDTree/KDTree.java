package edu.brown.cs.srockhil.KDTree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Data structure that holds nodes in a balanced binary tree based on specified dimensions.
 *
 * @param <T> Type of object that nodes will store. Must have coordinate values.
 * @param <C> Type of comparator that will be used to sort nodes
 */
public class KDTree<T extends Coordinate, C extends Comparator<TreeNode<T>>> {

  private int dimensions;
  private TreeNode<T> root;
  private boolean valid;
  private ArrayList<TreeNode<T>> nodelist;
  private ArrayList<C> comparators;
  private HashMap<String, TreeNode<T>> nodeNames;

  /**
   * Constructor for KD tree.
   *
   * @param dimensions  Number of dimensions kd tree will account for.
   * @param comparators Hashmap of comparators (there is one comparator for each dimension)
   */
  public KDTree(int dimensions, ArrayList<C> comparators) {
    this.dimensions = dimensions;
    this.comparators = comparators;
    // tree starts out as invalid as it has not been set up yet
    valid = false;
    // storage for nodes and their names
    nodelist = new ArrayList<>();
    nodeNames = new HashMap<>();
    root = null;
  }

  /**
   * When called, this function accepts an element and creates a new node to hold it.
   * If it has a name, the node is stored in a hashmap with name as key.
   *
   * @param nodeElement Element to be stored in a new node.
   */
  public void acceptData(T nodeElement) {
    TreeNode<T> node = new TreeNode<>(nodeElement);
    nodelist.add(node);
    String name = nodeElement.getName();
    if (!name.equals("")) {
      nodeNames.put(name, node);
    }
  }

  /**
   * Helper method that calls the recursive build tree function with the
   * correct initial parameters.
   */
  public void buildTree() {
    buildTreeRecursive(nodelist, 0, null, '0');
    valid = true;
  }

  /**
   * Recursively builds a balanced KD tree.
   * Sorts nodes by coordinate value (according to depth) and chooses median
   * node to become the left or right child of the given parent.
   *
   * @param list   List of nodes from which one node will be added to the tree.
   * @param depth  Integer depth which informs the dimension for the nodes to be sorted on.
   * @param parent Parent node who's left or right child field will be set.
   * @param side   Informs the decision to make the current node a left or right child.
   */
  public void buildTreeRecursive(List<TreeNode<T>> list, int depth, TreeNode<T> parent, char side) {
    if (list.size() == 0) {
      return;
    }
    //sorts list in ascending order using comparator that compares using the specified dimension.
    list.sort(comparators.get(depth % dimensions));
    int median = (list.size()) / 2;
    TreeNode<T> node = list.get(median);
    node.setDepth(depth);
    //set the root if their is none
    if (parent == null) {
      root = node;
    } else {
      //if the node values for the given dimension are less than the parent
      if (side == 'L') {
        parent.setLchild(node);
        //if the node values for the given dimension are greater than the parent
      } else if (side == 'R') {
        parent.setRchild(node);
      }
    }
    //create new sublist that represent nodes less than and greater that the current node
    // for the given dimension
    List<TreeNode<T>> leftList = list.subList(0, median);
    List<TreeNode<T>> rightList = list.subList(median + 1, list.size());

    this.buildTreeRecursive(leftList, depth + 1, node, 'L');
    this.buildTreeRecursive(rightList, depth + 1, node, 'R');
  }

  /**
   * Hashtable look up for node using name.
   *
   * @param name Name that is a key that maps to a node.
   * @return The node that was the value mapped to the name key.
   */
  public TreeNode<T> getNodefromName(String name) {
    return nodeNames.get(name);
  }

  /**
   * Getter method for root Node.
   *
   * @return The root of the tree.
   */
  public TreeNode<T> getRoot() {
    return root;
  }

  /**
   * Lets caller know if kd tree is a complete, valid tree.
   *
   * @return Boolean, true if valid false if not.
   */
  public Boolean isValid() {
    return valid;
  }


}
