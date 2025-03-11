package cs3500.pawnsboard.model.exceptions;

/**
 * Exception thrown when there is an issue with a deck configuration file,
 * such as invalid file format, missing cards, or file not found.
 */
public class InvalidDeckConfigurationException extends Exception {
  
  /**
   * Constructs an InvalidDeckConfigurationException with the specified message.
   *
   * @param message the error message
   */
  public InvalidDeckConfigurationException(String message) {
    super(message);
  }
}
