import edu.brown.cs.srockhil.KDTree.TreeNode;
import edu.brown.cs.srockhil.stars.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * class to test stars classes.
 */
public class UnitTest {

  public StarsManager _manager;
  public Csv _csv;
  public Neighbors _neighbors;
  public Radius _radius;


  @Before
  public void setUp() {
    //with storeOutput set to true, all output will be stored in an accessable results string
    _manager = new StarsManager(true);
    _csv = new Csv(_manager);
    _neighbors = new Neighbors(_manager);
    _radius = new Radius(_manager);
  }


  //Tests functionality when CSV file contains invalid data
  @Test
  public void invalidFileCSV() {
    //the tree should never become valid
    String[] args = new String[2];
    args[1] = "data/stars/invalid-body.csv";
    assertFalse(_manager.validTree());
    _csv.execute(args);
    assertFalse(_manager.validTree());
    args[1] = "data/stars/invalid-body-2.csv";
    _csv.execute(args);
    assertFalse(_manager.validTree());
    args[1] = "data/stars/invalid-header.csv";
    _csv.execute(args);
    assertFalse(_manager.validTree());
  }

  //tests functionality when commands are run but no
  // star data has been loaded
  @Test
  public void runCommandNoTree() {
    _neighbors.execute(null);
    assertEquals("ERROR:", _manager.getResult().substring(0, 6));
    _radius.execute(null);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
  }


  //tests functionality when names of stars are multiple words
  @Test
  public void combineStarNamesWithSpaces() {
    _csv.execute(new String[]{"", "data/stars/one-star.csv"});
    String[] args = new String[]{"", "10", "\"Lonely", "Star\""};
    _neighbors.execute(args);
    assertEquals(_manager.getResult(),"");
    //reset args array
    args = new String[]{"", "10", "\"Lonely", "Star\""};
    _radius.execute(args);
    System.out.println(_manager.getResult());
    assertEquals(_manager.getResult(),"");
  }


  //tests functionality when an invalid number of arguments is
  // passed to an executable
  @Test
  public void invalidNumArgs() {
    String[] args = new String[6];
    _neighbors.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
    _radius.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
  }

  //tests functionality when non-negative arguments are negative
  // and when number values are not parsable ints or doubles
  @Test
  public void negativeKandR() {
    String[] args = new String[5];
    args[1] = "-1";
    _neighbors.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
    _radius.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));

    args[1] = "a";
    _neighbors.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
    _radius.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));

  }

  //tests functionality when given coordinates are not numbers
  @Test
  public void badCoordinates() {
    String[] args = new String[]{"", "1", "5.5", "b", "c"};
    _neighbors.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
    _radius.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
  }

  //tests functionality when invalid star name is given
  @Test
  public void nonexistentStar() {
    String[] args = new String[]{"", "5", "FakeStar"};
    _neighbors.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
    _radius.execute(args);
    assertTrue(_manager.getResult().startsWith("ERROR:"));
  }

  //tests that the kd tree created from data is balanced
  @Test
  public void balancedTree() {
    _csv.execute(new String[]{"", "data/stars/tester-stars.csv"});
    TreeNode<Star> root = _manager.getRoot();
    assertEquals(root.getElement().getID(), 8);
    assertEquals(root.getLchild().getElement().getID(), 4);
    assertEquals(root.getRchild().getElement().getID(), 13);
    assertEquals(root.getLchild().getRchild().getLchild().getElement().getID(), 5);
  }

  //tests the output of the neighbors command on small data set
  @Test
  public void neighborsOutput() {
    _csv.execute(new String[]{"", "data/stars/tester-stars.csv"});
    _neighbors.execute(new String[]{"", "5", "0", "0", "0"});
    String[] results = _manager.getResult().split("<br>");
    for (int i = 0; i < 5; i++) {
      assertTrue(results[i].startsWith("Star ID: " + Integer.toString(i)));
    }
    _neighbors.execute(new String[]{"", "5", "\"zero\""});
    results = _manager.getResult().split("<br>");
    for (int i = 0; i < 5; i++) {
      assertTrue(results[i].startsWith("Star ID: " + Integer.toString(i + 1)));
    }
  }

  //tests output of radius command on small data set
  @Test
  public void radiusOutput() {
    _csv.execute(new String[]{"", "data/stars/tester-stars.csv"});
    _radius.execute(new String[]{"", "10", "0", "0", "0"});
    String[] results = _manager.getResult().split("<br>");
    for (int i = 0; i < 6; i++) {
      assertTrue(results[i].startsWith("Star ID: " + Integer.toString(i)));
    }
    _neighbors.execute(new String[]{"", "10", "\"zero\""});
    results = _manager.getResult().split("<br>");
    for (int i = 0; i < 5; i++) {
      assertTrue(results[i].startsWith("Star ID: " + Integer.toString(i + 1)));
    }
  }

  //tests functionality when number of neighbors is 0
  @Test
  public void noNeighbors() {
    _csv.execute(new String[]{"", "data/stars/tester-stars.csv"});
    _neighbors.execute(new String[]{"", "0", "0", "0", "0"});
    String[] results = _manager.getResult().split("<br>");
    assertEquals(results[0], "");
    assertEquals(results.length, 1);
  }

  //tests functionality when radius given is 0
  @Test
  public void zeroRadius() {
    _csv.execute(new String[]{"", "data/stars/tester-stars.csv"});
    _radius.execute(new String[]{"", "0", "0", "0", "0"});
    String[] results = _manager.getResult().split("<br>");
    assertTrue(results[0].startsWith("Star ID: 0"));
    assertEquals(results.length, 1);
  }

}