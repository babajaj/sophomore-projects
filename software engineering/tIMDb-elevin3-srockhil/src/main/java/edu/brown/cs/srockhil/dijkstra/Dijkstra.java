package edu.brown.cs.srockhil.dijkstra;

import edu.brown.cs.srockhil.graph.GraphNode;
import edu.brown.cs.srockhil.graph.Graph;
import edu.brown.cs.srockhil.graph.GraphNodeComparator;
import edu.brown.cs.srockhil.graph.Edge;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that performs dijkstra on a graph.
 *
 * @param <T> the type of node in the graph
 */
public class Dijkstra<T extends GraphNode> {

  private List<T> unvisited;
  private Graph<T> graph;
  private GraphNodeComparator comparator;

  /**
   * constructor for dijkstra.
   *
   * @param graph the graph dijkstra will operate on
   */
  public Dijkstra(Graph<T> graph) {
    this.graph = graph;
    comparator = new GraphNodeComparator();
    unvisited = new ArrayList<>();
    unvisited.add(graph.getRoot());
  }


  /**
   * Main method of search to build the shortest path to the target node.
   *
   * @param targetVal the value of the node to look for.
   * @return The node corresponding to the value given.
   * @throws Exception if the graph cannot use the database to expand
   * correctly.
   */
  public T search(String targetVal) throws Exception {
    T curr;
    while (!unvisited.isEmpty()) {
      curr = unvisited.remove(0);
      //if we've found the element
      if (curr.getElement().equals(targetVal)) {
        return curr;
      }
      //adds all new nodes into the list
      //this line throws an exception if there is an error during
      // expansion of the graph
      unvisited.addAll(graph.expand(curr));
      List<Edge> edges = curr.getEdges();
      //loop through all edges
      for (Edge edge : edges) {
        T neighbor = graph.getNeighbor(edge, curr);
        //if a neighbor is unvisited,
        if (unvisited.contains(neighbor)) {
          int compare = Double.compare(curr.getValue() + edge.getWeight(),
                  neighbor.getValue());
          if (compare < 0) {
            //if this path is a closer path to this node
            neighbor.setValue(curr.getValue() + edge.getWeight());
            graph.setPrev(neighbor, curr, edge);
          }
        }
      }
      unvisited.sort(comparator);
    }
    return null;
  }


}
