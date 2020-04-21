package edu.brown.cs.ilayzer.stars;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class CSVParserTest {

  /**
   * Tests the parseFile function in CSVParser
   */
  @Test
  public void testParseFile() {
    // standard case
    List<String[]> actual1 = CSVParser.parseFile("data/stars/one-star.csv");
    List<String[]> expected1 = new ArrayList<String[]>();
    expected1.add(new String[]{"StarID", "ProperName", "X", "Y", "Z"});
    expected1.add(new String[]{"1", "Lonely Star", "5", "-2.24", "10.04"});
    assertArrayEquals(expected1.toArray(), actual1.toArray());
    // empty file case
    List<String[]> actual2 = CSVParser.parseFile("data/stars/empty.csv");
    List<String[]> expected2 = new ArrayList<>();
    assertArrayEquals(expected2.toArray(), actual2.toArray());
  }
}
