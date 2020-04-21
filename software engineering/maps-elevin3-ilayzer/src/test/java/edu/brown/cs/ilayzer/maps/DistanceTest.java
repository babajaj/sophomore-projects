package edu.brown.cs.ilayzer.maps;

import edu.brown.cs.ilayzer.distance.Axis;
import edu.brown.cs.ilayzer.distance.Euclidean;
import edu.brown.cs.ilayzer.distance.Haversine;
import edu.brown.cs.ilayzer.maps.world.WorldEdge;
import edu.brown.cs.ilayzer.maps.world.WorldNode;
import edu.brown.cs.ilayzer.stars.Cartesian;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Class for testing distance measurements.
 */
public class DistanceTest {

  private final static double DELTA = 0.1; // delta for assertEquals double

  /**
   * Tests euclidean distance measurement.
   */
  @Test
  public void testEuclidean() {
    Euclidean<Cartesian> euclidean = new Euclidean<>();
    Cartesian p0 = new Cartesian(new double[]{0.0, 0.0, 0.0});
    Cartesian p1 = new Cartesian(new double[]{0.0, 1.0, 0.0});
    Cartesian p2 = new Cartesian(new double[]{0.0, 4.0, 3.0});
    assertEquals(1.0, euclidean.distance(p0, p1), DELTA);
    assertEquals(5.0, euclidean.distance(p0, p2), DELTA);
  }

  /**
   * Tests axis distance measurement.
   */
  @Test
  public void testAxis() {
    Axis<Cartesian> axis = new Axis<>(0);
    Cartesian p0 = new Cartesian(new double[]{0.0, 0.0, 0.0});
    Cartesian p1 = new Cartesian(new double[]{0.0, 1.0, 0.0});
    Cartesian p2 = new Cartesian(new double[]{0.0, 4.0, 3.0});
    assertEquals(0.0, axis.distance(p0, p1), DELTA);
    axis.setAxis(1);
    assertEquals(1.0, axis.distance(p0, p1), DELTA);
    assertEquals(3.0, axis.distance(p1, p2), DELTA);
    axis.setAxis(2);
    assertEquals(3.0, axis.distance(p1, p2), DELTA);
  }

  /**
   * Tests haversine distance measurement.
   */
  @Test
  public void testHaversine() {
    Haversine<Cartesian> hav = new Haversine<>(6371.0);
    Cartesian p0 = new Cartesian(new double[]{0.0, 0.0});
    Cartesian p1 = new Cartesian(new double[]{0.5, 1.0});
    Cartesian p2 = new Cartesian(new double[]{0.1, 4.0});
    assertEquals(124.3, hav.distance(p0, p1), DELTA);
    assertEquals(444.9, hav.distance(p0, p2), DELTA);
    assertEquals(336.5, hav.distance(p1, p2), DELTA);
  }
}
