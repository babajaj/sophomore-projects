package edu.brown.cs.ilayzer.algorithms;


import edu.brown.cs.ilayzer.graph.Edge;
import edu.brown.cs.ilayzer.graph.GraphNode;

/**
 * Interface for graph algorithm.
 * @param <T> the type of graph node
 * @param <E> the type of graph edge
 */
public interface GraphAlg<T extends GraphNode<E>, E extends Edge> {

  /**
   * Searches the graph for a node of value targetVal.
   * @param targetVal the target
   * @return the node of val targetVal
   * @throws Exception if exception thrown
   */
  T search(String targetVal) throws Exception;

}
