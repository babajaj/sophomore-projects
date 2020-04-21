package edu.brown.cs.ilayzer.distance;

/**
 * Interface for classes that can be used in a kd tree because they must
 * have a coordinates double array.
 */
public interface Coordinates {
  /**
   *
   * Gets the cartesian coordinates array.
   * @return the cartesian coordinates of the object
   */
  double[] getCoordinates();
}
