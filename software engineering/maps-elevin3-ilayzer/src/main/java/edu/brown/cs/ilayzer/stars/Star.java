package edu.brown.cs.ilayzer.stars;

import edu.brown.cs.ilayzer.distance.Coordinates;

import java.util.Arrays;

/**
 * Class for Stars that implements Cartesian interface to be used with KD-Trees.
 */
public class Star implements Coordinates {

  private int starId;
  private String properName;
  private double[] coordinates;

  /**
   * Constructor for a star.
   * @param starId the star id of the new star, ex: 0
   * @param properName the proper id of the new star, ex: "Sol"
   * @param coordinates the coordinates of the new star ex: {0, 0, 0}
   */
  public Star(int starId, String properName, double[] coordinates) {
    this.coordinates = coordinates;
    this.starId = starId;
    this.properName = properName;
  }

  /**
   * Constructor for wrapping coordinates in space (not a star).
   * @param coordinates the coordinates to be wrapped
   */
  public Star(double[] coordinates) {
    this.coordinates = coordinates;
  }

  /**
   * Gets the star id of the star.
   * @return the star id
   */
  public int getStarId() {
    return starId;
  }

  /**
   * Gets the proper name of the star.
   * @return the star proper name
   */
  public String getProperName() {
    return properName;
  }

  /**
   * Getter for coordinates.
   * @return the coordinates
   */
  @Override
  public double[] getCoordinates() {
    return coordinates;
  }

  /**
   * toString method.
   * @return a string representation of a star
   */
  @Override
  public String toString() {
    return "Star{"
        + "starId=" + starId
        + ", properId='" + properName + '\''
        + ", coordinates=" + Arrays.toString(coordinates)
        + '}';
  }
}
