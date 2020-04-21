package edu.brown.cs.ilayzer.maps.world;

import edu.brown.cs.ilayzer.graph.Edge;

/**
 * A class for an edge in the world graph.
 */
public class WorldEdge implements Edge {

  private double weight;
  private String id;
  private String name;
  private WorldNode start;
  private WorldNode end;
  /**
   * Constructor for an edge.
   * @param weight the weight of the edge
   * @param id the id
   * @param name the name
   * @param start the start node
   * @param end the destination/end node
   */
  public WorldEdge(double weight, String id, String name, WorldNode start,
                   WorldNode end) {
    this.weight = weight;
    this.id = id;
    this.name = name;
    this.start = start;
    this.end = end;
  }

  /**
   * Gets the weight of the edge.
   * @return the weight
   */
  @Override
  public double getWeight() {
    return this.weight;
  }

  /**
   * Getter method for the edge ID.
   * @return the id string
   */
  @Override
  public String getID() {
    return this.id;
  }

  /**
   * Gets the name of the edge.
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the starting node of the edge.
   * @return the starting node
   */
  public WorldNode getStart() {
    return this.start;
  }

  /**
   * Returns the destination node of the edge.
   * @return the destination
   */
  public WorldNode getEnd() {
    return this.end;
  }
}
