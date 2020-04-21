package edu.brown.cs.ilayzer.repl;

/**
 * This interface represents objects that can be executed.
 */
public interface Executable {
  /**
   * Executable object calls this to begin whatever its main process is.
   *
   * @param args The array of strings used as arguments to this process.
   * @return A string containing the result of execute.
   */
  String execute(String[] args);
}
