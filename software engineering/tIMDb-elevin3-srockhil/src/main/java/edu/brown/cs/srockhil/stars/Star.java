package edu.brown.cs.srockhil.stars;

import edu.brown.cs.srockhil.KDTree.Coordinate;

/**
 * Object that contains x, y, and z coordinates, as well as a name and an ID.
 */
public class Star implements Coordinate {

  private int starID;
  private String name;
  private double[] vals;


  @Override
  public int addData(String[] args) {
    Double x, y, z;
    try {
      //checks if given strings are parsable doubles.
      starID = Integer.parseInt(args[0]);
      x = Double.parseDouble(args[2]);
      y = Double.parseDouble(args[3]);
      z = Double.parseDouble(args[4]);
    } catch (NumberFormatException e) {
      //if not the file with the star data is invalid
      System.out.println("ERROR: invalid file");
      return -1;
    }
    //add coordinate values to an array for easier access.
    name = args[1];
    vals = new double[3];
    vals[0] = x;
    vals[1] = y;
    vals[2] = z;
    return 0;
  }

  @Override
  public double getVal(int axis) {
    return vals[axis];
  }

  @Override
  public String getName() {
    return name;
  }

  /**
   * getter method for star ID.
   *
   * @return star ID.
   */
  public int getID() {
    return starID;
  }

}


