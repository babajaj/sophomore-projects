package edu.brown.cs.ilayzer.comparators;

import edu.brown.cs.ilayzer.graph.Edge;
import edu.brown.cs.ilayzer.graph.GraphNode;

import java.util.Comparator;

/**
 * An interface for comparators to use with Graph algorithms.
 * @param <N> the type of GraphNode to be compared
 * @param <E> the type of GraphEdge
 */
public interface GraphComparator<N extends GraphNode<E>, E extends Edge>
    extends Comparator<N> {

}
