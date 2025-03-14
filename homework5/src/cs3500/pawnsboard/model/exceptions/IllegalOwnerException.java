package cs3500.pawnsboard.model.exceptions;

import cs3500.pawnsboard.model.PawnsBoard;

/**
 * Exception thrown when a player attempts to interact with or modify game elements
 * that belong to another player or that they don't have ownership rights to.
 * Used in {@link PawnsBoard#placeCard} and {@link PawnsBoard#passTurn}.
 */
public class IllegalOwnerException extends Exception {
  
  /**
   * Constructs an IllegalOwnerException with the specified message.
   *
   * @param message the error message
   */
  public IllegalOwnerException(String message) {
    super(message);
  }
}
