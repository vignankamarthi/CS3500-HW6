package cs3500.pawnsboard.view;

/**
 * Interface for views of the Pawns Board game.
 * Defines methods to render the game state visually.
 */
public interface PawnsBoardView {
  
  /**
   * Renders the current state of the game board.
   *
   * @return a string representation of the board
   */
  String toString();
  
  /**
   * Renders a comprehensive view of the game state including current player,
   * board state, and game results if the game is over.
   *
   * @return a complete representation of the game state
   */
  String renderGameState();
  
  /**
   * Renders the game state with a custom message header.
   * Useful for indicating specific events like game start or player actions.
   *
   * @param headerMessage the message to display as a header
   * @return a string with the header and game state
   */
  String renderGameState(String headerMessage);
}
