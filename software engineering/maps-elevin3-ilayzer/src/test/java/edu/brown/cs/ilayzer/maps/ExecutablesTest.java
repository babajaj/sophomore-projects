package edu.brown.cs.ilayzer.maps;

import edu.brown.cs.ilayzer.maps.executables.MapExecutable;
import edu.brown.cs.ilayzer.maps.executables.Nearest;
import edu.brown.cs.ilayzer.maps.executables.Route;
import edu.brown.cs.ilayzer.maps.executables.Ways;
import edu.brown.cs.ilayzer.maps.world.World;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import static org.junit.Assert.assertEquals;

/**
 * Class for unit testing executables. This is mostly for code coverage because
 * this is basically equivalent to doing system tests as we are testing
 * inputs and outputs to for a REPL.
 */
public class ExecutablesTest {

  private World world;
  private MapExecutable map;
  private Nearest nearest;
  private Ways ways;
  private Route route;

  /**
   * Sets up.
   */
  @Before
  public void setUp() throws SQLException, ClassNotFoundException {
    // set up world and executables
    world = new World(6731);
    map = new MapExecutable(world);
    nearest = new Nearest(world);
    ways = new Ways(world);
    route = new Route(world);
    map.execute(new String[] {"map", "data/maps/smallMaps.sqlite3"});
  }

  /**
   * Tears down.
   */
  @After
  public void tearDown() {
    nearest = null;
    world = null;
  }

  @Test
  public void testRoute() {
    assertEquals("/n/0 -> /n/1 : /w/0\n/n/1 -> /n/2 : /w/1\n/n/2 -> /n/5 : /w/4",
        route.execute(new String[]{"route", "41.8", "-71.3", "42", "-72"}));
  }

  @Test
  public void testWays() {
    assertEquals("/w/0\n/w/1\n/w/2\n/w/3\n/w/4\n/w/5\n/w/6",
        ways.execute(new String[]{"ways", "41.85", "-71.41", "41.81", "-70"}));
  }

  /**
   ** Tests making a call to Nearest.
   */
  @Test
  public void testNearest() {
    assertEquals("/n/0", nearest.execute(new String[]{"nearest", "41.82", "-71.4"}));
  }

}
