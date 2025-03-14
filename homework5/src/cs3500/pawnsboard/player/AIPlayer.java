package cs3500.pawnsboard.player;

import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;

/**
 * Stub implementation of the Player interface for AI players in the PawnsBoard game.
 * This class represents a computer player that will make decisions automatically.
 * The actual decision-making logic will be implemented in future assignments.
 *
 * @param <C> the type of Card used in the game
 */
public class AIPlayer<C extends Card> implements Player<C> {
  private final PlayerColors playerColor;
  
  /**
   * Constructs an AI player with the specified color.
   *
   * @param playerColor the color (RED or BLUE) assigned to this player
   * @throws IllegalArgumentException if playerColor is null
   */
  public AIPlayer(PlayerColors playerColor) {
    if (playerColor == null) {
      throw new IllegalArgumentException("Player color cannot be null");
    }
    this.playerColor = playerColor;
  }
  
  /**
   * Takes a turn in the game based on AI decision-making.
   * This is a stub method that will be implemented in a future assignment.
   *
   * @param model the current state of the game
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if it's not this player's turn
   */
  @Override
  public void takeTurn(PawnsBoard<C, ?> model) throws IllegalStateException, IllegalOwnerException {
    // TODO: Implement AI decision-making logic
    try {
      if (!isMyTurn(model)) {
        throw new IllegalOwnerException("Not " + playerColor + "'s turn");
      }
      
      // TODO: Decide whether to place a card or pass
      // TODO: If placing a card, choose which card and where to place it
    } catch (IllegalStateException | IllegalOwnerException e) {
      // Automatically capture the error
      receiveInvalidMoveMessage(e.getMessage());
      // Re-throw to allow the controller to handle it if needed
      throw e;
    }
  }
  
  /**
   * Places a card from the player's hand onto the board.
   * This is a stub method that will be implemented in a future assignment.
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
    // TODO: Implement card placement for AI
    try {
      if (!isMyTurn(model)) {
        throw new IllegalOwnerException("Not " + playerColor + "'s turn");
      }
      
      // delegate to the model
      model.placeCard(cardIndex, row, col);
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
   * This is a stub method that will be implemented in a future assignment.
   *
   * @param model the current state of the game
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if it's not this player's turn
   */
  @Override
  public void passTurn(PawnsBoard<C, ?> model) throws IllegalStateException, IllegalOwnerException {
    // TODO: Implement pass turn logic for AI
    try {
      if (!isMyTurn(model)) {
        throw new IllegalOwnerException("Not " + playerColor + "'s turn");
      }
      
      // delegate to the model
      model.passTurn();
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
   * Provides feedback to the AI about an invalid move.
   * This is a stub method that will be implemented in a future assignment.
   *
   * @param message the error message describing why the move was invalid
   */
  @Override
  public void receiveInvalidMoveMessage(String message) {
    // TODO: Implement AI learning from invalid moves
  }
  
  /**
   * Notifies the AI that the game has ended.
   * This is a stub method that will be implemented in a future assignment.
   *
   * @param model the final state of the game
   * @param isWinner true if this player won, false if they lost or tied
   */
  @Override
  public void notifyGameEnd(PawnsBoard<C, ?> model, boolean isWinner) {
    // TODO: Implement AI game end handling
  }
  
  /**
   * Returns a string representation of the AI player.
   *
   * @return a string describing the player and their color
   */
  @Override
  public String toString() {
    return "AI Player (" + playerColor + ")";
  }
}