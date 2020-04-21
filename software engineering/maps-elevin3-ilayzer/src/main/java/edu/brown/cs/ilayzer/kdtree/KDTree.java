package edu.brown.cs.ilayzer.kdtree;

import com.google.common.collect.MinMaxPriorityQueue;
import edu.brown.cs.ilayzer.comparators.AxisComparator;
import edu.brown.cs.ilayzer.comparators.DistanceComparator;
import edu.brown.cs.ilayzer.distance.Axis;
import edu.brown.cs.ilayzer.distance.Coordinates;
import edu.brown.cs.ilayzer.distance.Euclidean;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Class for a KD Tree of type T that stores tree in the node field "root".
 * @param <T> any object type that implements the Cartesian interface
 */
public class KDTree<T extends Coordinates> {

  private int dimensions; // dimension of search space
  private KDNode<T> root; // the root node of the tree
  private MinMaxPriorityQueue<T> neighborsQueue; // queue for nearest neighbors
  private List<T> radiusNeighbors; // list to add neighbors within radius to
  // distance metrics
  private Euclidean<T> euclidean;
  private Axis<T> axis;

  /**
   * Constructor for a KD tree.
   * @param elements an array of elements to store in kd tree
   * @param dimensions the dimension of the search space
   */
  public KDTree(T[] elements, int dimensions) {
    this.dimensions = dimensions;
    this.root = generateKDTree(elements, 0);
    this.euclidean = new Euclidean<>();
    this.axis = new Axis<>(0);
  }

  /**
   * Gets the root of the tree.
   * @return the root of the tree
   */
  public KDNode<T> getRoot() {
    return root;
  }

  /**
   * Recursive method that generates a KD-Tree according to given algorithm.
   * @param elements an array of objects containing points in R^n where n is dimensions
   * @param currentDepth a counter to be passed to help find sortByDimension
   * @return a KD-Tree containing the points in elements
   */
  private KDNode<T> generateKDTree(T[] elements, int currentDepth) {
    // base case
    if (elements.length == 0) {
      return null;
    }
    // get the current dimension to sort by by taking counter mod dimensions
    int sortByDimension = currentDepth % this.dimensions;
    // instantiate comparator
    AxisComparator comp = new AxisComparator(sortByDimension);
    // sort array
    Arrays.sort(elements, comp);
    // get median element and make it the root
    int medianIndex = elements.length / 2;
    T medianPoint = elements[medianIndex];
    KDNode<T> node = new KDNode<>(medianPoint);
    // get elements to put in subtrees
    T[] leftElements = Arrays.copyOfRange(elements, 0, medianIndex);
    T[] rightElements = Arrays.copyOfRange(elements, medianIndex + 1, elements.length);
    // generate subtrees
    node.setLeft(generateKDTree(leftElements, currentDepth + 1));
    node.setRight(generateKDTree(rightElements, currentDepth + 1));
    return node;
  }

  /**
   * Method that finds the k nearest neighbors to a target point.
   * @param targetPoint a target point instance of T
   * @param k the number of nearest neighbors to find
   * @return a list of the k nearest neighbors
   */
  public List<T> nearestNeighbors(T targetPoint, int k) {
    // list to which to add nearest neighbors later
    List<T> nearestNeighbors = new LinkedList<>();
    if (k == 0) {
      return nearestNeighbors;
    }
    // instantiate min-max priority queue to store k nearest neighbors
    this.neighborsQueue = MinMaxPriorityQueue.orderedBy(
        new DistanceComparator<>(targetPoint, euclidean))
        .maximumSize(k).create();

    // find nearest neighbors (fill queue) with recursive helper
    neighborsHelper(this.root, targetPoint, k, 0);

    // transfer nearest neighbors from priority queue to list
    while (!this.neighborsQueue.isEmpty()) {
      nearestNeighbors.add(this.neighborsQueue.poll());
    }
    neighborsQueue = null; // free up neighborsQueue memory
    return nearestNeighbors;
  }

