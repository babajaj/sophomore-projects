package edu.brown.cs.ilayzer.maps.executables;

import edu.brown.cs.ilayzer.repl.Executable;
import edu.brown.cs.ilayzer.maps.world.World;
import edu.brown.cs.ilayzer.maps.world.WorldNode;

import java.util.List;

/**
 * Class for the "nearest" command.
 */
public class Nearest implements Executable {
  private World world;

  /**
   * Constructor for Nearest executable class.
   * @param world the world.
   */
  public Nearest(World world) {
    this.world = world;
  }

  /**
   * Runs the nearest command on the user input.
   * @param args The array of strings used as arguments to this process.
   * @return
   */
  @Override
  public String execute(String[] args) {
    // Error check
    if (args.length != 3) {
      return "ERROR: nearest: incorrect number of args. Usage: 'nearest <lat1> <lat2>'.";
    }
    if (world.getTree() == null) {
      return "ERROR: must connect map data before using nearest.";
    }
    try {
      double latitude = Double.parseDouble(args[1]);
      double longitude = Double.parseDouble(args[2]);
      // check if coordinates are valid
      if (!world.areValidCoordinates(latitude, longitude)) {
        return "ERROR: inputted latitude or longitude is out of range "
            + "latitude: (-90,90), longitude: (-180, 180).";
      }
      // wrap coordinates to pass into kd trees nearest neighbor method
      double[] targetCoords = new double[]{latitude, longitude};
      WorldNode wrappedTarget = new WorldNode(targetCoords);
      // get first nearest neighbor
      List<WorldNode> nearestNode = world.getTree().nearestNeighbors(wrappedTarget, 1);
      if (!nearestNode.isEmpty()) {
        return nearestNode.get(0).getId();
      } else {
        // the tree was empty (there were no nodes in the map)
        return "";
      }
    } catch (NumberFormatException nfe) {
      return "ERROR: must input numbers for latitude and longitude.";
    }
  }
}
