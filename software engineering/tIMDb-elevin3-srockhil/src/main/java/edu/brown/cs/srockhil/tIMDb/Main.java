package edu.brown.cs.srockhil.tIMDb;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.srockhil.repl.REPL;
import edu.brown.cs.srockhil.executable.Executable;
import edu.brown.cs.srockhil.stars.Neighbors;
import edu.brown.cs.srockhil.stars.Radius;
import edu.brown.cs.srockhil.stars.StarsManager;
import edu.brown.cs.srockhil.stars.Csv;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private Boolean gui;
  private static Neighbors neighbors;
  private static Radius radius;
  private static StarsManager manager;
  private static Connect connect;
  private static MDB mdb;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    gui = false;
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
            .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      gui = true;
      runSparkServer((int) options.valueOf("port"));
    }

    //add executables to the map of commands
    HashMap<String, Executable> executables = new HashMap<>();
    manager = new StarsManager(gui);
    neighbors = new Neighbors(manager);
    radius = new Radius(manager);
    connect = new Connect();
    mdb = new MDB(connect);
    executables.put("stars", new Csv(manager));
    executables.put("neighbors", neighbors);
    executables.put("radius", radius);
    executables.put("connect", connect);
    executables.put("mdb", mdb);
    //run repl using specific commands in hashmap
    REPL repl = new REPL(executables);
    repl.runRepl();

  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
              templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    //Setup Spark Routes
    Spark.get("/timdb", new TimdbFrontHandler(), freeMarker);
    Spark.get("/stars", new StarsFrontHandler(), freeMarker);
    Spark.get("/connect", new ConnectHandler(), freeMarker);
    Spark.get("/radius", new RadiusHandler(), freeMarker);
    Spark.get("/neighbors", new NeighborsHandler(), freeMarker);
    Spark.get("/actor/:variable", new ActorPageFrontHandler(), freeMarker);
    Spark.get("/movie/:variable", new MoviePageFrontHandler(), freeMarker);
  }

  /**
   * Handle requests to the front page of our timdb website.
   */
  private static class TimdbFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      List<String> emptyList = new ArrayList<>();
      Map<String, Object> variables = ImmutableMap.of("title",
              "tIMDb: connect actors in the database", "connectOutput",
              emptyList, "noConnection", emptyList, "errorMessage", "");

      return new ModelAndView(variables, "timdbQuery.ftl");
    }
  }

  /**
   * Handle requests to the front page of our Stars website.
   */
  private static class StarsFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
              "Stars: Query the database", "outputNeighbor", "",
              "outputRadius",
              "");

      return new ModelAndView(variables, "starsQuery.ftl");
    }
  }


  /**
   * Handles giving output to actor page.
   */
  private static class ActorPageFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String actor = req.params(":variable");
      List<String[]> movies;
      try {
        movies = connect.movieList(actor);
      } catch (Exception e) {
        movies = null;
      }
      String actorName;
      try {
        actorName = connect.getActorName(actor);
      } catch (Exception e) {
        actorName = null;
      }
      Map<String, Object> variables = ImmutableMap.of("title",
              "Actor: " + actorName, "movieList", movies);

      return new ModelAndView(variables, "actorPage.ftl");
    }
  }

  /**
   * Handles giving output to movie page.
   */
  private static class MoviePageFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String movie = req.params(":variable");
      List<String[]> actors;
      try {
        actors = connect.actorList(movie);
      } catch (Exception e) {
        actors = null;
      }
      String movieName;
      try {
        movieName = connect.getMovieName(movie);
      } catch (Exception e) {
        movieName = null;
      }
      Map<String, Object> variables = ImmutableMap.of("title",
              "Movie: " + movieName, "actorList", actors);
      return new ModelAndView(variables, "moviePage.ftl");
    }
  }

  /**
   * Class handles input from textArea on GUI for search command.
   */
  private static class ConnectHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      //get the text
      String actor1 = qm.value("actor1");
      String actor2 = qm.value("actor2");
      //add this to mimic terminal input
      String input = "connect " + actor1 + " " + actor2;
      //split up text into array of strings
      String[] args = input.split("\\s+");
      String output = "";

      if (args.length > 1) {
        output = connect.execute(args);
      }
      //three variables given to the immutable map start out as null
      List<String[]> finalOutput = new ArrayList<>();
      String errorMessage = "";
      List<String[]> noConnection = new ArrayList<>();
      if (output.startsWith("ERROR")) {
        errorMessage = output;

      } else if (output.contains("-/-")) {
        noConnection.add(output.split("-/-"));


      } else if (output.length() != 0) {
        String[] lines = output.split(",,");
        for (String line : lines) {
          finalOutput.add(line.split("--"));
        }
      }

      Map<String, Object> variables =
              ImmutableMap.of("title", "Actor Connection", "connectOutput",
                      finalOutput, "noConnection", noConnection, "errorMessage",
                      errorMessage);
      return new ModelAndView(variables, "timdbQuery.ftl");
    }
  }

  /**
   * Class handles input from textArea on GUI for Radius command.
   */
  private static class RadiusHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      //get the text
      String textFromTextField = qm.value("text");
      //add this to mimic terminal input
      textFromTextField = "radius " + textFromTextField;
      //split up text into array of strings
      String[] args = textFromTextField.split("\\s+");
      String output = "";
      if (args.length > 1) {
        //call execute
        radius.execute(args);
        //the manager will be holding the output in result string
        output = manager.getResult();
      }
      Map<String, Object> variables = ImmutableMap.of("title", "radius",
              "outputNeighbor", "", "outputRadius", output);

      return new ModelAndView(variables, "starsQuery.ftl");
    }
  }

  /**
   * Class handles input from textArea on GUI for Neighbors command.
   */
  private static class NeighborsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      //same as RadiusHandler above
      QueryParamsMap qm = req.queryMap();
      String textFromTextField = qm.value("text");
      textFromTextField = "neighbors " + textFromTextField;
      String[] args = textFromTextField.split("\\s+");
      String output = "";
      if (args.length > 1) {
        neighbors.execute(args);
        output = manager.getResult();
      }
      Map<String, Object> variables = ImmutableMap.of("title", "neighbors",
              "outputNeighbor", output, "outputRadius", "");
      return new ModelAndView(variables, "starsQuery.ftl");
    }
  }


  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
