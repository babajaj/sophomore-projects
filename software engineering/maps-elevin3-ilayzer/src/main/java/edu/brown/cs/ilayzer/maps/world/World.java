package edu.brown.cs.ilayzer.maps.world;

import edu.brown.cs.ilayzer.kdtree.KDTree;

/**
 * A class that stores world information in useful data structures.
 */
public class World {

  // world information
  private static final double LATITUDE_MIN = -90.0;
  private static final double LATITUDE_MAX = 90.0;
  private static final double LONGITUDE_MIN = -180.0;
  private static final double LONGITUDE_MAX = 180.0;

  private double radius;
  private WorldDatabase wdb; // a class that connects to the database
  private KDTree<WorldNode> tree;
  private WorldGraph graph;

  /**
   * Constructor for a world.
   * @param radius the radius of the planet.
   */
  public World(double radius) {
    this.radius = radius;
  }

  /**
   * Gets the radius of the planet.
   * @return the radius.
   */
  public double getRadius() {
    return radius;
  }

  /**
   * Returns the database connection class.
   * @return the world database.
   */
  public WorldDatabase getDatabase() {
    return wdb;
  }

  /**
   * Sets the WorldDatabase to the passed in db connection.
   * @param db a class connected to a database.
   */
  public void setDatabase(WorldDatabase db) {
    this.wdb = db;
  }

  /**
   * Getter for the KDTree.
   * @return the kd tree.
   */
  public KDTree<WorldNode> getTree() {
    return tree;
  }

  /**
   * Sets the world kd tree to the inputted kd tree.
   * @param tree the tree to be set
   */
  public void setTree(KDTree<WorldNode> tree) {
    this.tree = tree;
  }

  /**
   * Method to set the root of the graph in the world.
   * @param root the new root
   */
  public void setGraphRoot(WorldNode root) {
    graph.setStartNode(root);
  }

  /**
   * Method to get the graph of a world.
   *
   * @return the graph
   */
  public WorldGraph getGraph() {
    return this.graph;
  }

  /**
   * Checks whether or not the inputted latitude and longitude are valid.
   * @param latitude a latitude
   * @param longitude a longitude
   * @return boolean whether or not the inputted latitude and longitude are valid
   */
  public boolean areValidCoordinates(double latitude, double longitude) {
    return latitude > LATITUDE_MIN && latitude < LATITUDE_MAX
        && longitude > LONGITUDE_MIN && longitude < LONGITUDE_MAX;
  }

  /**
   * Sets the graph of the world.
   * @param graph the graph to be set
   */
  public void setGraph(WorldGraph graph) {
    this.graph = graph;
  }
}
