package edu.brown.cs.elevin3;

import edu.brown.cs.srockhil.tIMDb.Connect;
import edu.brown.cs.srockhil.tIMDb.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

/**
 * class to test the connect class.
 */
public class ConnectTest {
  Connect connect;


  /**
   * creates a connect.
   */
  @Before
  public void setUp() {
    connect = new Connect();
  }

  /**
   * resets the connect.
   */
  @After
  public void tearDown() {
    connect = null;
  }

  /**
   * method to test the connect class. Covers incorrect inputs, and makes
   * sure querying and caching works.
   * @throws Exception if the query does not work.
   */
  @Test
  public void testConnect() throws Exception {
    String fail = connect.execute(new String[]{"connect", "Samuel L. Jackson",
            "Sylvester " +
                    "Stallone"});
    assertEquals(fail, "ERROR: must load a database before using connect");
    connect.setDatabase(new Database("data/timdb/timdb.sqlite3"));
    String attempt = connect.execute(new String[]{"connect", "Samuel L. " +
            "Jackson"});
    assertEquals(attempt, "ERROR: command must have 2 arguments");
    attempt = connect.execute(new String[]{"connect", "\"Sameeul L. " +
            "Jackson\"", "\"Sylvester Stallone\""});
    assertEquals(attempt, "ERROR: actor \"Sameeul L. Jackson\" does not " +
            "exist in the database");
    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
            "Jackson\"", "\"Sylvester Stallonee\""});
    assertEquals(attempt, "ERROR: actor \"Sylvester Stallonee\" does not " +
            "exist in the database");
    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
            "Jackson\"", "\"Sylvester Stallone\""});
    System.out.println(attempt);
    String result =
            "$m$0f5xn--Samuel L. Jackson--$m$0f502--John Travolta--$" +
                    "m$0f4_l--Pulp Fiction,,$m$0f502--John Travolta--$m$0" +
                    "2661h--Tony Shalhoub--$m$02q7fl9--Primary Colors,,$" +
                    "m$02661h--Tony Shalhoub--$m$0gn30--Sylvester Sta" +
                    "llone--$m$016dj8--Men in Black,,";
    System.out.println(result);
    System.out.println(attempt);
    assertEquals(result, attempt);
    attempt = connect.execute(new String[]{"connect", "Samuel L. " +
            "Jackson\"", "\"Sylvester Stallone\""});
    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
            "quotes");
    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
            "Jackson", "\"Sylvester Stallone\""});
    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
            "quotes");
    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
            "Jackson\"", "Sylvester Stallone\""});
    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
            "quotes");
    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
            "Jackson\"", "\"Sylvester Stallone"});
    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
            "quotes");
    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
            "Jackson\"", "\"Sylvester Stallone"});
    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
            "quotes");
    attempt = connect.execute(new String[]{"connect", "\"Samuel",
            "\"Sylvester\""});
    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
            "quotes");
    attempt = connect.execute(new String[]{"connect", "\"David S. Howard\"",
            "\"Sigourney Weaver\""});
    assertEquals(attempt,
            "$m$0g98lwf-/-David S. Howard-/-$m$0h96g-/-Sigourney Weaver");
    attempt = connect.getActorName("/m/0g98lwf");
    assertEquals(attempt, "David S. Howard");
    attempt = connect.getMovieName("/m/016dj8");
    assertEquals(attempt, "Men in Black");
    List<String[]> attemptList = connect.actorList("/m/016dj8");
    assertEquals(attemptList.get(0)[1], "Will Smith");
    attemptList = connect.movieList("/m/0f5xn");
    assertEquals(attemptList.get(0)[1], "Resurrecting the Champ");


  }


}
