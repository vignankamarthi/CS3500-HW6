package cs3500.pawnsboard.model;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.cell.PawnsBoardCell;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;

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
public interface PawnsBoard<C extends Card, E extends PawnsBoardCell<C>> 
        extends ReadOnlyPawnsBoard<C, E> {

  // -----------------------------------------------------------------------
  // Game Setup and Management
  // -----------------------------------------------------------------------

  /**
   * Initializes and starts a new game with the specified parameters.
   * Sets up the board with rows and columns, initializes player decks from the deck configuration
   * files, deals cards to each player's hand, and sets the first player.
   *
   * @param rows the number of rows on the board
   * @param cols the number of columns on the board
   * @param redDeckConfigPath path to the RED player's deck configuration file
   * @param blueDeckConfigPath path to the BLUE player's deck configuration file
   * @param startingHandSize the number of cards each player starts with
   * @throws IllegalArgumentException if any of the dimensional parameters are invalid
   * @throws IllegalArgumentException if the starting hand size is too large
   * @throws InvalidDeckConfigurationException if deck configuration is invalid or cannot be
   *                                           read
   */
  void startGame(int rows, int cols, String redDeckConfigPath,
                 String blueDeckConfigPath, int startingHandSize)
          throws IllegalArgumentException, InvalidDeckConfigurationException;

  // -----------------------------------------------------------------------
  // PlayerColors Actions
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
   * @throws IllegalAccessException if the cell doesn't contain enough pawns for the card's cost
   * @throws IllegalOwnerException if the pawns in the cell aren't owned by the current player
   * @throws IllegalCardException if the card is not in the current player's hand
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
  
  /**
   * Creates a deep copy of the current game board state.
   * This is useful for AI players to simulate moves without affecting the actual game.
   *
   * @return a new PawnsBoard instance with the same state as this board
   * @throws IllegalStateException if the game hasn't been started
   */
  PawnsBoard<C, E> copy() throws IllegalStateException;
}