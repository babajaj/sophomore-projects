package edu.brown.cs.elevin3;

import edu.brown.cs.srockhil.tIMDb.Connect;
import edu.brown.cs.srockhil.tIMDb.Database;
import edu.brown.cs.srockhil.tIMDb.MDB;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * class to test MDB class
 */
public class MDBTest {


  /**
   * makes sure mdb outputs the correct things when loaded.
   */
  @Test
  public void mdbTest() {
    MDB mdb = new MDB(new Connect());
    String attempt = mdb.execute(new String[]{"mdb"});
    String result = "ERROR: mdb takes one argument";
    assertEquals(result, attempt);
    attempt = mdb.execute(new String[]{"mdb", "hello"});
    result = "ERROR: hello is not a file";
    assertEquals(result, attempt);
    attempt = mdb.execute(new String[]{"mdb", "data/timdb/timdb.sqlite3"});
    result = "db set to data/timdb/timdb.sqlite3";
    assertEquals(result, attempt);
  }
}
