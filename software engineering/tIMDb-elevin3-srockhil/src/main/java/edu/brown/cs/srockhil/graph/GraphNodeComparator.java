package edu.brown.cs.srockhil.graph;

import java.util.Comparator;

/**
 * Class to compare graph nodes based on value.
 */
public class GraphNodeComparator implements Comparator<GraphNode> {

  /**
   * comparator method to compare the graph nodes.
   * @param o1 the first node.
   * @param o2 a second node.
   * @return 1 if the first is "greater", or -1 if it is less than.
   */
  @Override
  public int compare(GraphNode o1, GraphNode o2) {
    double val1 = o1.getValue();
    double val2 =  o2.getValue();
    return Double.compare(val1, val2);
  }
}
