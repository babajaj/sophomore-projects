package edu.brown.cs.ilayzer.maps.executables;

import edu.brown.cs.ilayzer.repl.Executable;
import edu.brown.cs.ilayzer.maps.world.World;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Class for the Ways executable.
 */
public class Ways implements Executable {
  private World world;

  /**
   * Constructor for the class handling the ways command.
   * @param world the world with database and kd tree and information
   */
  public Ways(World world) {
    this.world = world;
  }

  @Override
  public String execute(String[] args) {
    // error check
    if (args.length != 5) {
      return "ERROR: ways takes 4 arguments. Usage: ways <lat1> <lon1> <lat2> <lon2>.";
    }
    if (world.getDatabase() == null) {
      return "ERROR: must connect map data before using ways.";
    }
    try {
      // parse coordinates
      double lat1 = Double.parseDouble(args[1]);
      double lon1 = Double.parseDouble(args[2]);
      double lat2 = Double.parseDouble(args[3]);
      double lon2 = Double.parseDouble(args[4]);
      // check that coordinates are in range
      if (!world.areValidCoordinates(lat1, lon1) || !world.areValidCoordinates(lat2, lon2)) {
        return "ERROR: one of the border box point's latitude or longitude "
            + "is out of range latitude: (-90,90), longitude: (-180, 180).";
      }
      // check that the points form valid border box corners
      if (!areValidCorners(lat1, lon1, lat2, lon2)) {
        return "ERROR: the first point (lat1, lon1) must be to the north-west "
            + "of the second point (lat2, lon2).";
      }
      // get way ids within bounding box from database
      List<String> wayIds =
          world.getDatabase().getWayIdsWithinBoundingBox(lat1, lon1, lat2, lon2);
      Collections.sort(wayIds);
      return String.join("\n", wayIds);
    } catch (NumberFormatException nfe) {
      return "ERROR: must input numbers for latitude and longitude.";
    } catch (SQLException se) {
      return "ERROR: error accessing database.";
    }
  }

  /**
   * Check that the point (lat1, lon1) is the the northwest of the point (lat2, lon2).
   * @param lat1 the latitude of point 1
   * @param lon1 the longitude of point 1
   * @param lat2 the latitude of point 2
   * @param lon2 the longitude of point 2
   * @return whether point 1 is to the northwest of point2
   */
  private boolean areValidCorners(double lat1, double lon1, double lat2, double lon2) {
    return lat1 > lat2 && lon1 < lon2;
  }
}
