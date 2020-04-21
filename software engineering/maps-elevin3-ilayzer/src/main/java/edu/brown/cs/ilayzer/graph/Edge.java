package edu.brown.cs.ilayzer.graph;


/**
 * class representing an edge in a generic graph.
 */
public interface Edge {

  /**
   * getter method for the weight of the edge.
   *
   * @return the weight of the edge
   */
  double getWeight();

  /**
   * Gets the id of an edge.
   * @return the id of an edge
   */
  String getID();
}
