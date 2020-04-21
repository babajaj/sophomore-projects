package edu.brown.cs.ilayzer.distance;

/**
 * An interface for classes that calculate distance between coordinate objects.
 * @param <C> the type of coordinates.
 */
public interface Distance<C extends Coordinates> {
  /**
   * Calculates some distance between two objects implementing the coordinates.
   * @param c1 coordinate object 1
   * @param c2 coord
   * @return the distance between c1 and c2
   */
  double distance(C c1, C c2);
}
