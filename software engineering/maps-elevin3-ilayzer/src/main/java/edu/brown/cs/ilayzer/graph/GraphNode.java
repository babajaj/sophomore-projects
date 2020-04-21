package edu.brown.cs.ilayzer.graph;

import java.util.List;

/**
 * Interface for a generic graph node in a generic graph.
 *
 * @param <E> the type of edge
 */
public interface GraphNode<E extends Edge> {

  /**
   * getter method to get the "id" of a node. Should be unique.
   * @return the element/id
   */
  String getId();

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
  List<E> getEdges();

}
