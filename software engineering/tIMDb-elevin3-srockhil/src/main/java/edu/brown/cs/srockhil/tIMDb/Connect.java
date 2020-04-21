package edu.brown.cs.srockhil.tIMDb;


import edu.brown.cs.srockhil.dijkstra.Dijkstra;
import edu.brown.cs.srockhil.executable.Executable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to connect two actors. Functions like an intermediate class which
 * can ties together more generic classes for tIMDB.
 * Has fields output to output to repl and database to work with.
 */
public class Connect implements Executable {

  private String output = "";
  private Database database = null;


  /**
   * sets the database field in the class.
   *
   * @param database the database to set to (database created in MDB)
   */
  public void setDatabase(Database database) {
    this.database = database;
  }

  /**
   * tIMDB's generic repl method. Connects two actors in a database.
   *
   * @param args The array of strings used as arguments to this process.
   * @return the String which corresponds to the output in the gui. Also
   * prints to terminal
   */
  @Override
  public String execute(String[] args) {
    //check if theres a database present
    if (database == null) {
      return giveOutput(
              "ERROR: must load a database before using " + "connect");
    }
    int numArgs = args.length;
    if (numArgs < 3) {
      return giveOutput("ERROR: command must have 2 arguments");
    }
    //combines names into one argument, checks correct number of arguments
    String[] names = this.argParse(args);
    //returns null if there is an error (no quotes, too many arguments)
    if (names == null) {
      return giveOutput("ERROR: arguments must be two names surrounded by"
              + " quotes");
    }
    ActorGraph graph = new ActorGraph(database);
    String[] startIds = new String[2];

    //gets the ids of the start and end actor
    for (int i = 0; i < 2; i++) {
      try {
        startIds[i] = database.query("getID", names[i]).get(0);
      } catch (Exception e) {
        return giveOutput("ERROR: actor \"" + names[i]
                + "\" does not exist in the database");
      }
    }
    //checks if user is connecting actor to itself
    if (startIds[0].equals(startIds[1])) {
      return giveOutput("\"ERROR: Can't connect actors to themselves\"");
    }

    //gives the graph its root node
    graph.setStartNode(startIds[0], names[0]);
    Dijkstra<ActorNode> dijkstra = new Dijkstra<>(graph);
    ActorNode targetNode;

    try {
      targetNode = dijkstra.search(startIds[1]);
    } catch (Exception e) {
      //this should never occur
      return giveOutput("ERROR: unknown error within the database");
    }
    //happens if target node is not found, meaning no connection
    if (targetNode == null) {
      System.out.println(names[0] + " -/- " + names[1]);
      output = startIds[0].replace('/', '$') + "-/-" + names[0] + "-/-"
              + startIds[1].replace('/', '$') + "-/-" + names[1];
    } else {
      output = "";
      getDijkstraOutput(targetNode);
    }
    return output;
  }

  /**
   * Helper method to help set, print, and return output. Mostly to reduce
   * repetitive code for all the error messages.
   *
   * @param str the string to be output
   * @return the output
   */
  public String giveOutput(String str) {
    if (!str.equals("")) {
      System.out.println(str);
    }
    return str;
  }


  /**
   * Helper method to move through the calculated shortest path in execute
   * and return the output in a convenient format. Only happens if there
   * exists a shortest path.
   *
   * @param curr the current (target) node
   */
  public void getDijkstraOutput(ActorNode curr) {
    ActorNode prev = curr.getPrev();
    //recurs back to first node
    if (prev.getPrev() != null) {
      getDijkstraOutput(prev);
    }
    String movie;
    String movieID = curr.getPrevMovieID();
    try {
      movie = database.query("getMovie", movieID).get(0);
    } catch (Exception e) {
      output = "ERROR: unknown error within the database";
      return;
    }
    //prints out connections line by line
    System.out.println(
            prev.getActorName() + " -> " + curr.getActorName() + " : " + movie);
    //this is the output for the gui
    output +=
            prev.getElement().replace('/', '$') + "--" + prev.getActorName()
                    + "--" + curr.getElement().replace('/', '$') + "--"
                    + curr.getActorName() + "--"
                    + movieID.replace('/', '$') + "--" + movie + ",,";

  }

