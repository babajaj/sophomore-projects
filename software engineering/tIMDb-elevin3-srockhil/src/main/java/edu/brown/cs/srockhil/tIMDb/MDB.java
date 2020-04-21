package edu.brown.cs.srockhil.tIMDb;

import edu.brown.cs.srockhil.executable.Executable;

import java.io.File;

/**
 * Class to load a movie database from the repl.
 */
public class MDB implements Executable {


  private Connect connect;

  /**
   * constructor for MDB class.
   * @param connect a connect object to connect two actors
   */
  public MDB(Connect connect) {
    this.connect = connect;
  }


  /**
   * Method to execute from the repl. Loads the database
   * @param args The array of strings used as arguments to this process.
   * @return the output of loading the database.
   */
  @Override
  public String execute(String[] args) {
    if (args.length != 2) {
      return giveOutput("ERROR: mdb takes one argument");
    }
    Database database;
    try {
      //check if file exists
      File tempFile = new File(args[1]);
      if (tempFile.exists() && !tempFile.isDirectory()) {
        database = new Database(args[1]);
        //create database and give connect object access to it
        connect.setDatabase(database);
        return giveOutput("db set to " + args[1]);
      } else {
        return giveOutput("ERROR: " + args[1] + " is not a file");
      }
    } catch (Exception e) {
      return giveOutput("ERROR: database not found");
    }
  }

  /**
   * helper method to set, print, and return the output.
   * @param str the string to operate on
   * @return the output
   */
  public String giveOutput(String str) {
    System.out.println(str);
    return str;
  }
}
