package edu.brown.cs.ilayzer.tIMDb.actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


/**
 * class for the database in tIMDB.
 */
public class ActorDatabase {

  private static Connection conn = null;
  private HashMap<String, HashMap<String, List<String>>> tableMap;
  private static final int CLEAN = 1000000;

  /**
   * constructor for a database. Sets the tableMap with caches to easily
   * access information loaded from the database, and initializes a
   * connection to the database.
   *
   * @param filename the name of the file to set the database to
   * @throws Exception  if there is an error in the database/loading from
   * the database or if the incorrect driver is used.
   */
  public ActorDatabase(String filename) throws Exception {
    //we have a hashmap of "caches" which are just hashmaps
    tableMap = new HashMap<>();
    //cachefor getting Names from actor ids
    HashMap<String, List<String>> aidToName = new HashMap<>();
    tableMap.put("aidToName", aidToName);
    //cache for getting actor ids for a certain movie
    HashMap<String, List<String>> midToAids = new HashMap<>();
    tableMap.put("midToAids", midToAids);
    //cache for getting movie ids from actor ids
    HashMap<String, List<String>> aidToMids = new HashMap<>();
    tableMap.put("aidToMids", aidToMids);
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }

  /**
   * method to query the database file contained in the database class.
   *
   * @param command the type of query to be run (table to look in)
   * @param value   the value to query by
   * @return the result of the query
   * @throws Exception when query is not valid.
   */
  public List<String> query(String command, String value)
          throws Exception {
    //we clear the caches when they reach a certain size
    cleanup();
    List<String> result = new ArrayList<>();
    String statement = "";
    //if the user wants to get anID from an actor name
    //used at the beginning of the program when users input actor names
    if (command.equals("getID")) {
      PreparedStatement prep = conn.prepareStatement(
              "SELECT id from actor WHERE actor.name = ? LIMIT 1");
      prep.setString(1, value);
      ResultSet rs = prep.executeQuery();

      result.add(rs.getString(1));
      prep.close();
      rs.close();
      //if the user wants to get a movie name from a movie id
      //used at the end when printing out the connection
    } else if (command.equals("getMovie")) {
      PreparedStatement prep = conn.prepareStatement(
              "SELECT name from film WHERE id = ? LIMIT 1");
      prep.setString(1, value);
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
        result.add(rs.getString(1));
      }
      rs.close();
      prep.close();
    } else {
      //these caches/searches are used more often
      //gets the correct cache, checks if the value has a mapping
      //the given command is the same as the name of the desired cache
      List<String> cacheResult = tableMap.get(command).get(value);
      //if there is no cache mapping, query
      if (cacheResult == null) {
        switch (command) {
          case "aidToName":
            statement = "SELECT name from actor WHERE id = ?";
            break;
          case "midToAids":
            statement = "SELECT actor from actor_film WHERE "
                    + "actor_film.film = ?";
            break;
          case "aidToMids":
            statement = "SELECT film from actor_film WHERE "
                    + "actor = ?";
            break;
          default:
            break;
        }

        PreparedStatement prep = conn.prepareStatement(statement);

        prep.setString(1, value);
        ResultSet rs = prep.executeQuery();
        //put the values from the result set into a string
        while (rs.next()) {
          result.add(rs.getString(1));
        }
        //add values into cache
        tableMap.get(command).put(value, result);
        prep.close();
        rs.close();
      } else {
        result = cacheResult;
      }
    }
    return result;
  }

  /**
   * Method to clean up the hashmaps when they overfill.
   */
  public void cleanup() {
    for (HashMap<String, List<String>> map : tableMap.values()) {
      if (map.size() > CLEAN) {
        map.clear();
      }
    }
  }

}
