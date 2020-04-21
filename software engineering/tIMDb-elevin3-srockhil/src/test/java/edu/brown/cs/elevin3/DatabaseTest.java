package edu.brown.cs.elevin3;

import edu.brown.cs.srockhil.tIMDb.Database;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class to test database class.
 */
public class DatabaseTest {
  private Database database;

  /**
   * Sets up a database with smallTimdb.
   */
  @Before
  public void setUp() throws Exception {
    database = new Database("data/timdb/smallTimdb.sqlite3");
  }

  /**
   * Resets the database to null.
   */
  @After
  public void tearDown() {
    database = null;
  }

  /**
   * method to test querying within a database. Checks the differnet types of
   * queries to make sure they work.
   * @throws Exception when the query is invalid
   */
  @Test
  public void testQuery() throws Exception {
    List<String> id = database.query("getID", "Sylvester " +
            "Stallone");
    assertEquals(id.get(0), "/m/0gn30");
    List<String> movie = database.query("getMovie", "/m/02q3fdr");
    assertEquals(movie.get(0), "Ponyo");
    List<String> noMovie = database.query("getMovie", "not an id");
    assertTrue(noMovie.size() == 0);
    List<String> name = database.query("aidToName", "/m/0gn30");
    assertEquals(name.get(0), "Sylvester Stallone");
    //checking that caching happens
  }


}

