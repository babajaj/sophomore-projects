package edu.brown.cs.ilayzer.tIMDb.actors;

import edu.brown.cs.ilayzer.graph.Edge;
import edu.brown.cs.ilayzer.graph.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for the actor node.
 */
public class ActorNode implements GraphNode<ActorEdge> {

  private String id;
  private double dist;
  private ActorNode prev;
  private List<ActorEdge> edges;
  private Edge prevEdge;
  private HashMap<ActorEdge, String> edgeMovies;
  private String actorName;

  /**
   * Constructor for an actor node.
   *
   * @param id   the actor id/element of the node.
   * @param name the value/distance of the actor node
   */
  public ActorNode(String id, String name) {
    this.id = id;
    edges = new ArrayList<>();
    dist = Double.POSITIVE_INFINITY;
    prev = null;
    edgeMovies = new HashMap<>();
    this.actorName = name;
  }

  /**
   * method to set the value of the actor node.
   *
   * @param distance the distance of the node
   */
  public void setValue(double distance) {
    this.dist = distance;
  }

  /**
   * getter method for the value of the node.
   *
   * @return the distance from the root
   */
  public double getValue() {
    return dist;
  }

  /**
   * method to set the previous node in the path of a node.
   *
   * @param previous The previous actorNode.
   * @param previousEdge The edge that connects the previous node to this object.
   */
  public void setPrev(ActorNode previous, Edge previousEdge) {
    this.prev = previous;
    this.prevEdge = previousEdge;
  }


  /**
   * method to get the previous node in the path.
   *
   * @return the previous node
   */
  public ActorNode getPrev() {
    return prev;
  }

  /**
   * gets the movie id of the edge to the previous node.
   *
   * @return the previous edge id
   */
  public String getPrevMovieID() {
    return edgeMovies.get(prevEdge);
  }

  /**
   * adds an edge to a node and puts it in the edge-mmovie map.
   *
   * @param edge  the edge to add
   * @param movie the movie corresponding to the edge
   */
  public void addEdge(ActorEdge edge, String movie) {
    edges.add(edge);
    edgeMovies.put(edge, movie);

  }

  /**
   * Gets the edges of a node.
   *
   * @return a list of the edges.
   */
  @Override
  public List<ActorEdge> getEdges() {
    return edges;
  }


  /**
   * Gets the element contained in a node.
   * @return Returns the string id of the actor this node represents.
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the actor name of the node.
   *
   * @return the String name
   */
  public String getActorName() {
    return actorName;
  }


}
