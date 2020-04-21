package edu.brown.cs.ilayzer.maps;

import edu.brown.cs.ilayzer.maps.world.WorldDatabase;
import edu.brown.cs.ilayzer.maps.world.WorldNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorldDatabaseTest {

  private WorldDatabase db;

  @Before
  public void setUp() throws SQLException, ClassNotFoundException {
    db = new WorldDatabase("data/maps/smallMaps.sqlite3");
  }

  @Test
  public void testGetAllTraversableNodes() throws SQLException, ClassNotFoundException {
    // no traversable nodes
    db = new WorldDatabase("data/maps/noTraversable.sqlite3");
    assertEquals(0, db.getAllTraversableNodes().size());
    // only some nodes are traversable (/n/0 and /n/1 are traversable, /n/2 not)
    db = new WorldDatabase("data/maps/someTraversable.sqlite3");
    List<String> expectedNodeIds = new ArrayList<String>();
    expectedNodeIds.add("/n/0");
    expectedNodeIds.add("/n/1");
    List<WorldNode> nodes = db.getAllTraversableNodes();
    for (WorldNode node: nodes) {
      assertTrue(expectedNodeIds.contains(node.getId()));
    }
    // all nodes are traversable
    db = new WorldDatabase(("data/maps/smallMaps.sqlite3"));
    expectedNodeIds = new ArrayList<String>();
    expectedNodeIds.add("/n/0");
    expectedNodeIds.add("/n/1");
    expectedNodeIds.add("/n/2");
    expectedNodeIds.add("/n/3");
    expectedNodeIds.add("/n/4");
    expectedNodeIds.add("/n/5");
    nodes = db.getAllTraversableNodes();
    for (WorldNode node: nodes) {
      assertTrue(expectedNodeIds.contains(node.getId()));
    }
  }

  @Test
  public void testGetWayIdsWithinBoundingBox() throws SQLException {
    // no ways in box
    assertEquals(0, db.getWayIdsWithinBoundingBox(50, -50, 40, -45).size());
    // some ways in box
    List<String> expectedWays = new ArrayList<String>();
    expectedWays.add("/w/2");
    expectedWays.add("/w/0");
    List<String> ways = db.getWayIdsWithinBoundingBox(41.8201, -71.4001, 41.1999, -71.3999);
    assertTrue(expectedWays.containsAll(ways));
    assertTrue(ways.containsAll(expectedWays));
    // all ways in db in box
    expectedWays = new ArrayList<String>();
    expectedWays.add("/w/0");
    expectedWays.add("/w/1");
    expectedWays.add("/w/2");
    expectedWays.add("/w/3");
    expectedWays.add("/w/4");
    expectedWays.add("/w/5");
    expectedWays.add("/w/6");
    ways = db.getWayIdsWithinBoundingBox(41.83, -71.41, 41.79, -71.39);
    assertTrue(expectedWays.containsAll(ways));
    assertTrue(ways.containsAll(expectedWays));
  }

  @Test
  public void testValidCrossStreets() throws SQLException {
    // not a valid intersection
    assertNull(db.validCrossStreets("Radish Spirit Blvd", "Sootball Ln"));
    // valid intersection
    assertEquals("/n/1", db.validCrossStreets("Chihiro Ave", "Sootball Ln"));
  }

  @Test
  public void testFindNode() throws SQLException {
    // no node for given id
    WorldNode n = db.findNode("nonexistent");
    // node exists and was not cached
    WorldNode n1 = new WorldNode("/n/1", true, new double[]{41.8203, -71.4});
    assertEquals(n1, db.findNode("/n/1"));
    // node exists and was cached
    assertEquals(n1, db.findNode("/n/1"));
  }

  @Test
  public void testWaysFromNode() throws SQLException {
    // no ways from node
    WorldNode n5 = new WorldNode("/n/5", true, new double[]{41.8206, -71.4003});
    assertEquals(0, db.waysFromNode(n5).size());
    // single way from node
    WorldNode n2 = new WorldNode("/n/2", true, new double[]{41.8206, -71.4});
    assertEquals("/w/4", db.waysFromNode(n2).get(0).getID());
    // multiple ways from node
    WorldNode n0 = new WorldNode("/n/0", true, new double[]{41.82, -71.4});
    assertEquals("/w/0", db.waysFromNode(n0).get(0).getID());
    assertEquals("/w/2", db.waysFromNode(n0).get(1).getID());
  }

  @After
  public void tearDown() {
    db = null;
  }

}
