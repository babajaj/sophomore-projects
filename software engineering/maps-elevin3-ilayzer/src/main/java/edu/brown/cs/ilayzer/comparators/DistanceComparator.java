package edu.brown.cs.ilayzer.comparators;

import edu.brown.cs.ilayzer.distance.Coordinates;
import edu.brown.cs.ilayzer.distance.Distance;

import java.util.Comparator;

/**
 * A comparator that compares two Cartesian objects by how far they are from a point.
 *
 * @param <C> the type of coordinates
 */
public class DistanceComparator<C extends Coordinates> implements Comparator<C> {

  private C targetPoint; // the target point
  private Distance<C> metric;

  /**
   * Constructor for a distance comparator.
   * @param targetPoint the target point from which to measure distance
   * @param metric the distance metric to use
   */
  public DistanceComparator(C targetPoint, Distance<C> metric) {
    this.targetPoint = targetPoint;
    this.metric = metric;
  }

  /**
   * Compares the two Cartesian points by distance from targetPoint.
   * @param c1 a coordinates object
   * @param c2 a second coordinates object
   * @return the result of comparing the euclidean distances between c1's
   * coordinates and the target point and c2's coordinates and the target point
   */
  @Override
  public int compare(C c1, C c2) {
    return Double.compare(metric.distance(targetPoint, c1),
        metric.distance(targetPoint, c2));
  }
}
