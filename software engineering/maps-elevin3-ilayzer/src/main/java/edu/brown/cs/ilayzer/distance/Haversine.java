package edu.brown.cs.ilayzer.distance;

/**
 * Class for calculating Haversine distance.
 * @param <C> an object extending Coordinates
 */
public class Haversine<C extends Coordinates> implements Distance<C> {

  private double radius; // the radius of the sphere

  /**
   * Constructor for a Haversine distance calculator.
   * @param radius the radius of the sphere on which distance is measured.
   */
  public Haversine(double radius) {
    this.radius = radius;
  }

  @Override
  public double distance(C c1, C c2) {
    double lat1 = c1.getCoordinates()[0];
    double lat2 = c2.getCoordinates()[0];
    double lon1 = c1.getCoordinates()[1];
    double lon2 = c2.getCoordinates()[1];

    double phi1 = Math.toRadians(lat1);
    double phi2 = Math.toRadians(lat2);
    double deltaPhi = Math.toRadians(lat2 - lat1);
    double deltaLambda = Math.toRadians(lon2 - lon1);

    double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
        + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return radius * c;
  }
}
