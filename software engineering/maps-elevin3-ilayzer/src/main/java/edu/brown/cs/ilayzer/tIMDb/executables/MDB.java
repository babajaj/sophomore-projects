package edu.brown.cs.ilayzer.tIMDb.executables;

import edu.brown.cs.ilayzer.repl.Executable;
import edu.brown.cs.ilayzer.tIMDb.actors.ActorDatabase;

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
      return "ERROR: mdb takes one argument";
    }
    ActorDatabase database;
    try {
      //check if file exists
      File tempFile = new File(args[1]);
      if (tempFile.exists() && !tempFile.isDirectory()) {
        database = new ActorDatabase(args[1]);
        //create database and give connect object access to it
        connect.setDatabase(database);
        return "db set to " + args[1];
      } else {
        return "ERROR: " + args[1] + " is not a file";
      }
    } catch (Exception e) {
      return "ERROR: database not found";
    }
  }
}
