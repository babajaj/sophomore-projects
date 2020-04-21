package edu.brown.cs.srockhil.KDTree;

/**
 * An object that can store an element and has pointers to left and right children.
 *
 * @param <T> is any object.
 */
public class TreeNode<T> {

  private TreeNode<T> rchild;
  private TreeNode<T> lchild;
  private Integer depth;
  private T element;

  /**
   * Sets up the nodes element field upon instantiation.
   *
   * @param element is the object you would like the node to store.
   */
  public TreeNode(T element) {
    this.element = element;
  }


  /**
   * getter for element.
   *
   * @return element of type t.
   */
  public T getElement() {
    return element;
  }

  /**
   * setter for depth.
   *
   * @param depth integer depth.
   */
  public void setDepth(int depth) {
    this.depth = depth;
  }

  /**
   * getter for depth.
   *
   * @return integer depth.
   */
  public int getDepth() {
    return depth;
  }

  /**
   * setter for right child.
   *
   * @param rchild node to be rchild.
   */
  public void setRchild(TreeNode<T> rchild) {
    this.rchild = rchild;
  }

  /**
   * getter for right child.
   *
   * @return node rchild.
   */
  public TreeNode<T> getRchild() {
    return rchild;
  }

  /**
   * setter for left child.
   *
   * @param lchild node to be lchild.
   */
  public void setLchild(TreeNode<T> lchild) {
    this.lchild = lchild;
  }

  /**
   * getter for left child.
   *
   * @return node lchild.
   */
  public TreeNode<T> getLchild() {
    return lchild;
  }


}
