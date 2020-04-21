package edu.brown.cs.ilayzer.algorithms;

import edu.brown.cs.ilayzer.comparators.GraphComparator;
import edu.brown.cs.ilayzer.graph.GraphNode;
import edu.brown.cs.ilayzer.graph.Graph;
import edu.brown.cs.ilayzer.graph.Edge;
import edu.brown.cs.ilayzer.maps.world.WorldNode;

import java.util.*;


/**
 * Class that performs dijkstra on a graph.
 *
 * @param <T> the type of node in the graph
 * @param <E> the type of the edge in the graph
 */
public class Dijkstra<T extends GraphNode<E>, E extends Edge>
        implements GraphAlg<T, E> {

  private List<T> unvisited;
  private Graph<T, E> graph;
  private GraphComparator<T, E> comparator;
  private Set<T> visited;

  /**
   * constructor for dijkstra.
   *
   * @param graph      the graph dijkstra will operate on
   * @param comparator a comparator that determines what kind of search
   *                   (Dijkstra or AStar)
   */
  public Dijkstra(Graph<T, E> graph, GraphComparator<T, E> comparator) {
    this.graph = graph;
    this.comparator = comparator;
    unvisited = new ArrayList<>();
    unvisited.add(graph.getRoot());
    visited = new HashSet<>();
  }

  /**
   * Main method of search to build the shortest path to the target node.
   *
   * @param targetVal the value of the node to look for.
   * @return The node corresponding to the value given.
   * @throws Exception if the graph cannot use the database to expand
   *                   correctly.
   */
  @Override
  public T search(String targetVal) throws Exception {
    if (this.graph.getRoot() == null) {
      return null;
    }
    while (!unvisited.isEmpty()) {
      T curr = unvisited.remove(0);
      //if we've found the element
      if (curr.getId().equals(targetVal)) {
        return curr;
      }
      visited.add(curr);
      //adds all new nodes into the list
      //this line throws an exception if there is an error during
      // expansion of the graph
      for (T node : graph.expand(curr)) {
        if (!visited.contains(node)){
          unvisited.add(node);
        }
      }
      List<E> edges = curr.getEdges();
      //loop through all edges
      for (E edge : edges) {
        T neighbor = graph.getNeighbor(edge, curr);
        //if a neighbor is unvisited,
        if (unvisited.contains(neighbor) && !visited.contains(neighbor)) {
          int compare = Double.compare(curr.getValue() + edge.getWeight(),
                  neighbor.getValue());
          if (compare < 0) {
            //if this path is a closer path to this node
            neighbor.setValue(curr.getValue() + edge.getWeight());
          }
          graph.setPrev(neighbor, curr, edge);
        }
      }
      unvisited.sort(comparator);
    }
    return null;
  }


}
