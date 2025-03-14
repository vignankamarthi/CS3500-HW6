package cs3500.pawnsboard.player;

import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;

/**
 * Interface for a player type in the PawnsBoard game.
 * Defines the methods needed for a player to interact with the game model,
 * make moves, and manage their state in the game.
 * 
 * @param <C> the type of Card used in the game
 */
public interface Player<C extends Card> {
  
  /**
   * Takes a turn in the game, either by placing a card or passing.
   * This is the main method that drives player interaction with the game.
   * 
   * @param model the current state of the game
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if it's not this player's turn
   */
  void takeTurn(PawnsBoard<C, ?> model) throws IllegalStateException, IllegalOwnerException;
  
  /**
   * Places a card from the player's hand onto the board.
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
  void placeCard(PawnsBoard<C, ?> model, int cardIndex, int row, int col)
          throws IllegalStateException, IllegalAccessException, IllegalOwnerException, 
          IllegalCardException;
  
  /**
   * Passes the player's turn.
   * 
   * @param model the current state of the game
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if it's not this player's turn
   */
  void passTurn(PawnsBoard<C, ?> model) throws IllegalStateException, IllegalOwnerException;
  
  /**
   * Gets the color (RED or BLUE) associated with this player.
   * 
   * @return the player's color
   */
  PlayerColors getPlayerColor();
  
  /**
   * Checks if it's this player's turn.
   * 
   * @param model the current state of the game
   * @return true if it's this player's turn, false otherwise
   * @throws IllegalStateException if the game hasn't been started
   */
  boolean isMyTurn(PawnsBoard<C, ?> model) throws IllegalStateException;
  
  /**
   * Provides feedback to the player about an invalid move.
   * This can be used to display error messages for human players
   * or update AI decision-making.
   * 
   * @param message the error message describing why the move was invalid
   */
  void receiveInvalidMoveMessage(String message);
  
  /**
   * Notifies the player that the game has ended.
   * 
   * @param model the final state of the game
   * @param isWinner true if this player won, false if they lost or tied
   */
  void notifyGameEnd(PawnsBoard<C, ?> model, boolean isWinner);
}