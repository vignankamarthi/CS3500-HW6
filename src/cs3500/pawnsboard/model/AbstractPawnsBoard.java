package cs3500.pawnsboard.model;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.cell.PawnsBoardCell;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract implementation of the PawnsBoard interface that provides common functionality
 * for all Pawns Board game implementations. This class handles game state management,
 * player turns, validation, and scoring calculations that are common across different
 * {@link PawnsBoard} implementations.
 *
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
 * @param <E> the type of Cell used in this game's board
 */
public abstract class AbstractPawnsBoard<C extends Card, E extends PawnsBoardCell<C>> 
        implements PawnsBoard<C, E> {

  // Game state
  protected boolean gameStarted;
  protected boolean gameOver;
  protected PlayerColors currentPlayerColors;
  protected boolean lastPlayerPassed;
  protected int startingHandSize;

  // Board state - part of the grid representation invariant
  protected int rows;
  protected int columns;

  // PlayerColors decks and hands
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
  public PlayerColors getCurrentPlayer() throws IllegalStateException {
    validateGameStarted();
    return currentPlayerColors;
  }

  /**
   * Helper method to draw a card for the current player.
   * Only draws a card if there are cards remaining in the deck
   * and the starting hand size hasn't been exceeded.
   *
   * @throws IllegalStateException if the game hasn't been started or is already over
   */
  protected void drawCard() throws IllegalStateException {
    validateGameInProgress();

    List<C> currentDeck = getCurrentPlayerDeck();
    List<C> currentHand = getCurrentPlayerHand();

    // Only draw if:
    // 1. There are cards left in the deck
    // 2. Hand size is not at full capacity (equal to startingHandSize)
    if (!currentDeck.isEmpty() && currentHand.size() < startingHandSize) {
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
   * Gets the cards in the specified playerColors's hand.
   *
   * @param playerColors the playerColors whose hand to retrieve
   * @return a list of Card objects representing the playerColors's hand
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public List<C> getPlayerHand(PlayerColors playerColors) throws IllegalStateException {
    validateGameStarted();

    if (playerColors == null) {
      throw new IllegalArgumentException("PlayerColors cannot be null");
    }

    // Return a defensive copy of the hand
    return new ArrayList<>((isPlayerRed(playerColors)) ? redHand : blueHand);
  }

  /**
   * Gets the number of cards remaining in the specified playerColors's deck.
   *
   * @param playerColors the playerColors whose deck size to retrieve
   * @return the number of cards left in the playerColors's deck
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int getRemainingDeckSize(PlayerColors playerColors) throws IllegalStateException {
    validateGameStarted();

    if (playerColors == null) {
      throw new IllegalArgumentException("PlayerColors cannot be null");
    }

    return isPlayerRed(playerColors) ? redDeck.size() : blueDeck.size();
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
   * @return the winning PlayerColors (RED or BLUE), or null if the game is tied
   * @throws IllegalStateException if the game hasn't been started or is not over
   */
  @Override
  public PlayerColors getWinner() throws IllegalStateException {
    validateGameStarted();

    if (!gameOver) {
      throw new IllegalStateException("Game is not over yet");
    }

    int[] scores = getTotalScore();
    int redScore = scores[0];
    int blueScore = scores[1];

    if (redScore > blueScore) {
      return PlayerColors.RED;
    } else if (blueScore > redScore) {
      return PlayerColors.BLUE;
    } else {
      return null; // Tie
    }
  }

  /**
   * Switches the current player to the other player.
   */
  protected void switchPlayer() {
    currentPlayerColors = isPlayerRed(currentPlayerColors) ? PlayerColors.BLUE : PlayerColors.RED;
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
    return isPlayerRed(currentPlayerColors) ? redDeck : blueDeck;
  }

  /**
   * Gets the hand for the current player.
   *
   * @return the current player's hand
   */
  protected List<C> getCurrentPlayerHand() {
    return isPlayerRed(currentPlayerColors) ? redHand : blueHand;
  }
  
  /**
   * Checks if the given playerColors is the RED playerColors.
   *
   * @param playerColors the playerColors to check
   * @return true if the playerColors is RED, false otherwise
   */
  protected boolean isPlayerRed(PlayerColors playerColors) {
    return playerColors == PlayerColors.RED;
  }

  /**
   * Gets the card at the specified cell position.
   * This is a default implementation that throws UnsupportedOperationException.
   * Concrete subclasses must override this method to provide appropriate implementations.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the card at the specified position, or null if the cell doesn't contain a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   * @throws UnsupportedOperationException if not implemented by a subclass
   */
  @Override
  public C getCardAtCell(int row, int col) 
          throws IllegalArgumentException, IllegalStateException {
    validateGameStarted();
    validateCoordinates(row, col);
    
    if (getCellContent(row, col) != CellContent.CARD) {
      return null;
    }
    
    // This method needs implementation in subclasses that know about the board structure
    throw new UnsupportedOperationException("Method must be implemented by subclasses");
  }
  
  /**
   * Checks if it's legal for the current player to place a specific card at the given coordinates.
   * This method allows checking move legality without actually making the move.
   * A move is legal if all the following conditions are met:
   * - The game is in progress (started and not over)
   * - The card index is valid
   * - The cell contains pawns
   * - The pawns are owned by the current player
   * - There are enough pawns to cover the card's cost
   *
   * @param cardIndex the index of the card in the current player's hand
   * @param row the row index where the card would be placed
   * @param col the column index where the card would be placed
   * @return true if the move is legal, false otherwise
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started or is already over
   */
  @Override
  public boolean isLegalMove(int cardIndex, int row, int col)
          throws IllegalArgumentException, IllegalStateException {
    try {
      validateGameInProgress();
      validateCoordinates(row, col);
      
      List<C> currentHand = getCurrentPlayerHand();
      
      // Check if card index is valid
      if (cardIndex < 0 || cardIndex >= currentHand.size()) {
        return false;
      }
      
      C cardToPlace = currentHand.get(cardIndex);
      
      // Check if cell has pawns
      if (getCellContent(row, col) != CellContent.PAWNS) {
        return false;
      }
      
      // Check if pawns are owned by the current player
      if (getCellOwner(row, col) != currentPlayerColors) {
        return false;
      }
      
      // Check if there are enough pawns
      if (getPawnCount(row, col) < cardToPlace.getCost()) {
        return false;
      }
      
      return true;
      
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Game is not over yet");
    } catch (IllegalArgumentException e) {
      // Invalid coordinates
      throw new IllegalStateException("Invalid coordinates: (" + row + ", " + col + ")");
    }
  }
}