package edu.brown.cs.srockhil.KDTree;

/**
 * this interface represents objects that have any amount of coordinate values.
 */
public interface Coordinate {

  /**
   * This method adds data to this object. If the data doesnt comply
   * with certain standards and thus it is invalid, method returns -1.
   *
   * @param args Array of strings that are the arguments (fields of the object to be filled in).
   * @return 0 on successful addition of arguments, -1 on invalid arguments.
   */
  int addData(String[] args);


  /**
   * getter method for specific coordinate value.
   * @param axis the relevant axis.
   * @return the double value on that axis.
   */
  double getVal(int axis);

  /**
   * getter method for coordinate name.
   * @return the coordinates name.
   */
  String getName();

}
