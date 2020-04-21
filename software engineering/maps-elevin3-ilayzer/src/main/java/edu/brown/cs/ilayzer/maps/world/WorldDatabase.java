package edu.brown.cs.ilayzer.maps.world;

import edu.brown.cs.ilayzer.distance.Haversine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class that connects to a database pertaining to the maps project and provides
 * useful functions to access the data contained in that database.
 */
public class WorldDatabase {
  private HashMap<String, HashMap<String, List<String>>> tableMap;
  private static final int CLEAN = 100000;
  private static final double RADIUS = 6371.0;
  private static Connection conn;
  private Map<String, WorldNode> idToNode = new HashMap<>();
  private Map<WorldNode, List<WorldEdge>> nodeToEdges = new HashMap<>();
  private Haversine<WorldNode> haversine = new Haversine<>(RADIUS);

  //node to location
  //way to end/start nodes
  //way id to name
  //way id to type????
  //get every node put into a list with coordinates
  //


  /**
   * Constructor for a WorldDatabase.
   *
   * @param filePath the path to the database file to connect to.
   * @throws SQLException if sql exception
   * @throws ClassNotFoundException if class not found exception
   */
  public WorldDatabase(String filePath)
          throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filePath;
    conn = DriverManager.getConnection(urlToDB);
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }

  /**
   * Method that gets all traversable nodes in the database.
   * @return all the traversable nodes
   * @throws SQLException if there is an sql error
   */
  public List<WorldNode> getAllTraversableNodes() throws SQLException {
    List<WorldNode> allNodes = new ArrayList<>();
    PreparedStatement prep = conn.prepareStatement(
            "SELECT * from node WHERE id IN (SELECT distinct start FROM way\n"
                    + "UNION select distinct end FROM way where NOT (type =\n"
                    + "'unspecified' OR type = ''))");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      // get coordinates
      double latitude = (rs.getDouble(2));
      double longitude = (rs.getDouble(3));
      double[] coordinates = new double[]{latitude, longitude};
      WorldNode node = new WorldNode(rs.getString(1),
              true, coordinates);
      allNodes.add(node);
    }
    prep.close();
    rs.close();
    return allNodes;
  }

  /**
   * Method that returns the ids of ways that have at least one endpoint within the
   * bounding box defined by two points.
   *
   * @param lat1 latitude of northwest point
   * @param lon1 longitude of northwest point
   * @param lat2 latitude of southeast point
   * @param lon2 longitude of southeast point
   * @return the set of way ids inside (not inclusive) the bounding box
   * @throws SQLException if there was an sql error
   */
  @SuppressWarnings("checkstyle:MagicNumber")
  public List<String> getWayIdsWithinBoundingBox(double lat1, double lon1,
                                                 double lat2, double lon2)
          throws SQLException {
    Set<String> wayIds = new HashSet<>();
    PreparedStatement prep = conn.prepareStatement(
            "SELECT id FROM way WHERE (start IN "
                    +
                    "(SELECT id FROM node WHERE latitude < (?) AND longitude > (?) "
                    + "AND latitude > (?) AND longitude < (?)) OR "
                    +
                    "end IN (SELECT id FROM node WHERE latitude < (?) AND longitude > (?) "
                    + "AND latitude > (?) AND longitude < (?)))");
    prep.setDouble(1, lat1);
    prep.setDouble(2, lon1);
    prep.setDouble(3, lat2);
    prep.setDouble(4, lon2);
    prep.setDouble(5, lat1);
    prep.setDouble(6, lon1);
    prep.setDouble(7, lat2);
    prep.setDouble(8, lon2);
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      wayIds.add(rs.getString(1));
    }
    return new ArrayList<>(wayIds);
  }

  /**
   * Method to check if a street/cross street pair is valid and return the
   * intersection node.
   *
   * @param s  the street
   * @param cs cross-street
   * @return null if not a valid intersection, the node ID otherwise
   * @throws SQLException if there was an sql error
   */
  public String validCrossStreets(String s, String cs)
          throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT start from way "
            + "WHERE name = ? AND "
            + "(start IN (SELECT start from way WHERE name = ?) OR "
            + "(start IN (SELECT end from way WHERE name = ?))) UNION "
            + "SELECT end from way WHERE name = ? AND "
            + "(end IN (SELECT start from way WHERE name = ?) OR "
            + "(end IN (SELECT end from way WHERE name = ?))) LIMIT 1");

    prep.setString(1, s);
    prep.setString(2, cs);
    prep.setString(3, cs);
    prep.setString(4, s);
    prep.setString(5, cs);
    prep.setString(6, cs);
    ResultSet rs = prep.executeQuery();
    //should definitely cache this stuff
    String nodeId = null;
    while (rs.next()) {
      nodeId = rs.getString(1);
    }
    prep.close();
    rs.close();
    return nodeId;
  }

  /**
   * Method to create and return a node based on an query id, also caches the
   * id to node.
   *
   * @param nodeID the id to query on
   * @return the node corresponding to that id
   * @throws SQLException if there was an sql error
   */
  public WorldNode findNode(String nodeID) throws SQLException {
    if (idToNode.size() > 100000) {
      idToNode.clear();
    }
    if (idToNode.containsKey(nodeID)) {
      return idToNode.get(nodeID);
    } else {
      PreparedStatement prep =
              conn.prepareStatement("SELECT id, latitude, longitude from node where id = ?");
      prep.setString(1, nodeID);
      ResultSet rs = prep.executeQuery();
      double[] coords = new double[2];
      while (rs.next()) {
        coords[0] = (rs.getDouble(2));
        coords[1] = (rs.getDouble(3));
      }
      WorldNode node = new WorldNode(nodeID, true, coords);
      idToNode.put(nodeID, node);
      return node;
    }
  }

  /**
   * Gets the ways going away from a node.
   *
   * @param node the node
   * @return all the edges going away from the node
   * @throws SQLException if sql error
   */
  public List<WorldEdge> waysFromNode(WorldNode node) throws SQLException {
    List<WorldEdge> edgesFromNode = new ArrayList<>();
    if (nodeToEdges.size() > 100000) {
      nodeToEdges.clear();
    }
    if (nodeToEdges.containsKey(node)) {
      return nodeToEdges.get(node);
    }
    PreparedStatement prep = conn.prepareStatement("SELECT id, start, end, "
            + "name" + " from way WHERE "
            + "(start = ?) AND NOT (type = 'unspecified' OR type = '')");
    prep.setString(1, node.getId());
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      String id = rs.getString(1);
      String start = rs.getString(2);
      String end = rs.getString(3);
      String name = rs.getString(4);
      WorldNode startNode = findNode(start);
      WorldNode endNode = findNode(end);
      double weight = haversine.distance(startNode, endNode);
      WorldEdge edge = new WorldEdge(weight, id, name, startNode, endNode
      );
//is this allowed
      edgesFromNode.add(edge);
    }
    nodeToEdges.put(node, edgesFromNode);
    return edgesFromNode;
  }

}
