package edu.brown.cs.ilayzer.stars;

import edu.brown.cs.ilayzer.repl.Executable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StarControlTest {

  StarControl sc;

  @Before
  public void setUp() {
    sc = new StarControl();
    // fill the kd tree
    Executable stars = sc.new StarsCommand();
    String[] toFillTree = new String[] {"stars", "data/stars/ten-star.csv"};
    stars.execute(toFillTree);
  }

  @After
  public void tearDown() {
    sc = null;
  }

  /**
   ** Tests returnQuotedName.
   */
  @Test
  public void testReturnQuotedName() {
    setUp();
    String[] split1 = ("neighbors 5 \"Sol\"").split("\\s");
    String[] split2 = ("neighbors 5 \"Sagittarius B\"").split("\\s");
    String[] split3 = ("neighbors 5 \"Sagittarius Star B\"").split("\\s");
    String[] split4 = ("neighbors 1 0 0 0").split("\\s");
    String[] split5 = ("neighbors 3 \"\"").split("\\s");

    assertEquals("Sol", sc.returnQuotedName(split1));
    assertEquals("Sagittarius B", sc.returnQuotedName(split2));
    assertEquals("Sagittarius Star B", sc.returnQuotedName(split3));
    assertEquals(null, sc.returnQuotedName(split4));
    assertEquals(null, sc.returnQuotedName(split5));
    tearDown();
  }

  /**
   ** Tests the evaluate() method of StarsCommand.
   */
  @Test
  public void testStarsCommand() {
    setUp();
    // get command
    Executable stars = sc.new StarsCommand();

    // should work
    String[] args1 = new String[] {"stars", "data/stars/ten-star.csv"};
    assertEquals("Read 10 stars from data/stars/ten-star.csv",
        stars.execute(args1));
    // too many args
    String[] args2 = new String[] {"stars", "file", "other"};
    assertEquals("ERROR: Stars: incorrect arguments. Usage: stars <filename>",
        stars.execute(args2));
    // too few args (empty)
    String[] args3 = new String[] {};
    assertEquals("ERROR: Stars: incorrect arguments. Usage: stars <filename>",
        stars.execute(args3));
    // incorrect filename
    String[] args4 = new String[] {"stars", "non-existent_file"};
    assertEquals("ERROR: could not read file non-existent_file",
        stars.execute(args4));

    tearDown();
  }

  /**
   ** Tests the evaluate() method of NeighborsCommand.
   */
  @Test
  public void testNeighborsCommand() {
    setUp();

    // get command
    Executable neighbors = sc.new NeighborsCommand();

    // neighbors coordinates
    String[] args1 = new String[] {"neighbors", "10", "0", "0", "0"};
    assertEquals("0\n70667\n71454\n71457\n87666\n118721\n3759\n2\n1\n3",
        neighbors.execute(args1));
    // neighbors star name
    String[] args2 = new String[] {"neighbors", "9", "\"Sol\""};
    assertEquals("70667\n71454\n71457\n87666\n118721\n3759\n2\n1\n3",
        neighbors.execute(args2));
    // neighbors incorrect star name
    String[] args3 = new String[] {"neighbors", "9", "\"wut\""};
    assertEquals("ERROR: neighbors: unknown star proper id: wut",
        neighbors.execute(args3));
    // non integer k
    String[] args4 = new String[] {"neighbors", "k", "\"Sol\""};
    assertEquals("ERROR: neighbors: error parsing k, Usage: neighbors <k>"
        + " <Coordinates | Star Proper Id>",
        neighbors.execute(args4));
    // too many args
    String[] args5 = new String[] {"neighbors", "4", "5", "0", "0", "0", "0"};
    assertEquals("ERROR: neighbors: too many arguments. Usage: neighbors <k>"
        + " <Coordinates | Star Proper Id>",
        neighbors.execute(args5));
    // too few args
    String[] args6 = new String[] {"neighbors", "\"Sol\""};
    assertEquals("ERROR: neighbors: not enough arguments. Usage: neighbors <k>"
            + " <Coordinates | Star Proper Id>",
        neighbors.execute(args6));

    tearDown();
  }

  /**
   ** Tests the evaluate() method of RadiusCommand.
   */
  @Test
  public void testRadiusCommand() {
    setUp();

    // get command
    Executable radius = sc.new RadiusCommand();

    // radius coordinates
    String[] args1 = new String[] {"radius", "10", "0", "0", "0"};
    assertEquals("0\n70667\n71454\n71457\n87666\n118721\n3759",
        radius.execute(args1));
    // radius star name
    String[] args2 = new String[] {"radius", "9", "\"Sol\""};
    assertEquals("70667\n71454\n71457\n87666\n118721\n3759",
        radius.execute(args2));
    // radius incorrect star name
    String[] args3 = new String[] {"radius", "9", "\"wut\""};
    assertEquals("ERROR: radius: unknown star proper id: wut",
        radius.execute(args3));
    // non integer r
    String[] args4 = new String[] {"radius", "k", "\"Sol\""};
    assertEquals("ERROR: radius: error parsing r, Usage: radius <r>"
            + " <Coordinates | Star Proper Id>",
        radius.execute(args4));
    // too many args
    String[] args5 = new String[] {"radius", "4", "5", "4", "0", "0", "0"};
    assertEquals("ERROR: radius: too many arguments. Usage: radius <r>"
            + " <Coordinates | Star Proper Id>",
        radius.execute(args5));
    // too few args
    String[] args6 = new String[] {"radius", "\"Sol\""};
    assertEquals("ERROR: radius: not enough arguments. Usage: radius <r>"
            + " <Coordinates | Star Proper Id>",
        radius.execute(args6));

    tearDown();
  }
}
