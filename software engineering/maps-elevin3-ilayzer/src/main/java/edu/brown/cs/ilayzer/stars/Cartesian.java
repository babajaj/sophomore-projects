package edu.brown.cs.ilayzer.stars;

import java.util.Arrays;

/**
 * A class that just stores basic coordinates (for testing).
 */
public class Cartesian implements edu.brown.cs.ilayzer.distance.Coordinates {

  private double[] coordinates; // the coordinates

  /**
   * Constructor for a Coordinates object.
   * @param coordinates an array for coordinates (1, 2) would be {1, 2}
   */
  public Cartesian(double[] coordinates) {
    this.coordinates = coordinates;
  }

  /**
   * Gets the coordinates array.
   * @return the coordinates
   */
  @Override
  public double[] getCoordinates() {
    return this.coordinates;
  }

  /**
   * Returns a string representation of the coordinates.
   * @return a string representation
   */
  @Override
  public String toString() {
    return "Coordinates{"
        + "coordinates=" + Arrays.toString(coordinates)
        + '}';
  }
}
