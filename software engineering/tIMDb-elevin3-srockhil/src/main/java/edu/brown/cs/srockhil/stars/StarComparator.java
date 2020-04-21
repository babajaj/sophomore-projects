package edu.brown.cs.srockhil.stars;

import edu.brown.cs.srockhil.KDTree.TreeNode;

import java.util.Comparator;

/**
 * This object can compare stars based on a given dimension.
 *
 * @param <T> Type that extends a node that holds a star object.
 */
public class StarComparator<T extends TreeNode<Star>> implements Comparator<T> {

  private int axis;

  /**
   * Constructor stores integer that represents axis on which comparisons will occur.
   *
   * @param axis The integer representing the axis, or dimension.
   */
  public StarComparator(int axis) {
    this.axis = axis;
  }


  @Override
  public int compare(T o1, T o2) {
    return this.compareCoordinate(o1, o2);

  }

  //compares stars based on axis 0,1,or 2 (x, y, or z)
  private int compareCoordinate(TreeNode<Star> o1, TreeNode<Star> o2) {
    if (o1.getElement().getVal(axis) < o2.getElement().getVal(axis)) {
      return -1;
    } else if (o1.getElement().getVal(axis) == o2.getElement().getVal(axis)) {
      return 0;
    }
    return 1;
  }
}
