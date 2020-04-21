package edu.brown.cs.ilayzer.comparators;

import edu.brown.cs.ilayzer.graph.Edge;
import edu.brown.cs.ilayzer.graph.GraphNode;

/**
 * Class to compare graph nodes based on value.
 *
 * @param <N> the type of GraphNode
 * @param <E> the type of GraphEdge
 */
public class DijkstraComparator<N extends GraphNode<E>, E extends Edge>
    implements GraphComparator<N, E> {

  /**
   * comparator method to compare the graph nodes.
   *
   * @param o1 the first node.
   * @param o2 a second node.
   * @return 1 if the first is "greater", or -1 if it is less than.
   */
  @Override
  public int compare(N o1, N o2) {
    double val1 = o1.getValue();
    double val2 = o2.getValue();
    return Double.compare(val1, val2);
  }
}
