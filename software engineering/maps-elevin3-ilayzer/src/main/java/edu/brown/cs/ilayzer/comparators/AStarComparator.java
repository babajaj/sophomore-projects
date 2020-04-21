package edu.brown.cs.ilayzer.comparators;

import edu.brown.cs.ilayzer.distance.Distance;
import edu.brown.cs.ilayzer.graph.Edge;
import edu.brown.cs.ilayzer.graph.GraphNode;
import edu.brown.cs.ilayzer.distance.Coordinates;

/**
 * Class to compare graph nodes in astar algorithm based on value.
 *
 * @param <E> the type of the edge
 * @param <N> the type of the node
 */
public class AStarComparator<N extends GraphNode<E> & Coordinates,
        E extends Edge> implements GraphComparator<N, E> {
  private N targetPoint;
  private Distance<N> metric;

  /**
   * Constructor for an AStar comparator.
   * @param targetPoint the target point
   * @param metric a distance metric
   */
  public AStarComparator(N targetPoint, Distance<N> metric) {
    this.targetPoint = targetPoint;
    this.metric = metric;
  }

  /**
   * comparator method to compare the graph nodes by weight.
   *
   * @param n1 the first node.
   * @param n2 a second node.
   * @return 1 if the first is "greater", or -1 if it is less than.
   */
  @Override
  public int compare(N n1, N n2) {
    double val1 = n1.getValue();
    double val2 = n2.getValue();
    double target1 = metric.distance(n1, this.targetPoint);
    double target2 = metric.distance(n2, this.targetPoint);
    return Double.compare(val1 + target1, val2 + target2);
  }
}
