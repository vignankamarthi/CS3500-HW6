package cs3500.pawnsboard.player;

import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import cs3500.pawnsboard.view.PawnsBoardTextualView;

/**
 * Implementation of Player interface for human players in the PawnsBoard game.
 * This class represents a human player who provides input through a controller
 * to interact with the game model. It keeps track of the player's color and
 * provides methods to make moves and receive feedback.
 *
 * @param <C> the type of Card used in the game
 */
public class HumanPlayer<C extends Card> implements Player<C> {
  private final PlayerColors playerColor;
  private boolean receivedInvalidMove;
  private String lastErrorMessage;
  
  /**
   * Constructs a human player with the specified color.
   *
   * @param playerColor the color (RED or BLUE) assigned to this player
   * @throws IllegalArgumentException if playerColor is null
   */
  public HumanPlayer(PlayerColors playerColor) {
    if (playerColor == null) {
      throw new IllegalArgumentException("Player color cannot be null");
    }
    this.playerColor = playerColor;
    this.receivedInvalidMove = false;
    this.lastErrorMessage = "";
  }
  
  /**
   * Takes a turn in the game based on human input provided through a controller.
   * This method doesn't directly take input but is called by the controller
   * after gathering the player's decision.
   * 
   * Note: For human players, the actual move decisions are managed by a controller,
   * which will call either placeCard() or passTurn() based on user input.
   * This method is primarily for synchronization with the AI implementation.
   *
   * @param model the current state of the game
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if it's not this player's turn
   */
  @Override
  public void takeTurn(PawnsBoard<C, ?> model) throws IllegalStateException, IllegalOwnerException {
    try {
      // For human players, this is a no-operation since the controller manages input
      // and directly calls placeCard or passTurn based on player decisions
      if (!isMyTurn(model)) {
        throw new IllegalOwnerException("Not " + playerColor + "'s turn");
      }
      
      // Reset invalid move flag at start of turn
      receivedInvalidMove = false;
    } catch (IllegalStateException | IllegalOwnerException e) {
      // Automatically capture the error
      receiveInvalidMoveMessage(e.getMessage());
      // Re-throw to allow the controller to handle it if needed
      throw e;
    }
  }
  
  /**
   * Places a card from the player's hand onto the board.
   * This method is called by the controller based on the player's selection.
   *
   * @param model the current state of the game
   * @param cardIndex the index of the card in the player's hand
   * @param row the row where the card should be placed
   * @param col the column where the card should be placed
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalAccessException if the cell doesn't have enough pawns for the card
   * @throws IllegalOwnerException if the pawns in the cell aren't owned by this player
   * @throws IllegalCardException if the card index is invalid
   */
  @Override
  public void placeCard(PawnsBoard<C, ?> model, int cardIndex, int row, int col)
          throws IllegalStateException, IllegalAccessException, IllegalOwnerException,
          IllegalCardException {
    try {
      // Check if it's this player's turn
      if (!isMyTurn(model)) {
        throw new IllegalOwnerException("Not " + playerColor + "'s turn");
      }
      
      // Delegate to model to place the card
      model.placeCard(cardIndex, row, col);
      
      // Reset invalid move flag after successful move
      receivedInvalidMove = false;
    } catch (IllegalStateException | IllegalAccessException | 
             IllegalOwnerException | IllegalCardException e) {
      // Automatically capture the error
      receiveInvalidMoveMessage(e.getMessage());
      // Re-throw to allow the controller to handle it if needed
      throw e;
    }
  }
  
  /**
   * Passes the player's turn.
   * This method is called by the controller when the player chooses to pass.
   *
   * @param model the current state of the game
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if it's not this player's turn
   */
  @Override
  public void passTurn(PawnsBoard<C, ?> model) throws IllegalStateException, IllegalOwnerException {
    try {
      // Check if it's this player's turn
      if (!isMyTurn(model)) {
        throw new IllegalOwnerException("Not " + playerColor + "'s turn");
      }
      
      // Delegate to model to pass the turn
      model.passTurn();
      
      // Reset invalid move flag after successful pass
      receivedInvalidMove = false;
    } catch (IllegalStateException | IllegalOwnerException e) {
      // Automatically capture the error
      receiveInvalidMoveMessage(e.getMessage());
      // Re-throw to allow the controller to handle it if needed
      throw e;
    }
  }
  
  /**
   * Gets the color (RED or BLUE) associated with this player.
   *
   * @return the player's color
   */
  @Override
  public PlayerColors getPlayerColor() {
    return playerColor;
  }
  
  /**
   * Checks if it's this player's turn.
   *
   * @param model the current state of the game
   * @return true if it's this player's turn, false otherwise
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public boolean isMyTurn(PawnsBoard<C, ?> model) throws IllegalStateException {
    return model.getCurrentPlayer() == playerColor;
  }
  
  /**
   * Provides feedback to the player about an invalid move.
   * For human players, this stores the error message for the controller
   * to retrieve and display to the user.
   *
   * @param message the error message describing why the move was invalid
   */
  @Override
  public void receiveInvalidMoveMessage(String message) {
    this.receivedInvalidMove = true;
    this.lastErrorMessage = message;
  }
  
  /**
   * Notifies the player that the game has ended.
   * For human players, this information would be displayed by the controller.
   *
   * @param model the final state of the game
   * @param isWinner true if this player won, false if they lost or tied
   */
  @Override
  public void notifyGameEnd(PawnsBoard<C, ?> model, boolean isWinner) {
    // For human players, the controller will handle displaying game end information
    // This method provides a hook for that functionality
  }
  
  /**
   * Checks if the player has received an invalid move message.
   *
   * @return true if the player's last move attempt was invalid
   */
  public boolean hasReceivedInvalidMove() {
    return receivedInvalidMove;
  }
  
  /**
   * Gets the last error message received for an invalid move.
   *
   * @return the error message, or an empty string if no error occurred
   */
  public String getLastErrorMessage() {
    return lastErrorMessage;
  }
  
  /**
   * Clears the invalid move state and error message.
   * Called by the controller after displaying the error message to the user.
   */
  public void clearInvalidMoveState() {
    this.receivedInvalidMove = false;
    this.lastErrorMessage = "";
  }
  
  /**
   * Returns a string representation of the human player.
   *
   * @return a string describing the player and their color
   */
  @Override
  public String toString() {
    return "Human Player (" + playerColor + ")";
  }
}