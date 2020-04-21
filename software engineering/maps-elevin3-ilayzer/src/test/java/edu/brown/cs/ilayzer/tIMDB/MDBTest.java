package edu.brown.cs.ilayzer.tIMDB;

import edu.brown.cs.ilayzer.tIMDb.executables.Connect;
import edu.brown.cs.ilayzer.tIMDb.executables.MDB;
import org.junit.Test;


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
