package edu.brown.cs.ilayzer.maps.executables;

import edu.brown.cs.ilayzer.algorithms.Dijkstra;
import edu.brown.cs.ilayzer.comparators.AStarComparator;
import edu.brown.cs.ilayzer.distance.Haversine;
import edu.brown.cs.ilayzer.repl.Executable;
import edu.brown.cs.ilayzer.maps.world.World;
import edu.brown.cs.ilayzer.maps.world.WorldEdge;
import edu.brown.cs.ilayzer.maps.world.WorldGraph;
import edu.brown.cs.ilayzer.maps.world.WorldNode;

import java.sql.SQLException;
import java.util.List;

/**
 * Class to connect two nodes.
 */
public class Route implements Executable {

  private World world;

  /**
   * Constructor for route class.
   * @param world the world the route is generated in, and sets the graph in
   *              the world
   */
  public Route(World world) {
    this.world = world;
    world.setGraph(new WorldGraph(world));
  }

  /**
   * TIMDB's generic repl method. Connects two actors in a database.
   *
   * @param args The array of strings used as arguments to this process.
   * @return the String which corresponds to the output in the gui. Also
   * prints to terminal
   */
  @Override
  public String execute(String[] args) {
    //check if theres a database present
    world.getGraph().clearGraph();
    boolean coordinates = true;
    if (world.getDatabase() == null) {
      return ("ERROR: must load a database before using 'route' command");
    }
    int numArgs = args.length;
    if (numArgs != 5) {
      return ("ERROR: command 'route' must have 5 arguments, in the form: "
              + "\"route <lat1> <lon1> <lat2> <lon2>\" OR in the form \"route "
              + "\"Street 1\" "
              + "\"Cross-street 1\" \"Street 2\" \"Cross-street 2\"\"");
    }
    //check if coordinates
    if (args[1].contains("\"")) {
      coordinates = false;
    }

    try {
      if (coordinates) {
        //check that coords are valid doubles
        double lat1 = Double.parseDouble(args[1]);
        double lon1 = Double.parseDouble(args[2]);
        double lat2 = Double.parseDouble(args[3]);
        double lon2 = Double.parseDouble(args[4]);
        WorldNode start = getNearest(lat1, lon1);
        WorldNode end = getNearest(lat2, lon2);
        //checks that nodes exist
        if (start == null || end == null) {
          return ("ERROR: the world is empty");
        } else if (start.getId().equals(end.getId())) {
          return ("You are already at your destination!");
        } else {
          world.setGraphRoot(start);
          Dijkstra<WorldNode, WorldEdge> astar =
                  new Dijkstra<>(world.getGraph(),
                          new AStarComparator<>(end,
                          new Haversine<>(world.getRadius())));
          WorldNode targetNode = astar.search(end.getId());
          if (targetNode == null) {
            return (start.getId() + " -/- " + end.getId());
          } else {
            return getPathOutput(targetNode);
          }
        }
      } else {
        String idFrom =
            world.getDatabase().validCrossStreets(args[1].replace("\"", ""),
                args[2].replace("\"", ""));
        String idTo = world.getDatabase().validCrossStreets(args[3].replace(
            "\"", ""), args[4].replace("\"", ""));
        if (idFrom == null) {
          return ("ERROR: origin cross streets don't intersect, do not exist in "
            + " the database, or are not inputted with quotes.");
        } else if (idTo == null) {
          return ("ERROR: destination cross streets don't intersect, do not exist in "
              + " the database, or are not inputted with quotes.");
        } else if (idFrom.equals(idTo)) {
          return ("You are already at your destination!");
        } else {
          world.setGraphRoot(world.getDatabase().findNode(idFrom));
          Dijkstra<WorldNode, WorldEdge> astar =
                  new Dijkstra<>(world.getGraph(),
                          new AStarComparator<>(
                                  world.getDatabase().findNode(idTo),
                                  new Haversine<>(world.getRadius())));
          WorldNode targetNode = astar.search(idTo);
          if (targetNode == null) {
            return (idFrom + " -/- " + idTo);
          } else {
            return getPathOutput(targetNode);
          }
        }
      }
    } catch (NumberFormatException e) {
      return ("ERROR: arguments must be valid lat and lon coordinates");
    } catch (SQLException e) {
      return "ERROR: error querying the database";
    } catch (Exception e) {
      return ("ERROR: Unknown error occured");
    }

  }

  /**
   * Helper method to move through the calculated shortest path in execute
   * and return the output in a convenient format. Only happens if there
   * exists a shortest path.
   *
   * @param curr the current (target) node
   * @return the string of the path to be outputted
   */
  public String getPathOutput(WorldNode curr) {
    WorldNode prev = curr.getPrev();
    String wayId = curr.getPrevWayID();
    if (prev.getPrev() == null) {
      return (prev.getId() + " -> " + curr.getId() + " : " + wayId);
    }
    if (prev != null) {
      return (getPathOutput(prev) + "\n" + prev.getId() + " -> " + curr.getId() +  " : " + wayId);
    } else {
      return "";
    }
  }

  /**
   * Method to get nearest node to coordinates.
   *
   * @param lat latitude
   * @param lon longitude
   * @return the node closest to coordinates
   */
  public WorldNode getNearest(double lat, double lon) {
    List<WorldNode> list =
            world.getTree().nearestNeighbors(new WorldNode(new double[]{lat, lon}), 1);
    if (list.size() == 0) {
      return null;
    }
    return list.get(0);
  }
}
