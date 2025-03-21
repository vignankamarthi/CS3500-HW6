package cs3500.pawnsboard.model;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.cell.PawnsBoardCell;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mock implementation of the PawnsBoard interface for testing purposes.
 * This mock allows precise control over the state of the game board,
 * making it easier to test views and controllers in isolation.
 *
 * <p>The mock provides methods to set specific states such as cell contents,
 * card placement, pawn counts, and scores. It implements all methods from the
 * PawnsBoard interface, returning the pre-configured values rather than
 * computing them based on game logic.</p>
 *
 * <p>Most methods follow a fluent interface pattern, returning the mock instance
 * to allow for method chaining during test setup.</p>
 *
 * @param <C> the type of Card used in this game
 * @param <E> the type of Cell used in this game's board
 */
public class PawnsBoardMock<C extends Card, E extends PawnsBoardCell<C>> 
        implements PawnsBoard<C, E> {

  private boolean gameStarted;
  private boolean gameOver;
  private PlayerColors currentPlayer;
  private int rows;
  private int columns;
  private final Map<String, CellContent> cellContents;
  private final Map<String, PlayerColors> cellOwners;
  private final Map<String, Integer> pawnCounts;
  private final Map<String, C> cellCards;
  private final Map<Integer, int[]> rowScores;
  private final List<C> redHand;
  private final List<C> blueHand;
  private int[] totalScore;
  private PlayerColors winner;
  private int redDeckSize;
  private int blueDeckSize;

  /**
   * Constructs a mock PawnsBoard with default empty state.
   * Initially the game is not started, the board has 0 rows and columns,
   * and all collections are initialized as empty.
   */
  public PawnsBoardMock() {
    this.gameStarted = false;
    this.gameOver = false;
    this.currentPlayer = PlayerColors.RED;
    this.rows = 0;
    this.columns = 0;
    this.cellContents = new HashMap<>();
    this.cellOwners = new HashMap<>();
    this.pawnCounts = new HashMap<>();
    this.cellCards = new HashMap<>();
    this.rowScores = new HashMap<>();
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.totalScore = new int[]{0, 0};
    this.winner = null;
    this.redDeckSize = 0;
    this.blueDeckSize = 0;
  }

  /**
   * Returns whether the game has been started.
   * This value is set by setGameStarted().
   *
   * @return true if the game has been started, false otherwise
   */
  public boolean isGameStarted() {
    return gameStarted;
  }

  /**
   * Sets the game's started state.
   * This determines whether methods that require a started game will throw
   * IllegalStateException or return valid values.
   *
   * @param started whether the game has started
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setGameStarted(boolean started) {
    this.gameStarted = started;
    return this;
  }

  /**
   * Sets the game's over state.
   * When true, isGameOver() will return true and getWinner() can be called.
   * When false, getWinner() will throw IllegalStateException.
   *
   * @param over whether the game is over
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setGameOver(boolean over) {
    this.gameOver = over;
    return this;
  }

  /**
   * Sets the current player whose turn it is.
   * This value will be returned by getCurrentPlayer() when the game is started.
   *
   * @param player the current player (RED or BLUE)
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setCurrentPlayer(PlayerColors player) {
    this.currentPlayer = player;
    return this;
  }

  /**
   * Sets the board dimensions.
   * These values will be returned by getBoardDimensions() and used for
   * coordinate validation in other methods.
   *
   * @param rows the number of rows in the board
   * @param cols the number of columns in the board
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setBoardDimensions(int rows, int cols) {
    this.rows = rows;
    this.columns = cols;
    return this;
  }

  /**
   * Sets the content type of a specific cell.
   * This value will be returned by getCellContent() for the specified coordinates.
   *
   * @param row     the row index of the cell
   * @param col     the column index of the cell
   * @param content the content type (EMPTY, PAWNS, or CARD)
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setCellContent(int row, int col, CellContent content) {
    cellContents.put(cellKey(row, col), content);
    return this;
  }

  /**
   * Sets the owner of a specific cell.
   * This value will be returned by getCellOwner() for the specified coordinates.
   *
   * @param row   the row index of the cell
   * @param col   the column index of the cell
   * @param owner the player who owns the cell (RED or BLUE)
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setCellOwner(int row, int col, PlayerColors owner) {
    cellOwners.put(cellKey(row, col), owner);
    return this;
  }

  /**
   * Sets the pawn count for a specific cell.
   * This value will be returned by getPawnCount() for the specified coordinates.
   *
   * @param row   the row index of the cell
   * @param col   the column index of the cell
   * @param count the number of pawns in the cell (0-3)
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setPawnCount(int row, int col, int count) {
    pawnCounts.put(cellKey(row, col), count);
    return this;
  }

  /**
   * Sets the card in a specific cell.
   * This card will be returned by getCardAtCell() for the specified coordinates.
   *
   * @param row  the row index of the cell
   * @param col  the column index of the cell
   * @param card the card to place in the cell
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setCardAtCell(int row, int col, C card) {
    cellCards.put(cellKey(row, col), card);
    return this;
  }

  /**
   * Sets the row scores for a specific row.
   * These values will be returned by getRowScores() for the specified row.
   *
   * @param row        the row index
   * @param redScore   the RED player's score for this row
   * @param blueScore  the BLUE player's score for this row
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setRowScores(int row, int redScore, int blueScore) {
    rowScores.put(row, new int[]{redScore, blueScore});
    return this;
  }

  /**
   * Sets the total score for both players.
   * These values will be returned by getTotalScore().
   *
   * @param redTotal   the RED player's total score
   * @param blueTotal  the BLUE player's total score
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setTotalScore(int redTotal, int blueTotal) {
    this.totalScore = new int[]{redTotal, blueTotal};
    return this;
  }

  /**
   * Sets the winner of the game.
   * This value will be returned by getWinner() when the game is over.
   * A null value indicates a tie game.
   *
   * @param winner the winner (RED, BLUE, or null for tie)
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setWinner(PlayerColors winner) {
    this.winner = winner;
    return this;
  }

  /**
   * Sets a player's hand of cards.
   * These cards will be returned by getPlayerHand() for the specified player.
   *
   * @param player the player whose hand to set
   * @param hand   the list of cards in the player's hand
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setPlayerHand(PlayerColors player, List<C> hand) {
    if (player == PlayerColors.RED) {
      redHand.clear();
      redHand.addAll(hand);
    } else {
      blueHand.clear();
      blueHand.addAll(hand);
    }
    return this;
  }

  /**
   * Sets a player's remaining deck size.
   * This value will be returned by getRemainingDeckSize() for the specified player.
   *
   * @param player    the player whose deck size to set
   * @param deckSize  the number of cards left in the player's deck
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setRemainingDeckSize(PlayerColors player, int deckSize) {
    if (player == PlayerColors.RED) {
      redDeckSize = deckSize;
    } else {
      blueDeckSize = deckSize;
    }
    return this;
  }

  /**
   * Helper method to create a key for the cell maps.
   * Creates a unique string key from row and column indices.
   *
   * @param row the row index
   * @param col the column index
   * @return the cell key in the format "row,col"
   */
  private String cellKey(int row, int col) {
    return row + "," + col;
  }

  /**
   * Sets up a basic 3x5 initial board state with pawns at the edges.
   * This creates a standard starting board with RED pawns in the first column
   * and BLUE pawns in the last column, all with count 1.
   *
   * @return this mock for method chaining
   */
  public PawnsBoardMock<C, E> setupInitialBoard() {
    this.gameStarted = true;
    this.gameOver = false;
    this.currentPlayer = PlayerColors.RED;
    this.rows = 3;
    this.columns = 5;
    
    // Clear previous state
    this.cellContents.clear();
    this.cellOwners.clear();
    this.pawnCounts.clear();
    
    // Set up RED pawns in first column
    for (int r = 0; r < 3; r++) {
      setCellContent(r, 0, CellContent.PAWNS);
      setCellOwner(r, 0, PlayerColors.RED);
      setPawnCount(r, 0, 1);
    }
    
    // Set up BLUE pawns in last column
    for (int r = 0; r < 3; r++) {
      setCellContent(r, 4, CellContent.PAWNS);
      setCellOwner(r, 4, PlayerColors.BLUE);
      setPawnCount(r, 4, 1);
    }
    
    // Set row scores to 0
    for (int r = 0; r < 3; r++) {
      setRowScores(r, 0, 0);
    }
    
    setTotalScore(0, 0);
    
    return this;
  }

  //-----------------------------------------------------------------------
  // PawnsBoard interface implementation methods
  //-----------------------------------------------------------------------

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
  @Override
  public void startGame(int rows, int cols, String redDeckConfigPath,
                        String blueDeckConfigPath, int startingHandSize)
          throws IllegalArgumentException, InvalidDeckConfigurationException {
    // Do nothing, this is a mock
  }

  /**
   * Returns whether the game is over.
   * This value is set by setGameOver().
   *
   * @return true if the game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * Returns the current player whose turn it is.
   * This value is set by setCurrentPlayer().
   *
   * @return the current player
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public PlayerColors getCurrentPlayer() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    return currentPlayer;
  }

  /**
   * Mock implementation of placeCard.
   * This method is a no-op in the mock as the board state is set directly
   * through the setter methods.
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
  @Override
  public void placeCard(int cardIndex, int row, int col) {
    // Do nothing, this is a mock
  }

  /**
   * Mock implementation of passTurn.
   * This method is a no-op in the mock as the game state is set directly
   * through the setter methods.
   *
   * @throws IllegalStateException if the game hasn't been started or is already over
   * @throws IllegalOwnerException if there's an issue with turn control
   */
  @Override
  public void passTurn() {
    // Do nothing, this is a mock
  }

  /**
   * Returns the dimensions of the board.
   * These values are set by setBoardDimensions().
   *
   * @return an array where the first element is the number of rows and the second is the number of
   *         columns
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int[] getBoardDimensions() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    return new int[] {rows, columns};
  }

  /**
   * Returns the content type of a specific cell.
   * This value is set by setCellContent().
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the cell content type (EMPTY, PAWNS, or CARD)
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public CellContent getCellContent(int row, int col) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    validateCoordinates(row, col);
    return cellContents.getOrDefault(cellKey(row, col), CellContent.EMPTY);
  }

  /**
   * Returns the owner of a specific cell.
   * This value is set by setCellOwner().
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the player who owns the cell, or null if the cell is empty
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public PlayerColors getCellOwner(int row, int col) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    validateCoordinates(row, col);
    return cellOwners.get(cellKey(row, col));
  }

  /**
   * Returns the number of pawns in a specific cell.
   * This value is set by setPawnCount().
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the number of pawns in the cell, or 0 if the cell is empty or contains a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int getPawnCount(int row, int col) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    validateCoordinates(row, col);
    return pawnCounts.getOrDefault(cellKey(row, col), 0);
  }

  /**
   * Returns a player's hand of cards.
   * These values are set by setPlayerHand().
   * Returns a defensive copy to prevent modification of the mock's state.
   *
   * @param playerColors the player whose hand to retrieve
   * @return a list of cards in the player's hand
   * @throws IllegalStateException if the game hasn't been started
   * @throws IllegalArgumentException if playerColors is null
   */
  @Override
  public List<C> getPlayerHand(PlayerColors playerColors) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    if (playerColors == null) {
      throw new IllegalArgumentException("PlayerColors cannot be null");
    }
    return new ArrayList<>(playerColors == PlayerColors.RED ? redHand : blueHand);
  }

  /**
   * Returns a player's remaining deck size.
   * This value is set by setRemainingDeckSize().
   *
   * @param playerColors the player whose deck size to retrieve
   * @return the number of cards left in the player's deck
   * @throws IllegalStateException if the game hasn't been started
   * @throws IllegalArgumentException if playerColors is null
   */
  @Override
  public int getRemainingDeckSize(PlayerColors playerColors) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    if (playerColors == null) {
      throw new IllegalArgumentException("PlayerColors cannot be null");
    }
    return playerColors == PlayerColors.RED ? redDeckSize : blueDeckSize;
  }

  /**
   * Returns the scores for a specific row.
   * These values are set by setRowScores().
   *
   * @param row the row index to calculate scores for
   * @return an array where the first element is Red's score for the row and the second is Blue's
   * @throws IllegalArgumentException if the row is out of bounds
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int[] getRowScores(int row) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    if (row < 0 || row >= rows) {
      throw new IllegalArgumentException("Row index out of bounds: " + row);
    }
    return rowScores.getOrDefault(row, new int[]{0, 0});
  }

  /**
   * Returns the total score for both players.
   * These values are set by setTotalScore().
   *
   * @return an array where the first element is Red's total score and the second is Blue's
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int[] getTotalScore() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    return totalScore.clone();
  }

  /**
   * Returns the winner of the game.
   * This value is set by setWinner().
   *
   * @return the winning player (RED or BLUE), or null if the game is tied
   * @throws IllegalStateException if the game hasn't been started or is not over
   */
  @Override
  public PlayerColors getWinner() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    if (!gameOver) {
      throw new IllegalStateException("Game is not over yet");
    }
    return winner;
  }

  /**
   * Returns the card in a specific cell.
   * This value is set by setCardAtCell().
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the card in the cell, or null if the cell doesn't contain a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public C getCardAtCell(int row, int col) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    validateCoordinates(row, col);
    return cellCards.get(cellKey(row, col));
  }

  /**
   * Validates that the given coordinates are within the board bounds.
   *
   * @param row the row coordinate
   * @param col the column coordinate
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  private void validateCoordinates(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= columns) {
      throw new IllegalArgumentException(
              "Invalid coordinates: (" + row + ", " + col + ")");
    }
  }
}
