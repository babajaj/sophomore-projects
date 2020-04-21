//package edu.brown.cs.ilayzer.tIMDB;
//
//import edu.brown.cs.ilayzer.tIMDb.executables.Connect;
//import edu.brown.cs.ilayzer.tIMDb.actors.ActorDatabase;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//import java.util.List;
//
///**
// * class to test the connect class.
// */
//public class ConnectTest {
//  Connect connect;
//
//
//  /**
//   * creates a connect.
//   */
//  @Before
//  public void setUp() {
//    connect = new Connect();
//  }
//
//  /**
//   * resets the connect.
//   */
//  @After
//  public void tearDown() {
//    connect = null;
//  }
//
//  /**
//   * method to test the connect class. Covers incorrect inputs, and makes
//   * sure querying and caching works.
//   * @throws Exception if the query does not work.
//   */
//  @Test
//  public void testConnect() throws Exception {
//    String fail = connect.execute(new String[]{"connect", "Samuel L. Jackson",
//            "Sylvester " +
//                    "Stallone"});
//    assertEquals(fail, "ERROR: must load a database before using connect");
//    connect.setDatabase(new ActorDatabase("data/timdb/timdb.sqlite3"));
//    String attempt = connect.execute(new String[]{"connect", "Samuel L. " +
//            "Jackson"});
//    assertEquals(attempt, "ERROR: command must have 2 arguments");
//    attempt = connect.execute(new String[]{"connect", "\"Sameeul L. " +
//            "Jackson\"", "\"Sylvester Stallone\""});
//    assertEquals(attempt, "ERROR: actor \"Sameeul L. Jackson\" does not " +
//            "exist in the database");
//    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
//            "Jackson\"", "\"Sylvester Stallonee\""});
//    assertEquals(attempt, "ERROR: actor \"Sylvester Stallonee\" does not " +
//            "exist in the database");
//    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
//            "Jackson\"", "\"Sylvester Stallone\""});
//    String expected =
//        "Samuel L. Jackson -> Jeff Goldblum : The Great White Hype\n"
//    + "Jeff Goldblum -> Gerrit Graham : Terror in the Aisles\n"
//    + "Gerrit Graham -> Griffin Dunne : Terror in the Aisles\n"
//    + "Griffin Dunne -> Donald Sutherland : Terror in the Aisles\n"
//    + "Donald Sutherland -> Sylvester Stallone : Lock Up";
//    assertEquals(expected, attempt);
//    attempt = connect.execute(new String[]{"connect", "Samuel L. " +
//            "Jackson\"", "\"Sylvester Stallone\""});
//    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
//            "quotes");
//    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
//            "Jackson", "\"Sylvester Stallone\""});
//    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
//            "quotes");
//    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
//            "Jackson\"", "Sylvester Stallone\""});
//    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
//            "quotes");
//    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
//            "Jackson\"", "\"Sylvester Stallone"});
//    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
//            "quotes");
//    attempt = connect.execute(new String[]{"connect", "\"Samuel L. " +
//            "Jackson\"", "\"Sylvester Stallone"});
//    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
//            "quotes");
//    attempt = connect.execute(new String[]{"connect", "\"Samuel",
//            "\"Sylvester\""});
//    assertEquals(attempt, "ERROR: arguments must be two names surrounded by " +
//            "quotes");
//    attempt = connect.execute(new String[]{"connect", "\"David S. Howard\"",
//            "\"Sigourney Weaver\""});
//    assertEquals(attempt,
//            "David S. Howard -/- Sigourney Weaver");
//    attempt = connect.getActorName("/m/0g98lwf");
//    assertEquals(attempt, "David S. Howard");
//    attempt = connect.getMovieName("/m/016dj8");
//    assertEquals(attempt, "Men in Black");
//    List<String[]> attemptList = connect.actorList("/m/016dj8");
//    assertEquals(attemptList.get(0)[1], "Will Smith");
//    attemptList = connect.movieList("/m/0f5xn");
//    assertEquals(attemptList.get(0)[1], "Resurrecting the Champ");
//
//
//  }
//
//
//}
