package edu.brown.cs.ilayzer.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Generic Read, Evaluate, Print Loop object.
 * Loops for command line input, interpreting each line.
 * Valid commands are stored in a hashmap, where strings
 * map to executable objects.
 */
public class REPL {

  private HashMap<String, Executable> commandMap;

  /**
   * The constructor takes stores the given HashMap of executables and their
   * string names.
   *
   * @param executables The hashmap mapping strings to executables
   */
  public REPL(HashMap<String, Executable> executables) {
    commandMap = executables;
  }


  /**
   * This method uses a buffered reader to read from standard input,
   * interpreting each input and then looping until EOF is reached.
   */
  public void runRepl() {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    String command = null;
    //infinite loop
    while (true) {
      try {
        command = reader.readLine();
      } catch (IOException e) {
        break;
      }
      //line read will be null if EOF is found
      if (command == null) {
        break;
      }
      //if command is not blank line, interpret the command
      if (command.length() != 0) {
        this.interpret(command);
      } else {
        System.out.println("ERROR: Please enter a command");
      }
    }
  }

  /**
   * Splits the given string up into an array using whitespace as the regex.
   * Calls execute on the executable specified by the command string, using
   * the split string as the arguments.
   *
   * @param command The string to be split
   */
  public void interpret(String command) {
    String[] args = command.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    //do nothing if no characters were inputed
    if (args.length != 0) {
      //if the given string is not a known command, it is invalid.
      if (commandMap.get(args[0]) == null) {
        System.out.println("ERROR: Invalid command.");
      } else {
        String executed = commandMap.get(args[0]).execute(args);
        if (executed != null) {
          System.out.println(executed);
        }
      }
    }
  }
}

