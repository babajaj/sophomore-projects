package edu.brown.cs.ilayzer.maps;

import edu.brown.cs.ilayzer.algorithms.Dijkstra;
import edu.brown.cs.ilayzer.comparators.AStarComparator;
import edu.brown.cs.ilayzer.comparators.DijkstraComparator;
import edu.brown.cs.ilayzer.distance.Euclidean;

import edu.brown.cs.ilayzer.maps.world.World;
import edu.brown.cs.ilayzer.maps.world.WorldEdge;
import edu.brown.cs.ilayzer.maps.world.WorldGraph;
import edu.brown.cs.ilayzer.maps.world.WorldNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * class to test out the graph algorithms (I will use astar in this case)
 */
public class GraphAlgTest {
  WorldGraph worldGraph;


  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    // set up world and executables
    World world = new World(6731);
    worldGraph = new WorldGraph(world);
  }

  /**
   * Sets up.
   */
  @After
  public void tearDown() {
    worldGraph.clearGraph();
  }

  @Test
  public void testCheaperButLonger() throws Exception {
    WorldNode start = new WorldNode("start", true, new double[]{0.0, 0.0});
    worldGraph.setStartNode(start);
    WorldNode close1 = new WorldNode("close1", true, new double[]{1.0, 1.0});
    WorldNode close2 = new WorldNode("close2", true, new double[]{2.0, 2.0});
    WorldNode close3 = new WorldNode("close3", true, new double[]{3.0, 3.0});
    WorldNode close4 = new WorldNode("close4", true, new double[]{4.0, 4.0});
    WorldNode far = new WorldNode("far", true, new double[]{5.0, 5.0});
    worldGraph.addToNodes(close1);
    worldGraph.addToNodes(close2);
    worldGraph.addToNodes(close3);
    worldGraph.addToNodes(close4);
    worldGraph.addToNodes(far);
    WorldEdge edge01 = new WorldEdge(1, "01", null, worldGraph.getRoot(),
            close1);
    WorldEdge edge12 = new WorldEdge(1, "02", null, close1,
            close2);
    WorldEdge edge23 = new WorldEdge(1, "03", null, close2,
            close3);
    WorldEdge edge34 = new WorldEdge(1, "04", null, close3,
            close4);
    WorldEdge edge4f = new WorldEdge(1, "05", null, close4,
            far);
    WorldEdge edge0f = new WorldEdge(40, "06", null, worldGraph.getRoot(),
            far);
    worldGraph.getRoot().addEdgeFrom(edge01);
    close1.addEdgeFrom(edge12);
    close2.addEdgeFrom(edge23);
    close3.addEdgeFrom(edge34);
    close4.addEdgeFrom(edge4f);
    worldGraph.getRoot().addEdgeFrom(edge0f);
    Dijkstra<WorldNode, WorldEdge> astar = new Dijkstra<>(worldGraph,
            new AStarComparator<>(far,
            new Euclidean<>()));
    astar.search(far.getId());
    assertEquals(far.getPrev().getId(), close4.getId());
    assertEquals(close4.getPrev().getId(), close3.getId());
    assertEquals(close3.getPrev().getId(), close2.getId());
    assertEquals(close2.getPrev().getId(), close1.getId());
    assertEquals(close1.getPrev().getId(), worldGraph.getRoot().getId());
    worldGraph.clearGraph();
  }

  /**
   * Test to figure out if in astar, the algorithm is prioritizing going
   * towards the destination.
   */
  @Test
  public void testAstarAlg() throws Exception {
    WorldNode start = new WorldNode("start", true, new double[]{0.0, 0.0});
    worldGraph.setStartNode(start);
    WorldNode right = new WorldNode("right", true, new double[]{1.0, 0.0});
    WorldNode up = new WorldNode("up", true, new double[]{0.0, 1.0});
    WorldNode dest = new WorldNode("dest", true, new double[]{0.0, 2.0});
    worldGraph.addToNodes(up);
    worldGraph.addToNodes(right);
    worldGraph.addToNodes(dest);
    WorldEdge edge01 = new WorldEdge(1, "01", null, worldGraph.getRoot(),
            right);
    WorldEdge edge12 = new WorldEdge(1, "02", null, worldGraph.getRoot(),
            up);
    //making sure the path is longer, but up should be close to
    // target so astar should prioritize it
    WorldEdge edge23 = new WorldEdge(1.1, "03", null, up,
            dest);
    WorldEdge edge34 = new WorldEdge(1, "04", null, right,
            dest);
    worldGraph.getRoot().addEdgeFrom(edge01);
    worldGraph.getRoot().addEdgeFrom(edge12);
    up.addEdgeFrom(edge23);
    right.addEdgeFrom(edge34);
    Dijkstra<WorldNode, WorldEdge> astar = new Dijkstra<>(worldGraph,
            new AStarComparator<>(dest,
            new Euclidean<>()));
    astar.search(dest.getId());
    assertEquals(dest.getPrev().getId(), up.getId());
    assertEquals(up.getPrev().getId(), worldGraph.getRoot().getId());
    worldGraph.clearGraph();
  }

  @Test
  public void testNoRoute() throws Exception {
    WorldNode start = new WorldNode("start", true, new double[]{0.0, 0.0});
    worldGraph.setStartNode(start);
    WorldNode close1 = new WorldNode("close1", true, new double[]{1.0, 1.0});
    WorldNode close2 = new WorldNode("close2", true, new double[]{2.0, 2.0});
    WorldNode close3 = new WorldNode("close3", true, new double[]{3.0, 3.0});
    WorldNode close4 = new WorldNode("close4", true, new double[]{4.0, 4.0});
    WorldNode dest = new WorldNode("dest", true, new double[]{5.0, 5.0});
    WorldNode far = new WorldNode("far", true, new double[]{6.0, 6.0});
    worldGraph.addToNodes(close1);
    worldGraph.addToNodes(close2);
    worldGraph.addToNodes(close3);
    worldGraph.addToNodes(close4);
    worldGraph.addToNodes(dest);
    worldGraph.addToNodes(far);
    WorldEdge edge01 = new WorldEdge(1, "01", null, worldGraph.getRoot(),
            close1);
    WorldEdge edge12 = new WorldEdge(1, "02", null, close1,
            close2);
    WorldEdge edge23 = new WorldEdge(1, "03", null, close2,
            close3);
    WorldEdge edge34 = new WorldEdge(1, "04", null, close3,
            close4);
    WorldEdge edge4f = new WorldEdge(1, "05", null, close4,
            far);
    WorldEdge edge0f = new WorldEdge(40, "06", null, worldGraph.getRoot(),
            far);
    worldGraph.getRoot().addEdgeFrom(edge01);
    close1.addEdgeFrom(edge12);
    close2.addEdgeFrom(edge23);
    close3.addEdgeFrom(edge34);
    close4.addEdgeFrom(edge4f);
    worldGraph.getRoot().addEdgeFrom(edge0f);
    Dijkstra<WorldNode, WorldEdge> astar = new Dijkstra<>(worldGraph,
            new DijkstraComparator<>());
    astar.search(dest.getId());
    assertNull(dest.getPrev());
    worldGraph.clearGraph();
  }
}
