package edu.brown.cs.srockhil.comparator;

import edu.brown.cs.srockhil.KDTree.Coordinate;

import java.util.Comparator;

/**
 * This class compares the straight line distance from one coordinate to another.
 * @param <T> The types of object in space with which we are calculating distance.
 */
public class DistanceComparator<T extends Coordinate> implements Comparator<T> {

  private Double[] point;

  /**
   * Stores the point from which distance will be calculated.
   * @param point Array of doubles representing the axis values.
   */
  public DistanceComparator(Double[] point) {
    this.point = point;

  }

  @Override
  public int compare(T o1, T o2) {
    double d1 = calcDistance(o1);
    double d2 = calcDistance(o2);
    if (d1 < d2) {
      return -1;
    } else if (d1 == d2) {
      return 0;
    }
    return 1;
  }

  //calculates pythagorean distance
  private double calcDistance(T o) {
    double total = 0.0;
    for (int i = 0; i < 3; i++) {
      total += Math.pow((Math.abs(o.getVal(i) - point[i])), 2);
    }
    return Math.sqrt(total);

  }
}