  /**
   * Recursive helper that performs nearest neighbors algorithm, is void.
   * because it fills the static MinMaxPriorityQueue
   * @param current the current node
   * @param targetPoint the target point to find nearest neighbors around
   * @param k the number of nearest neighbors to find
   * @param depth the depth
   */
  private void neighborsHelper(KDNode<T> current, T targetPoint, int k, int depth) {
    // check if current node is null
    if (current == null) {
      return;
    }
    // offer current node to queue
    this.neighborsQueue.offer(current.getValue());
    // get farthest of the nearest neighbors
    T farthestNeighbor = this.neighborsQueue.peekLast();

    int relevantAxis = depth % this.dimensions;
    this.axis.setAxis(relevantAxis);

    double farthestEuclideanDistance =
        euclidean.distance(targetPoint, farthestNeighbor);
    double relevantAxisDistance =
        axis.distance(targetPoint, current.getValue());

    if (farthestEuclideanDistance > relevantAxisDistance) {
      neighborsHelper(current.getLeft(), targetPoint, k, depth + 1);
      neighborsHelper(current.getRight(), targetPoint, k, depth + 1);
    } else if (current.getValue().getCoordinates()[relevantAxis] < targetPoint.
        getCoordinates()[relevantAxis]) {
      neighborsHelper(current.getRight(), targetPoint, k, depth + 1);
      // check if we found enough nearest neighbors
      if (neighborsQueue.size() < k) {
        // search left subtree
        neighborsHelper(current.getLeft(), targetPoint, k, depth + 1);
      }
    } else {
      neighborsHelper(current.getLeft(), targetPoint, k, depth + 1);
      // check if we found enough nearest neighbors
      if (neighborsQueue.size() < k) {
        // search right subtree
        neighborsHelper(current.getRight(), targetPoint, k, depth + 1);
      }
    }
  }

  /**
   * Finds all the neighboring elements within a radius from a point.
   * @param targetPoint the point around which to search
   * @param radius the search radius
   * @return the list of neighboring cartesian elements
   */
  public List<T> radiusSearch(T targetPoint, double radius) {
    this.radiusNeighbors = new LinkedList<>();
    // populate list with radius helper
    radiusHelper(this.root, targetPoint, radius, 0);
    // sort collection so that nearest neighbors are first
    this.radiusNeighbors.sort(new DistanceComparator<>(targetPoint, euclidean));
    return this.radiusNeighbors;
  }

  /**
   * Recursive helper that performs main radius search algorithm, is void
   * because it fills the static radiusNeighbors list while searching.
   * @param current the current node
   * @param targetPoint the target point around which to search
   * @param radius the radius distance
   * @param depth the current depth in the tree used to determine which
   *              axis to use for relevant axis distance
   */
  private void radiusHelper(KDNode<T> current, T targetPoint, double radius, int depth) {
    // check if current node is null
    if (current == null) {
      return;
    }
    // if current is within radius add to radius neighbors
    double euclideanDistance = euclidean.distance(targetPoint, current.getValue());
    if (euclideanDistance <= radius) {
      this.radiusNeighbors.add(current.getValue());
    }

    int relevantAxis = depth % this.dimensions;
    this.axis.setAxis(relevantAxis);

    double relevantAxisDistance = axis.distance(targetPoint, current.getValue());

    if (relevantAxisDistance < radius) {
      radiusHelper(current.getLeft(), targetPoint, radius, depth + 1);
      radiusHelper(current.getRight(), targetPoint, radius, depth + 1);
    } else if (current.getValue().getCoordinates()[relevantAxis] < targetPoint.
        getCoordinates()[relevantAxis]) {
      radiusHelper(current.getRight(), targetPoint, radius, depth + 1);
    } else {
      radiusHelper(current.getLeft(), targetPoint, radius, depth + 1);
    }
  }
}
