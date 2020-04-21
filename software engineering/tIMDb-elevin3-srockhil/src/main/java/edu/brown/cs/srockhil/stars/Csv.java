package edu.brown.cs.srockhil.stars;


import edu.brown.cs.srockhil.KDTree.KDTree;
import edu.brown.cs.srockhil.KDTree.TreeNode;
import edu.brown.cs.srockhil.executable.Executable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

/**
 * This object parses through Csv files of stars, checking
 * if they are valid and giving their data to a KD tree.
 */
public class Csv implements Executable {


  private StarsManager manager;

  /**
   * Stores a StarsManager which will hold the KD tree.
   *
   * @param manager StarsManager that holds tree.
   */
  public Csv(StarsManager manager) {
    this.manager = manager;
  }

  @Override
  public String execute(String[] args) {
    int numArgs = args.length;
    if (numArgs != 2) {
      //checks for correct number of arguments
      System.out.println("ERROR: invalid arguments. Usage: stars <filename>");
      return null;
    }
    String path = args[1];
    File file = new File(path);
    //creates a buffered reader to read from terminal
    BufferedReader reader = null;
    String line = null;
    String[] data = null;
    int numLines = 0;

    //first reads the header of the file if it exists.
    try {
      reader = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: file not found.");
      return null;
    }
    try {
      line = reader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    //the header must be of the proper format
    if (!line.equals("StarID,ProperName,X,Y,Z")) {
      System.out.println("ERROR: invalid file.");
      return null;
    }
    //if it is a valid file, creates a new tree to accept the data that will be read.
    KDTree<Star, Comparator<TreeNode<Star>>> tree = manager.newTree();

    //reads lines until EOF
    while (true) {
      try {
        line = reader.readLine();
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
      //EOF
      if (line == null) {
        break;
      }
      //keeps track of number of lines (stars) read
      numLines++;
      //splits the string up into individual strings representing star attributes
      data = line.split(",");
      if (data.length != 5) {
        //makes sure there are 5 attributes.
        System.out.println("ERROR: invalid file");
        return null;
      }
      //creates a star object
      Star star = new Star();
      //adds data to the object, returns if data is invalid
      if (star.addData(data) == -1) {
        return null;
      }
      tree.acceptData(star);
    }
    System.out.println("Read " + numLines + " stars from " + path);
    tree.buildTree();
    return null;
  }

}
