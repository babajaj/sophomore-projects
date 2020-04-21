package edu.brown.cs.ilayzer.tIMDb.actors;

import edu.brown.cs.ilayzer.graph.Edge;

/**
 * class representing an edge in a generic graph.
 */
public class ActorEdge implements Edge {

  private double weight;

  /**
   * Constructor for the edge.
   *
   * @param weight the weight of the edge.
   */
  public ActorEdge(double weight) {
    this.weight = weight;
  }

  /**
   * getter method for the weight of the edge.
   *
   * @return the weight of the edge
   */
  public double getWeight() {
    return weight;
  }

  @Override
  public String getID() {
    return null;
  }

}
