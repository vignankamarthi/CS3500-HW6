package cs3500.pawnsboard.model;

import java.util.List;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.cell.PawnsBoardCell;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.Player;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;
//TODO: Create a README File for the ENTIRE PROJECT
//TODO: Attempt to create a user-player interface that, in the future, allows for user OR AI player implementations
//TODO: Create a document envisioning how you to implement the user AND AI player models
//TODO: Create document for how I envision instantiating user-player for AI and human player differentiation
/**
 * This interface represents the behaviors for the Pawns Board game.
 * It provides the functionality needed to play the game including initializing the game,
 * making player moves, retrieving game state, and determining the winner.
 *
 * <p>The Pawns Board game is a two-player card game played on a board
 * where players place {@link Card}s from their hand onto cells with their pawns.
 * Cards influence cells based on their influence pattern.</p>
 *
 *
 * <p>Different implementations of this interface may (just examples, list could go on):</p>
 * <ul>
 *   <li>Support different board configurations beyond rectangular</li>
 *   <li>Implement alternative influence mechanics beyond the standard add/convert pawns</li>
 *   <li>Provide different starting configurations for the board</li>
 *   <li>Support various scoring strategies</li>
 *   <li>Provide any sort of deck and hand sizes</li>
 *   <li>...</li>
 * </ul>
 *
 * <p>Implementations must preserve these core INVARIANTS:</p>
 * <ul>
 *   <li>Every board implementation must use a grid representation with rows and columns,
 *       regardless of the board's shape</li>
 *   <li>A cell can only contain one type of content (empty, pawns, or a card)</li>
 *   <li>Cards are only placed in cells with enough pawns to cover their cost</li>
 *   <li>Cards cannot be placed on cells with pawns owned by another player</li>
 * </ul>
 *
 * @param <C> the type of Card used in this game
 * @param <E> the type of Cell used in this game's board
 */
public interface PawnsBoard<C extends Card, E extends PawnsBoardCell<C>> {

  // -----------------------------------------------------------------------
  // Game Setup and Management
  // -----------------------------------------------------------------------

  /**
   * Initializes and starts a new game with the specified parameters.
   * Sets up the board with rows and columns, initializes player decks from the deck configuration
   * file, deals cards to each player's hand, and sets the first player.
   *
   * @param rows the number of rows on the board
   * @param cols the number of columns on the board
   * @param deckConfigPath path to the deck configuration file
   * @param startingHandSize the number of cards each player starts with
   * @throws IllegalArgumentException if any of the dimensional parameters are invalid
   * @throws IllegalArgumentException if the starting hand size is too large
   * @throws {@link InvalidDeckConfigurationException} if deck configuration is invalid or cannot be read
   */
  void startGame(int rows, int cols, String deckConfigPath, int startingHandSize)
          throws IllegalArgumentException, InvalidDeckConfigurationException;

  /**
   * Checks if the game has ended.
   *
   * <p>In the PawnsBoardBase implementation, the game ends when both players pass their turn
   * in succession. Alternative implementations might define different ending conditions.</p>
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Gets the current player whose turn it is.
   *
   * @return the current player
   * @throws IllegalStateException if the game hasn't been started
   */
  Player getCurrentPlayer() throws IllegalStateException;

  // -----------------------------------------------------------------------
  // Player Actions
  // -----------------------------------------------------------------------

  /**
   * Places a card from the current player's hand onto the specified cell.
   * The cell must contain enough pawns owned by the current player to cover the card's cost.
   * After placement, the card's influence will be applied to the board according to the
   * game's influence rules.
   *
   * <p>This is a core game action that may have implementation-specific influence effects.
   * All implementations must ensure that no cell ever exceeds a certain number pawns
   * when applying influence.</p>
   *
   * @param cardIndex the index of the card in the current player's hand
   * @param row the row index where the card will be placed
   * @param col the column index where the card will be placed
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws {@link IllegalAccessException} if the cell doesn't contain enough pawns for the card's cost
   * @throws {@link IllegalOwnerException} if the pawns in the cell aren't owned by the current player
   * @throws {@link IllegalCardException} if the card is not in the current player's hand
   */
  void placeCard(int cardIndex, int row, int col)
          throws IllegalArgumentException, IllegalStateException, IllegalAccessException,
          IllegalOwnerException, IllegalCardException;

  /**
   * The current player passes their turn, giving control to the other player.
   *
   * <p>In the PawnsBoardBase implementation, if both players pass consecutively,
   * the game ends.</p>
   *
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if there's an issue with turn control
   */
  void passTurn() throws IllegalStateException, IllegalOwnerException;


  // -----------------------------------------------------------------------
  // Board and Game State Queries
  // -----------------------------------------------------------------------

  /**
   * Gets the dimensions of the board.
   *
   * <p>In the PawnsBoardBase implementation, the board is a rectangle so the int array will have
   * 2 values, first one being the width of the board, and second value being the height of the
   * board.</p>
   *
   * @return an array where the elements represent some type of dimension depending on the shape of
   * the board.
   * @throws IllegalStateException if the game hasn't been started
   */
  int[] getBoardDimensions() throws IllegalStateException;

  /**
   * Gets the content type of the given cell position on the board with the given dimensions
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a {@link CellContent} enum indicating whether the cell is empty, contains pawns, or a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  CellContent getCellContent(int row, int col)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the owner of a cell's contents (pawns or card).
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the {@link Player} who owns the cell's contents, or null if the cell is empty
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  Player getCellOwner(int row, int col)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the number of pawns in a specified cell.
   *
   * <p>This method will never return a value greater than 3, in the PawnsBoardBase
   * implementation.</p>
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the number of pawns in the cell, or 0 if the cell is empty or contains a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
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
  List<C> getPlayerHand(Player player) throws IllegalStateException;


  /**
   * Gets the number of cards remaining in the specified player's deck.
   *
   * @param player the player whose deck size to retrieve
   * @return the number of cards left in the player's deck
   * @throws IllegalStateException if the game hasn't been started
   */
  int getRemainingDeckSize(Player player) throws IllegalStateException;

  // -----------------------------------------------------------------------
  // Scoring and Game Outcome
  // -----------------------------------------------------------------------

  /**
   * Gets the row scores for both players for a specific row.
   *
   * <p>In the PawnsBoardBase implementation, the row score is calculated by summing the value
   * scores of each player's cards on that row.</p>
   *
   * @param row the row index to calculate scores for
   * @return an array where the first element is Red's score for the row and the second is Blue's
   * @throws IllegalArgumentException if the row is out of bounds
   * @throws IllegalStateException if the game hasn't been started
   */
  int[] getRowScores(int row) throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the total score for each player across all rows.
   *
   * <p>In the PawnsBoardBase implementation, for each row, the player with the higher row score
   * adds that score to their total. If row scores are tied, neither player gets points.
   * Alternative implementations may use different scoring rules.</p>
   *
   * @return an array where the first element is Red's total score and the second is Blue's
   * @throws IllegalStateException if the game hasn't been started
   */
  int[] getTotalScore() throws IllegalStateException;

  /**
   * Gets the winning player if the game is over.
   *
   * @return the winning Player (RED or BLUE), or null if the game is tied
   * @throws IllegalStateException if the game hasn't been started or is not over
   */
  Player getWinner() throws IllegalStateException;
}