package edu.brown.cs.srockhil.graph;

import java.util.List;

/**
 * Generic expandable graph interface.
 * @param <T> the type of node that the graph contains
 */
public interface Graph<T> {

  /**
   * method to expand a graph on a node.
   * @param element the node/element from which to expand
   * @return the list of nodes it branches to (not including already known
   * ones)
   * @throws Exception if the database it uses cannot provide information
   * correctly
   */
  List<T> expand(T element) throws Exception;

  /**
   * getter method to return the root/origin of a graph.
   * @return the root
   */
  T getRoot();

  /**
   * getter method for node on the other side of an edge from a node.
   * @param edge the edge connecting the two nodes
   * @param node the known node in the edge
   * @return the neighbor node
   */
  T getNeighbor(Edge edge, T node);

  /**
   * Sets the previous node of a node in a path.
   * @param currNode the node to set the previous for
   * @param prevNode the previous node
   * @param edge the edge connecting the two nodes
   */
  void setPrev(T currNode, T prevNode, Edge edge);

}
