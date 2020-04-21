package edu.brown.cs.ilayzer.maps.executables;

import edu.brown.cs.ilayzer.repl.Executable;
import edu.brown.cs.ilayzer.maps.world.World;
import edu.brown.cs.ilayzer.maps.world.WorldDatabase;
import edu.brown.cs.ilayzer.maps.world.WorldNode;
import edu.brown.cs.ilayzer.kdtree.KDTree;
import java.sql.SQLException;
import java.util.List;

/**
 * A class for the map executable.
 */
public class MapExecutable implements Executable {

  private World world; // pointer to the world this executable operates within

  /**
   * Constructor for the map executable.
   * @param world the database
   */
  public MapExecutable(World world) {
    this.world = world;
  }

  /**
   * Method to execute from the repl. Loads the database
   * @param args The array of strings used as arguments to this process.
   * @return the output of loading the database.
   */
  @Override
  public String execute(String[] args) {
    if (args.length != 2) {
      return "ERROR: map takes one argument. Usage: 'map <path/to/db>'.";
    }
    try {
      String path = args[1];
      // set the world database to a new world database
      world.setDatabase(new WorldDatabase(path));
      // set world tree to new kd tree made from all nodes in database
      List<WorldNode> allNodes = world.getDatabase().getAllTraversableNodes();
      KDTree<WorldNode> tree = new KDTree<>(allNodes.toArray(
          new WorldNode[allNodes.size()]), 2);
      world.setTree(tree);
      return "map set to " + path;
    } catch (SQLException se) {
      return "ERROR: map database file malformed or does not exist.";
    } catch (ClassNotFoundException ce) {
      return "ERROR: map database file not found.";
    }
  }
}
