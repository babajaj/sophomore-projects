package edu.brown.cs.ilayzer.distance;

/**
 * A class for measuring axis distance.
 * @param <C> the type of coordinates
 */
public class Axis<C extends Coordinates> implements Distance<C> {
  private int axis; // the axis along which to measure distance

  /**
   * Constructor for axis distance class.
   * @param axis the axis along which to measure distance
   */
  public Axis(int axis) {
    this.axis = axis;
  }

  /**
   * Changes the axis along which to measure.
   * @param axis the new axis
   */
  public void setAxis(int axis) {
    this.axis = axis;
  }

  @Override
  public double distance(C c1, C c2) {
    return Math.abs(c1.getCoordinates()[axis] - c2.getCoordinates()[axis]);
  }
}
