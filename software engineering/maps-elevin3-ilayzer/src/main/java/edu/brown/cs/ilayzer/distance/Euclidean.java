package edu.brown.cs.ilayzer.distance;

/**
 * Class for calculating the euclidean distance between two coordinates objects.
 * @param <C> a coordinates object.
 */
public class Euclidean<C extends Coordinates> implements Distance<C> {
  @Override
  public double distance(C c1, C c2) {
    double[] pointA = c1.getCoordinates();
    double[] pointB = c2.getCoordinates();
    // make sure points are of the same dimension
    if (pointA.length != pointB.length) {
      throw new IllegalArgumentException("euclideanDistance: Points must be of same dimension.");
    }
    double sumOfSquares = 0.0;
    // loop through finding distance between A and B in each dimension
    for (int dim = 0; dim < pointA.length; dim++) {
      double distance = Math.abs(pointA[dim] - pointB[dim]);
      sumOfSquares += distance * distance;
    }
    return Math.sqrt(sumOfSquares);
  }
}
