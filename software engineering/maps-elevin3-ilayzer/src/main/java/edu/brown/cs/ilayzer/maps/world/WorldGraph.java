package edu.brown.cs.ilayzer.maps.world;

import edu.brown.cs.ilayzer.graph.Graph;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for graph structure of the world.
 */
public class WorldGraph implements Graph<WorldNode, WorldEdge> {

  private World world;
  private Set<WorldNode> nodes;
  private WorldNode startNode;

  /**
   * Constructor for an actor graph of actor nodes.
   *
   * @param world the world that the graph is taking place in
   */
  public WorldGraph(World world) {
    this.world = world;
    this.nodes = new HashSet<>();
  }

  /**
   * method to expand an actor graph on an actor node.
   *
   * @param node the world node/element from which to expand.
   * @return the list of actor worldNodes it branches to (not including
   * already known ones)
   * @throws Exception if the database cannot return information.
   */
  @Override
  public List<WorldNode> expand(WorldNode node) throws Exception {
    Set<WorldNode> newNodes = new HashSet<>();
    List<WorldNode> expanded = new ArrayList<>();
    WorldDatabase db = world.getDatabase();
    //gets all the ways linking away from a node
    if (world.getDatabase() != null) {
      List<WorldEdge> ways =
              db.waysFromNode(node);
      //loops through all the ways
      for (WorldEdge way : ways) {
        WorldNode end = way.getEnd();
        //adds a new node if its not already in the graph
        if (!nodes.contains(end)) {
          nodes.add(end);
          newNodes.add(end);
        }
        //either way, it adds a new edge from the current node to this neighbor
        end.addEdgeTo(way);
        node.addEdgeFrom(way);
      }
    } else {
      for (WorldEdge edge : node.getEdges()) {
        newNodes.add(edge.getEnd());
      }
    }
    expanded.addAll(newNodes);
    return expanded;
  }

  /**
   * Method to get the root of the graph.
   * @return the root node.
   */
  @Override
  public WorldNode getRoot() {
    return startNode;
  }


  /**
   * Gets the neighbor of a node.
   * @param edge the edge connecting the two nodes
   * @param node the known node in the edge
   * @return the node at the other end of the edge.
   */
  @Override
  public WorldNode getNeighbor(WorldEdge edge, WorldNode node) {
    return edge.getEnd();
  }

  /**
   * Method to set the previous node of a node.
   * @param currNode the node to set the previous for
   * @param prevNode the previous node
   * @param edge the edge connecting the two nodes
   */
  @Override
  public void setPrev(WorldNode currNode, WorldNode prevNode, WorldEdge edge) {
    currNode.setPrev(prevNode, edge);
  }

  /**
   * Clears the graph.
   */
  public void clearGraph() {
    this.startNode = null;
    this.nodes.clear();
  }

  /**
   * Adds a node to the graph.
   * @param node the node to be added
   */
  public void addToNodes(WorldNode node) {
    nodes.add(node);
  }

  /**
   * Sets a node to be the root of the tree.
   * @param root the node to be root.
   */
  public void setStartNode(WorldNode root) {
    root.setValue(0);
    this.startNode = root;
    nodes.add(root);
  }
}
