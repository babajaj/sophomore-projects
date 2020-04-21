package edu.brown.cs.ilayzer.graph;

import java.util.List;

/**
 * Generic expandable graph interface.
 * @param <N> the type of node that the graph contains
 * @param <E> the type of graph edge
 */
public interface Graph<N extends GraphNode<E>, E extends Edge> {

  /**
   * method to expand a graph on a node.
   * @param element the node/element from which to expand
   * @return the list of nodes it branches to (not including already known
   * ones)
   * @throws Exception if the database it uses cannot provide information
   * correctly
   */
  List<N> expand(N element) throws Exception;

  /**
   * getter method to return the root/origin of a graph.
   * @return the root
   */
  N getRoot();

  /**
   * getter method for node on the other side of an edge from a node.
   * @param edge the edge connecting the two nodes
   * @param node the known node in the edge
   * @return the neighbor node
   */
  N getNeighbor(E edge, N node);

  /**
   * Sets the previous node of a node in a path.
   * @param currNode the node to set the previous for
   * @param prevNode the previous node
   * @param edge the edge connecting the two nodes
   */
  void setPrev(N currNode, N prevNode, E edge);

  /**
   * Sets the root of the graph.
   * @param root the node to be the new root.
   */
  void setStartNode(N root);

}