  /**
   * Method to get the actor list corresponding to a movie id. For the gui.
   *
   * @param id the movie id to query the database on
   * @return an array list of string arrays of size 2, where the first entry
   * in the string array is the id of the actor, and the second entry is the
   * name. there should be a string array for every actor in the movie.
   * @throws Exception if the database cannot query correctly
   */
  public List<String[]> actorList(String id) throws Exception {
    id = id.replace('$', '/');
    List<String> query;
    query = database.query("midToAids", id);
    List<String[]> displayList = new ArrayList<>();
    for (String actor : query) {
      String actorName = database.query("aidToName", actor).get(0);
      displayList.add(new String[]{actor.replace('/', '$'), actorName});
    }
    return displayList;
  }

  /**
   * Method to get the movie list an actor is in. for the gui.
   *
   * @param id the actor id to query the database on
   * @return an array list of string arrays of size 2, where the first entry
   * in the string array is the id of the movie, and the second entry is the
   * name. there should be a string array for every movie the actor is in.
   * @throws Exception if the database cannot query correctly
   */
  public List<String[]> movieList(String id) throws Exception {
    id = id.replace('$', '/');
    List<String> query;
    query = database.query("aidToMids", id);
    List<String[]> displayList = new ArrayList<>();
    for (String movie : query) {
      String movieName = database.query("getMovie", movie).get(0);
      displayList.add(new String[]{movie.replace('/', '$'), movieName});
    }
    return displayList;
  }

  /**
   * method to get the movie name based on an id. for gui.
   *
   * @param id the unique id of the movie in the database
   * @return the name of the movie
   * @throws Exception if the database cannot query correctly
   */
  public String getMovieName(String id) throws Exception {
    id = id.replace('$', '/');
    return database.query("getMovie", id).get(0);
  }

  /**
   * method to get the actor name of an id. for gui.
   *
   * @param id the unique actor id
   * @return the name of the actor
   * @throws Exception if the database cannot query correctly
   */
  public String getActorName(String id) throws Exception {
    id = id.replace('$', '/');
    return database.query("aidToName", id).get(0);
  }


  /**
   * Combines names within quotes into single arguments.
   *
   * @param args The arguments to be parsed.
   * @return An array of two strings containing the names of the first and
   * second actors.
   */
  public String[] argParse(String[] args) {
    int numArgs = args.length;
    String[] names = new String[2];
    //counter keeps track of current element in arg array
    int counter = 1;

    //if the first argument starts with quotes
    if (args[counter].startsWith("\"")) {
      names[0] = args[counter];
      counter++;
      //loop through rest of strings until end quote is found or there are none left
      while (counter < numArgs) {
        if (names[0].endsWith("\"")) {
          break;
        }
        //if its just a quote, concat it
        if (args[counter].equals("\"")) {
          names[0] = names[0].concat(args[counter]);
        } else {
          //if its another word, concat it with a space before
          names[0] = names[0].concat(" " + args[counter]);
        }
        counter++;
      }
    } else {    //if the first argument doesnt start with quotes it is invalid
      return null;
    }

    //counter is now set to element containing start of second actors name
    if (numArgs > counter && args[counter].startsWith("\"")) {
      names[1] = args[counter];
      counter++;
      while (counter < numArgs) {
        if (names[1].endsWith("\"")) {
          break;
        }
        if (args[counter].equals("\"")) {
          names[1] = names[1].concat(args[counter]);
        } else {
          names[1] = names[1].concat(" " + args[counter]);
        }
        counter++;
      }
    } else { //if there are no elements left or the second name doesnt start
      // with a quote
      return null;
    }

    //checks if there are more arguments after the two names
    if (counter != numArgs) {
      return null;
    }

    // makes sure final names start and end with quotes
    for (int i = 0; i < 2; i++) {
      if (!names[i].startsWith("\"") || !names[i].endsWith("\"")
              || names[i].length() < 3) {
        return null;
      }
    }


    //then removes quotes from names
    names[0] = names[0].substring(1, names[0].length() - 1);
    names[1] = names[1].substring(1, names[1].length() - 1);
    return names;
  }
}
