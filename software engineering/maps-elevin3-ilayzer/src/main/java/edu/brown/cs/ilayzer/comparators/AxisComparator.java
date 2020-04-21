package edu.brown.cs.ilayzer.comparators;

import edu.brown.cs.ilayzer.distance.Coordinates;

import java.util.Comparator;

/**
 * comparator that compares cartesian objects by comparing coordinate along
 * a specific axis.
 */
public class AxisComparator implements Comparator<Coordinates> {
  private int compareAxis; // the axis along which to compare coordinates

  /**
   * constructor for an axis comparator.
   * @param compareAxis the axis along which to compare coordinates
   */
  public AxisComparator(int compareAxis) {
    this.compareAxis = compareAxis;
  }

  /**
   * compares the coordinate along compareAxis of the two Cartesian objects.
   * @param c1 a cartesian
   * @param c2 a second cartesian
   * @return whether c1 is larger than c2
   */
  @Override
  public int compare(Coordinates c1, Coordinates c2) {
    return Double.compare(c1.getCoordinates()[compareAxis],
        c2.getCoordinates()[compareAxis]);
  }
}
