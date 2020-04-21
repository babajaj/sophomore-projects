package edu.brown.cs.ilayzer.stars;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to parse csv files.
 */
public final class CSVParser {

  /**
   * Constructor for a CSV parser object.
   */
  private CSVParser() {

  }

  /**
   * Parses a file into a list of rows of string arrays.
   * @param filename the filename of the CSV
   * @return a list of string arrays where each array is a row in csv table
   */
  public static List<String[]> parseFile(String filename) {
    List<String[]> rows = new LinkedList<String[]>();
    try {
      FileReader fr = new FileReader(filename);
      BufferedReader br = new BufferedReader(fr);
      // read in file
      String line;
      while ((line = br.readLine()) != null) {
        // split line and add to row list
        String[] split = line.split(",");
        rows.add(split);
        // read next line in csv file
      }
    } catch (IOException i) {
      return null; // ERROR
    }
    return rows;
  }
}
