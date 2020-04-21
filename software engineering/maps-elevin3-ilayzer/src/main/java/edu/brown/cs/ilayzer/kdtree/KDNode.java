package edu.brown.cs.ilayzer.kdtree;

import edu.brown.cs.ilayzer.distance.Coordinates;

/**
 * Class for a node in a KD Tree.
 * @param <T> a type that implements the Cartesian interface
 */
public class KDNode<T extends Coordinates> {
  private KDNode<T> left;
  private KDNode<T> right;
  private T value; // the Cartesian object (ex: a Star object)

  /**
   * Constructor.
   * @param val the Cartesian object (ex: a Star object)
   */
  public KDNode(T val) {
    this.value = val;
  }

  /**
   * Gets left subtree (node).
   * @return the left node
   */
  public KDNode<T> getLeft() {
    return left;
  }

  /**
   * Sets the right subtree to a node.
   * @param left the node to set as the right subtree
   */
  public void setLeft(KDNode<T> left) {
    this.left = left;
  }

  /**
   * Gets right subtree (node).
   * @return the right node
   */
  public KDNode<T> getRight() {
    return right;
  }

  /**
   * Sets the right subtree to a node.
   * @param right the node to set as the right subtree
   */
  public void setRight(KDNode<T> right) {
    this.right = right;
  }

  /**
   * Gets the object of type T that implements Cartesian attached to the node.
   * @return the object 'value' attached to the node.
   */
  public T getValue() {
    return value;
  }

  /**
   * Sets the value of the node to a Cartesian object of type T.
   * @param value an object of type T that implements Cartesian
   */
  public void setValue(T value) {
    this.value = value;
  }

  /**
   * To string method that captures entire tree.
   * @return the string representation of the node and any subtrees
   */
  @Override
  public String toString() {
    String leftString = "null";
    if (left != null) {
      leftString = left.toString();
    }
    String rightString = "null";
    if (right != null) {
      rightString = right.toString();
    }
    return "KDNode{"
        + ", point=" + value.toString()
        + "left=" + leftString
        + ", right=" + rightString
        + '}';
  }
}
