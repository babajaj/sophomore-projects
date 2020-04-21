package edu.brown.cs.ilayzer.tIMDb.actors;

import edu.brown.cs.ilayzer.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for an actor graph of actor nodes for tIMDB.
 */
public class ActorGraph implements Graph<ActorNode, ActorEdge> {

  private ActorDatabase database;
  private HashMap<String, ActorNode> nodes;
  private HashMap<ActorEdge, ActorNode[]> neighbors;
  private ActorNode startNode;

  /**
   * Constructor for an actor graph of actor nodes.
   *
   * @param database the database used to build the graph
   */
  public ActorGraph(ActorDatabase database) {
    this.database = database;
    nodes = new HashMap<>();
    neighbors = new HashMap<>();
  }

  /**
   * method to expand an actor graph on an actor node.
   *
   * @param actorNode the actor node/element from which to expand.
   * @return the list of actor nodes it branches to (not including
   * already known ones)
   * @throws Exception if the database cannot return information.
   */
  @Override
  public List<ActorNode> expand(ActorNode actorNode) throws Exception {
    List<ActorNode> newNodes = new ArrayList<>();
    //gets all the movies the actor has been in
    List<String> movies =
            database.query("aidToMids", actorNode.getId());
    String currFullName =
            database.query("aidToName", actorNode.getId()).get(0);
    //loops through all the movies
    for (String movieID : movies) {
      //gets all the actors for each movie
      List<String> ids = database.query("midToAids", movieID);
      //for each actor, checks if the name constraint passes
      for (String id : ids) {
        String neighborFullName = database.query("aidToName", id).get(0);
        //if the name constraint passes
        if (nameChecker(currFullName, neighborFullName)) {
          //checks if that actor node is already in the graph
          ActorNode newNode = nodes.get(id);
          //adds a new node if its not already in the graph
          if (newNode == null) {
            newNode = new ActorNode(id, neighborFullName);
            newNode.setValue(Double.POSITIVE_INFINITY);
            newNodes.add(newNode);
            nodes.put(id, newNode);
          }
          //either way, it adds a new edge from the current node to this neighbor
          double weight = 1.0 / (double) ids.size();
          ActorEdge edge = new ActorEdge(weight);
          actorNode.addEdge(edge, movieID);
          newNode.addEdge(edge, movieID);
          neighbors.put(edge, new ActorNode[]{actorNode, newNode});
        }
      }
    }
    return newNodes;
  }

  /**
   * getter method to get the root of the graph (used for distance/path).
   *
   * @return the root node
   */
  @Override
  public ActorNode getRoot() {
    return startNode;
  }

  /**
   * Gets the neighbor of a node in a graph given an edge.
   *
   * @param edge the edge connecting the two nodes
   * @param node the known node in the edge
   * @return the other node in the edge
   */
  @Override
  public ActorNode getNeighbor(ActorEdge edge, ActorNode node) {
    ActorNode[] edgeNodes = neighbors.get(edge);
    if (edgeNodes[0].equals(node)) {
      return edgeNodes[1];
    }
    return edgeNodes[0];
  }


  /**
   * sets the previous actor node to an actor node (for path).
   *
   * @param currNode the node to set the previous for
   * @param prevNode the previous node
   * @param edge     the edge connecting the two nodes
   */
  public void setPrev(ActorNode currNode, ActorNode prevNode, ActorEdge edge) {
    currNode.setPrev(prevNode, edge);
  }


  /**
   * sets the start node/root actor node in the graph.
   * @param root the new root
   */
  public void setStartNode(ActorNode root) {
    startNode = root;
    startNode.setValue(0);
    nodes.put(root.getId(), startNode);
  }

  /**
   * Method to check the firstName -- lastName constraint between two
   * actor nodes.
   *
   * @param currFullName     the name of the current node
   * @param neighborFullName the name of the neighbor node/second actor node
   * @return true if the constrain is satisfied, false otherwise.
   */
  public boolean nameChecker(String currFullName, String neighborFullName) {
    if (currFullName.length() == 0 || neighborFullName.length() == 0) {
      return false;
    }
    String[] currNames = currFullName.split("\\s+");
    String currLastName = currNames[currNames.length - 1];
    char currLNChar = currLastName.toCharArray()[0];
    char neighborFNChar = neighborFullName.toCharArray()[0];
    return currLNChar == neighborFNChar;
  }


}
