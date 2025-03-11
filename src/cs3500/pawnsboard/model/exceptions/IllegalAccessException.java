package cs3500.pawnsboard.model.exceptions;

import cs3500.pawnsboard.model.PawnsBoard;

/**
 * Exception thrown when a game action cannot be performed due to insufficient resources
 * or when requirements for accessing a game element are not met. Used in 
 * {@link PawnsBoard#placeCard}.
 */
public class IllegalAccessException extends Exception {
  
  /**
   * Constructs an IllegalAccessException with the specified message.
   *
   * @param message the error message
   */
  public IllegalAccessException(String message) {
    super(message);
  }
}
