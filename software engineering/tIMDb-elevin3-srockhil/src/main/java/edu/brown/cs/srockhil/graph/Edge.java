package edu.brown.cs.srockhil.graph;


/**
 * class representing an edge in a generic graph.
 */
public class Edge {

  private double weight;

  /**
   * Constructor for the edge.
   *
   * @param weight the weight of the edge.
   */
  public Edge(double weight) {
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
}
