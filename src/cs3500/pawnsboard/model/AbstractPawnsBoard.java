package cs3500.pawnsboard.model;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract implementation of the PawnsBoard interface that provides common functionality
 * for all Pawns Board game implementations. This class handles game state management,
 * player turns, validation, and scoring calculations that are common across different
 * {@link PawnsBoard} implementations.
 *
 * <p>The abstract class implements methods that have universal behavior regardless of
 * the specific board implementation, while leaving board-specific operations as abstract
 * methods to be implemented by concrete subclasses.</p>
 *
 * <p>All implementations must preserve these core invariants:</p>
 * <ul>
 *   <li>Every board implementation uses a grid representation with rows and columns,
 *       which are initialized during game start</li>
 *   <li>A cell can only contain one type of content (empty, pawns, or a card)</li>
 *   <li>Cards are only placed in cells with enough pawns to cover their cost</li>
 *   <li>Cards cannot be placed on cells with pawns owned by another player</li>
 * </ul>
 *
 * @param <C> the type of Card used in this game
 */
public abstract class AbstractPawnsBoard<C extends Card> implements PawnsBoard<C> {

  // Game state
  protected boolean gameStarted;
  protected boolean gameOver;
  protected Player currentPlayer;
  protected boolean lastPlayerPassed;

  // Board state - part of the grid representation invariant
  protected int rows;
  protected int columns;

  // Player decks and hands
  protected List<C> redDeck;
  protected List<C> blueDeck;
  protected List<C> redHand;
  protected List<C> blueHand;

  /**
   * Checks if the game has ended.
   *
   * <p>In the standard implementation, the game ends when both players pass their turn
   * in succession. Alternative implementations might define different ending conditions.</p>
   *
   * @return true if the game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * Gets the current player whose turn it is.
   *
   * @return the current player
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public Player getCurrentPlayer() throws IllegalStateException {
    validateGameStarted();
    return currentPlayer;
  }

  /**
   * Helper method to draw a card for the current player.
   * This happens automatically at the start of a turn.
   * If there are no cards left in the deck, no card is drawn.
   *
   * @throws IllegalStateException if the game hasn't been started or is already over
   */
  protected void drawCard() throws IllegalStateException {
    validateGameInProgress();

    List<C> currentDeck = getCurrentPlayerDeck();
    List<C> currentHand = getCurrentPlayerHand();

    if (!currentDeck.isEmpty()) {
      currentHand.add(currentDeck.remove(0));
    }
  }

  /**
   * The current player passes their turn, giving control to the other player.
   *
   * <p>If both players pass consecutively, the game ends.</p>
   *
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if there's an issue with turn control
   */
  @Override
  public void passTurn() throws IllegalStateException, IllegalOwnerException {
    validateGameInProgress();

    // Check if the previous player also passed
    if (lastPlayerPassed) {
      gameOver = true;
    } else {
      lastPlayerPassed = true;
      switchPlayer();
      
      // Draw a card for the new current player at the start of their turn
      drawCard();
    }
  }

  /**
   * Gets the cards in the specified player's hand.
   *
   * @param player the player whose hand to retrieve
   * @return a list of Card objects representing the player's hand
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public List<C> getPlayerHand(Player player) throws IllegalStateException {
    validateGameStarted();

    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    // Return a defensive copy of the hand
    return new ArrayList<>(player == Player.RED ? redHand : blueHand);
  }

  /**
   * Gets the number of cards remaining in the specified player's deck.
   *
   * @param player the player whose deck size to retrieve
   * @return the number of cards left in the player's deck
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int getRemainingDeckSize(Player player) throws IllegalStateException {
    validateGameStarted();

    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    return player == Player.RED ? redDeck.size() : blueDeck.size();
  }

  /**
   * Gets the total score for each player across all rows.
   * For each row, the player with the higher row score adds that score to their total.
   * If row scores are tied, neither player gets points.
   *
   * @return an array where the first element is Red's total score and the second is Blue's
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int[] getTotalScore() throws IllegalStateException {
    validateGameStarted();

    int redTotal = 0;
    int blueTotal = 0;

    for (int row = 0; row < rows; row++) {
      int[] rowScores = getRowScores(row);
      int redRowScore = rowScores[0];
      int blueRowScore = rowScores[1];

      if (redRowScore > blueRowScore) {
        redTotal += redRowScore;
      } else if (blueRowScore > redRowScore) {
        blueTotal += blueRowScore;
      }
      // If tied, neither player gets points
    }

    return new int[] {redTotal, blueTotal};
  }

  /**
   * Gets the winning player if the game is over.
   *
   * @return the winning Player (RED or BLUE), or null if the game is tied
   * @throws IllegalStateException if the game hasn't been started or is not over
   */
  @Override
  public Player getWinner() throws IllegalStateException {
    validateGameStarted();

    if (!gameOver) {
      throw new IllegalStateException("Game is not over yet");
    }

    int[] scores = getTotalScore();
    int redScore = scores[0];
    int blueScore = scores[1];

    if (redScore > blueScore) {
      return Player.RED;
    } else if (blueScore > redScore) {
      return Player.BLUE;
    } else {
      return null; // Tie
    }
  }

  /**
   * Switches the current player to the other player.
   */
  protected void switchPlayer() {
    currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;
  }

  /**
   * Validates that the game has been started.
   *
   * @throws IllegalStateException if the game hasn't been started
   */
  protected void validateGameStarted() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
  }

  /**
   * Validates that the game is in progress (started and not over).
   *
   * @throws IllegalStateException if the game hasn't been started or is already over
   */
  protected void validateGameInProgress() throws IllegalStateException {
    validateGameStarted();

    if (gameOver) {
      throw new IllegalStateException("Game is already over");
    }
  }

  /**
   * Validates that the given coordinates are within the board bounds.
   *
   * @param row the row coordinate
   * @param col the column coordinate
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  protected void validateCoordinates(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= rows || col < 0 || col >= columns) {
      throw new IllegalArgumentException(
              "Invalid coordinates: (" + row + ", " + col + ")");
    }
  }

  /**
   * Gets the deck for the current player.
   *
   * @return the current player's deck
   */
  protected List<C> getCurrentPlayerDeck() {
    return currentPlayer == Player.RED ? redDeck : blueDeck;
  }

  /**
   * Gets the hand for the current player.
   *
   * @return the current player's hand
   */
  protected List<C> getCurrentPlayerHand() {
    return currentPlayer == Player.RED ? redHand : blueHand;
  }
}