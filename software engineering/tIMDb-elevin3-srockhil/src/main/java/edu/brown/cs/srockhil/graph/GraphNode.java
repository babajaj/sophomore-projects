package edu.brown.cs.srockhil.graph;

import java.util.List;

/**
 * Interface for a generic graph node in a generic graph.
 */
public interface GraphNode {

  /**
   * getter method to get the "id" of a node. Should be unique.
   * @return the element/id
   */
  String getElement();

  /**
   * sets the value of the node. Not necessarily unique.
   * @param value the value to set it to
   */
  void setValue(double value);

  /**
   * getter method for the value of a node.
   * @return the value of the node.
   */
  double getValue();

  /**
   * gets the edges connected to a node.
   * @return an arraylist of the edges
   */
  List<Edge> getEdges();

}
