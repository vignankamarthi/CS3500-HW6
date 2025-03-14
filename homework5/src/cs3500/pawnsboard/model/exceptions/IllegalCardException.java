package cs3500.pawnsboard.model.exceptions;

import cs3500.pawnsboard.model.PawnsBoard;

/**
 * Exception thrown when an operation involving a card cannot be completed
 * due to invalid card state, unavailability, or other card-related constraints.
 * Used in {@link PawnsBoard#placeCard}.
 */
public class IllegalCardException extends Exception {
  
  /**
   * Constructs an IllegalCardException with the specified message.
   *
   * @param message the error message
   */
  public IllegalCardException(String message) {
    super(message);
  }
}
