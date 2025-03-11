package cs3500.pawnsboard.model;

import java.util.List;

import cs3500.pawnsboard.model.card.Card;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;

/**
 * This interface represents the behaviors for the Pawns Board game.
 * It provides the functionality needed to play the game including initializing the game,
 * making player moves, retrieving game state, and determining the winner.
 *
 * <p>The Pawns Board game is a two-player card game played on a rectangular board
 * where players place cards from their hand onto cells with their pawns.
 * Cards influence adjacent cells based on their influence pattern.</p>
 *
 * <p>Documented INVARIANT: No cell ever contains more than 3 pawns. This is enforced during
 * initialization and maintained throughout the game when influence is applied.</p>
 */
public interface PawnsBoard {

  /**
   * Initializes and starts a new game with the specified parameters.
   * Sets up the board with rows and columns, initializes player decks from the deck config file,
   * deals cards to each player's hand, and sets Red as the first player.
   * The starting board has a single pawn in each cell of the first and last columns,
   * belonging to the Red and Blue players respectively.
   *
   * <p>INVARIANT: When placing initial pawns, no cell will ever exceed the maximum of 3 pawns.</p>
   *
   * @param rows the number of rows on the board (must be positive)
   * @param cols the number of columns on the board (must be odd and greater than 1)
   * @param deckConfigPath path to the deck configuration file
   * @param startingHandSize the number of cards each player starts with (cannot exceed 1/3 of deck size using integer division)
   * @throws IllegalArgumentException if dimensions are invalid, deck configuration is invalid,
   *         or starting hand size is too large
   */
  void startGame(int rows, int cols, String deckConfigPath, int startingHandSize) 
      throws IllegalArgumentException;

  /**
   * Checks if the game has ended. The game ends when both players pass their turn in succession.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Gets the current player whose turn it is.
   *
   * @return the current player: RED or BLUE
   * @throws IllegalStateException if the game hasn't been started
   */
  Player getCurrentPlayer() throws IllegalStateException;

  /**
   * The current player draws a card from their deck. This happens automatically at the start of a
   * turn.
   * If there are no cards left in the deck, no card is drawn.
   *
   * @throws IllegalStateException if the game hasn't been started or is already over
   */
  void drawCard() throws IllegalStateException;

  /**
   * Places a card from the current player's hand onto the specified cell.
   * The cell must contain enough pawns owned by the current player to cover the card's cost.
   * After placement, the card's influence will be applied to the board.
   *
   * <p>INVARIANT: When applying card influence, no cell will ever exceed the maximum of
   * 3 pawns.</p>
   *
   * @param cardIndex the index of the card in the current player's hand
   * @param row the row index where the card will be placed
   * @param col the column index where the card will be placed
   * @throws IllegalArgumentException if the row or column is out of bounds
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalAccessException if the cell doesn't contain enough pawns to cover the card's
   *                                cost
   * @throws IllegalOwnerException if the pawns in the cell aren't owned by the current player
   * @throws IllegalCardException if the card is not in the current player's hand
   */
  void placeCard(int cardIndex, int row, int col)
      throws IllegalArgumentException, IllegalStateException, IllegalAccessException,
      IllegalOwnerException, IllegalCardException;

  /**
   * The current player passes their turn. Control passes to the other player.
   * If both players pass consecutively, the game ends.
   *
   * @throws IllegalStateException if the game hasn't been started or is already over
   */
  void passTurn() throws IllegalStateException;

  /**
   * Gets the dimensions of the board as a 2-element array.
   *
   * @return an array where the first element is the number of rows and the second is the
   *         number of columns
   * @throws IllegalStateException if the game hasn't been initialized
   */
  int[] getBoardDimensions() throws IllegalStateException;

  /**
   * Gets the content of a cell on the board.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a CellContent enum indicating whether the cell is empty, contains pawns, or a card
   * @throws IllegalArgumentException if the row or column is out of bounds
   * @throws IllegalStateException if the game hasn't been initialized
   */
  CellContent getCellContent(int row, int col)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the owner of a cell's contents (pawns or card). If the cell is empty, returns null.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the Player who owns the cell's contents, or null if the cell is empty
   * @throws IllegalArgumentException if the row or column is out of bounds
   * @throws IllegalStateException if the game hasn't been initialized
   */
  Player getCellOwner(int row, int col)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the number of pawns in a specified cell.
   *
   * <p>INVARIANT: This method will never return a value greater than 3, as the game
   * enforces a maximum of 3 pawns per cell.</p>
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the number of pawns in the cell, or 0 if the cell is empty or contains a card
   * @throws IllegalArgumentException if the row or column is out of bounds
   * @throws IllegalStateException if the game hasn't been initialized
   */
  int getPawnCount(int row, int col)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the cards in the specified player's hand.
   *
   * @param player the player whose hand to retrieve
   * @return a list of Card objects representing the player's hand
   * @throws IllegalStateException if the game hasn't been started
   */
  List<Card> getPlayerHand(Player player) throws IllegalStateException;

  /**
   * Gets the number of cards remaining in the specified player's deck.
   *
   * @param player the player whose deck size to retrieve
   * @return the number of cards left in the player's deck
   * @throws IllegalStateException if the game hasn't been started
   */
  int getRemainingDeckSize(Player player) throws IllegalStateException;



  /**
   * Gets the total score for each player across all rows.
   * For each row, the player with the higher row score adds that score to their total.
   * If row scores are tied, neither player gets points.
   *
   * @return an array where the first element is Red's total score and the second is Blue's
   * @throws IllegalStateException if the game hasn't been initialized
   */
  int[] getTotalScore() throws IllegalStateException;

  /**
   * Gets the winning player if the game is over.
   *
   * @return the winning Player (RED or BLUE), null if the game is tied or not over yet
   * @throws IllegalStateException if the game hasn't been started
   */
  Player getWinner() throws IllegalStateException;

  /**
   * Gets the hand of the current player.
   *
   * @return a list of cards in the current player's hand
   * @throws IllegalStateException if the game hasn't been started
   */
  List<Card> getHand() throws IllegalStateException;

  /**
   * Removes the specified card from the current player's hand after it has been placed.
   *
   * @param cardIndex the index of the card to discard from the current player's hand
   * @throws IllegalCardException if the card is not in the current player's hand
   * @throws IllegalStateException if the game hasn't been started or is already over
   */
  void discardCard(int cardIndex) throws IllegalCardException, IllegalStateException;
}
