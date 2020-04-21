package edu.brown.cs.ilayzer.maps.world;

import edu.brown.cs.ilayzer.graph.GraphNode;
import edu.brown.cs.ilayzer.distance.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Class for a WorldNode.
 */
public class WorldNode implements GraphNode<WorldEdge>, Coordinates {

  private String id;
  private Boolean traversable;
  private double[] coordinates;
  private double dist;
  private WorldNode prev;
  private WorldEdge prevEdge;
  //ways coming into a node
  private Set<WorldEdge> toWays;
  //ways coming away from a node
  private Set<WorldEdge> fromWays;

  /**
   * Constructor for a worldnode.
   * @param id the id
   * @param traversable whether or not node is traversable
   * @param coordinates the coordinates of the node in the world (lat, long)
   */
  public WorldNode(String id, Boolean traversable, double[] coordinates) {
    this.id = id;
    toWays = new HashSet<>();
    fromWays = new HashSet<>();
    dist = Double.POSITIVE_INFINITY;
    prev = null;
    this.traversable = traversable;
    this.coordinates = coordinates;
  }

  /**
   * Constructor for wrapping a point in space (everything else null).
   * @param coordinates the coordinates to be wrapped
   */
  public WorldNode(double[] coordinates) {
    this.coordinates = coordinates;
  }

  /**
   * method to set the value of the Wnode.
   *
   * @param distance the distance of the node
   */
  public void setValue(double distance) {
    this.dist = distance;
  }

  /**
   * Gets the coordinates of the world node.
   * @return the coordinates of the node.
   */
  public double[] getCoordinates() {
    return coordinates;
  }

  /**
   * getter method for the value of the node.
   *
   * @return the distance from the root
   */
  public double getValue() {
    return dist;
  }

  /**
   * method to set the previous node in the path of a node.
   *
   * @param previous     The previous WNode.
   * @param previousEdge The edge that connects the previous node to this object.
   */
  public void setPrev(WorldNode previous, WorldEdge previousEdge) {
    this.prev = previous;
    this.prevEdge = previousEdge;
  }


  /**
   * method to get the previous node in the path.
   *
   * @return the previous node
   */
  public WorldNode getPrev() {
    return prev;
  }

  /**
   * gets the way id of the edge to the previous node.
   *
   * @return the previous edge id
   */
  public String getPrevWayID() {
    return prevEdge.getID();
  }

  /**
   * adds an edge to a node and puts it in the edge-node map.
   *
   * @param edge  the edge to add
   */
  public void addEdgeTo(WorldEdge edge) {
    toWays.add(edge);
  }

  /**
   * adds an edge from a node and puts it in the node-edge map.
   *
   * @param edge  the edge to add
   */
  public void addEdgeFrom(WorldEdge edge) {
    fromWays.add(edge);
  }

  /**
   * Gets the edges of a node.
   *
   * @return a list of the edges.
   */
  @Override
  public List<WorldEdge> getEdges() {
    List<WorldEdge> edges = new ArrayList<>();
    edges.addAll(fromWays);
    return edges;
  }

  /**
   * Method to set coordinates of a node.
   * @param coords the coordinates
   */
  public void setCoordinates(double[] coords) {
    this.coordinates = coords;
  }




  /**
   * Gets the id contained in a node.
   *
   * @return Returns the string id of the node.
   */
  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldNode worldNode = (WorldNode) o;
    return id.equals(worldNode.id)
        && traversable.equals(worldNode.traversable)
        && Arrays.equals(coordinates, worldNode.coordinates);
  }

  @SuppressWarnings("checkstyle:MagicNumber")
  @Override
  public int hashCode() {
    int result = Objects.hash(id, traversable);
    result = 31 * result + Arrays.hashCode(coordinates);
    return result;
  }
}
