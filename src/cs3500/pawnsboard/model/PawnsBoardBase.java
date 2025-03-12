package cs3500.pawnsboard.model;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.cards.deckbuilder.DeckBuilder;
import cs3500.pawnsboard.model.cards.deckbuilder.PawnsBoardBaseCardDeckBuilder;
import cs3500.pawnsboard.model.cell.PawnsBoardCell;
import cs3500.pawnsboard.model.cell.PawnsBoardBaseCell;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.Player;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the PawnsBoard interface for a rectangular board.
 * This class represents a rectangular Pawns Board game with all the required functionality
 * to play the game, including initializing the board, placing cards, applying influence,
 * and calculating scores.
 *
 * <p>This implementation has these specific behaviors:</p>
 * <ul>
 *   <li>The board is implemented as a grid with a positive number of rows and an odd
 *       number of columns, maintaining the grid representation invariant</li>
 *   <li>The initial state has pawns in the first column owned by RED and pawns in the last
 *       column owned by BLUE</li>
 *   <li>Influence adds pawns to empty cells, increases pawn count for owned pawns,
 *       and changes ownership of opponent's pawns</li>
 *   <li>The maximum number of pawns in a cell is 3</li>
 *   <li>The game ends when both players pass consecutively</li>
 * </ul>
 *
 * <p>Class invariants include:</p>
 * <ul>
 *   <li>The board dimensions remain fixed after game initialization - 'rows' and 'columns' 
 *       are set in startGame() and never modified by any other method (DOCUMENTED INVARIANT)</li>
 *   <li>A cell can only contain one type of content (empty, pawns, or a card)</li>
 *   <li>Cards are only placed in cells with enough pawns to cover their cost</li>
 *   <li>Cells never contain more than 3 pawns</li>
 * </ul>
 *
 * @param <C> the type of Card used in this game
 */

//TODO: Test this implementation

public class PawnsBoardBase<C extends Card> 
        extends AbstractPawnsBoard<C, PawnsBoardBaseCell<C>> {

  // Board state - using List of Lists instead of arrays for type safety
  private List<List<PawnsBoardCell<C>>> board;

  // Deck builder for card reading
  private final DeckBuilder<C> deckBuilder;

  /**
   * Constructs a PawnsBoardBase with a specific deck builder.
   *
   * @param deckBuilder the deck builder to use for card reading
   * @throws IllegalArgumentException if deckBuilder is null
   */
  public PawnsBoardBase(DeckBuilder<C> deckBuilder) {
    if (deckBuilder == null) {
      throw new IllegalArgumentException("Deck builder cannot be null");
    }
    this.deckBuilder = deckBuilder;
  }

  /**
   * Constructs a PawnsBoardBase with a default deck builder.
   * Uses the PawnsBoardBaseCardDeckBuilder as the default implementation.
   */
  @SuppressWarnings("unchecked")
  public PawnsBoardBase() {
    this.deckBuilder = (DeckBuilder<C>) new PawnsBoardBaseCardDeckBuilder();
  }

  /**
   * Initializes and starts a new game with the specified parameters.
   * Sets up the board with rows and columns, initializes player decks from the deck configuration
   * file, deals cards to each player's hand, and sets the first player.
   *
   * <p>For rectangular boards, rows must be positive and columns must be odd and greater than 1.
   * The starting pawns are placed in the first and last columns.</p>
   *
   * @param rows the number of rows on the board
   * @param cols the number of columns on the board
   * @param deckConfigPath path to the deck configuration file
   * @param startingHandSize the number of cards each player starts with
   * @throws IllegalArgumentException if any of the dimensional parameters are invalid
   * @throws IllegalArgumentException if the starting hand size is too large
   * @throws InvalidDeckConfigurationException if deck configuration is invalid or cannot be read
   */
  @Override
  public void startGame(int rows, int cols, String deckConfigPath, int startingHandSize)
          throws IllegalArgumentException, InvalidDeckConfigurationException {
    // Validate dimensions
    validateBoardDimensions(rows, cols);

    // Set up board
    // DOCUMENTED INVARIANT: Board dimensions (rows and columns) are set here and 
    // never modified elsewhere, maintaining the invariant that board dimensions 
    // remain fixed after initialization
    this.rows = rows;
    this.columns = cols;
    board = createEmptyBoard(rows, cols);

    // Set up decks and hands
    List<List<C>> decks = deckBuilder.createDecks(deckConfigPath, false);
    if (decks.size() != 2) {
      throw new InvalidDeckConfigurationException("Expected 2 decks, got " + decks.size());
    }

    redDeck = decks.get(0);
    blueDeck = decks.get(1);

    // Validate deck size
    int minDeckSize = rows * cols;
    if (redDeck.size() < minDeckSize || blueDeck.size() < minDeckSize) {
      throw new InvalidDeckConfigurationException(
              "Deck size must be at least " + minDeckSize + " cards");
    }

    // Validate starting hand size
    if (startingHandSize > redDeck.size() / 3) {
      throw new IllegalArgumentException(
              "Starting hand size cannot exceed one third of the deck size");
    }

    // Set up hands
    redHand = new ArrayList<>();
    blueHand = new ArrayList<>();

    // Deal cards
    for (int i = 0; i < startingHandSize; i++) {
      redHand.add(redDeck.remove(0));
      blueHand.add(blueDeck.remove(0));
    }

    // Initialize board with starting pawns
    initializeStartingBoard();

    // Set game state
    currentPlayer = Player.RED;
    gameStarted = true;
    gameOver = false;
    lastPlayerPassed = false;

    // Draw a card for the first player at the start of the game
    drawCard();
  }

  /**
   * Places a card from the current player's hand onto the specified cell.
   * The cell must contain enough pawns owned by the current player to cover the card's cost.
   * After placement, the card's influence will be applied to the board according to the
   * game's influence rules.
   *
   * <p>In this implementation, influence spreads according to the card's influence grid, adding
   * pawns to empty cells, increasing pawn count for owned pawns, and changing ownership of
   * opponent's pawns.</p>
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
  public void placeCard(int cardIndex, int row, int col)
          throws IllegalArgumentException, IllegalStateException, IllegalAccessException,
          IllegalOwnerException, IllegalCardException {
    validateGameInProgress();
    validateCoordinates(row, col);

    List<C> currentHand = getCurrentPlayerHand();

    // Validate card index
    if (cardIndex < 0 || cardIndex >= currentHand.size()) {
      throw new IllegalCardException("Invalid card index: " + cardIndex);
    }

    C cardToPlace = currentHand.get(cardIndex);
    PawnsBoardCell<C> targetCell = board.get(row).get(col);

    // Check if the cell has pawns
    if (targetCell.getContent() != CellContent.PAWNS) {
      throw new IllegalAccessException("Cell does not contain pawns");
    }

    // Check if the pawns are owned by the current player
    if (targetCell.getOwner() != currentPlayer) {
      throw new IllegalOwnerException("Pawns in cell are not owned by current player");
    }

    // Check if there are enough pawns
    if (targetCell.getPawnCount() < cardToPlace.getCost()) {
      throw new IllegalAccessException(
              "Not enough pawns in cell. Required: " + cardToPlace.getCost()
                      + ", Available: " + targetCell.getPawnCount());
    }

    // Place the card
    targetCell.setCard(cardToPlace, currentPlayer);

    // Apply influence
    applyCardInfluence(cardToPlace, row, col);

    // Remove the card from hand
    currentHand.remove(cardIndex);

    // Reset pass flag and switch players
    lastPlayerPassed = false;
    switchPlayer();
    
    // Draw a card for the new current player at the start of their turn
    drawCard();
  }

  /**
   * Gets the dimensions of the board.
   *
   * @return an array where the first element is the number of rows and the second is the number of
   *         columns
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int[] getBoardDimensions() throws IllegalStateException {
    validateGameStarted();
    // DOCUMENTED INVARIANT: This method returns the dimensions but doesn't modify them,
    // helping maintain the invariant that dimensions remain fixed after initialization
    return new int[] {rows, columns};
  }

  /**
   * Gets the content type of the given cell position on the board.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a {@link CellContent} enum indicating whether the cell is empty, contains pawns,
   *                               or a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public CellContent getCellContent(int row, int col)
          throws IllegalArgumentException, IllegalStateException {
    validateGameStarted();
    validateCoordinates(row, col);
    return board.get(row).get(col).getContent();
  }

  /**
   * Gets the owner of a cell's contents (pawns or card).
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the {@link Player} who owns the cell's contents, or null if the cell is empty
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public Player getCellOwner(int row, int col)
          throws IllegalArgumentException, IllegalStateException {
    validateGameStarted();
    validateCoordinates(row, col);
    return board.get(row).get(col).getOwner();
  }

  /**
   * Gets the number of pawns in a specified cell.
   * This method will never return a value greater than 3 in this implementation.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the number of pawns in the cell, or 0 if the cell is empty or contains a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int getPawnCount(int row, int col)
          throws IllegalArgumentException, IllegalStateException {
    validateGameStarted();
    validateCoordinates(row, col);
    return board.get(row).get(col).getPawnCount();
  }

  /**
   * Gets the row scores for both players for a specific row.
   * The row score is calculated by summing the value scores of each player's cards on that row.
   *
   * @param row the row index to calculate scores for
   * @return an array where the first element is Red's score for the row and the second is Blue's
   * @throws IllegalArgumentException if the row is out of bounds
   * @throws IllegalStateException if the game hasn't been started
   */
  @Override
  public int[] getRowScores(int row) throws IllegalArgumentException, IllegalStateException {
    validateGameStarted();

    if (row < 0 || row >= rows) {
      throw new IllegalArgumentException("Row index out of bounds: " + row);
    }

    int redScore = 0;
    int blueScore = 0;

    for (int col = 0; col < columns; col++) {
      PawnsBoardCell<C> cell = board.get(row).get(col);

      if (cell.getContent() == CellContent.CARD) {
        if (isPlayerRed(cell.getOwner())) {
          redScore += cell.getCard().getValue();
        } else {
          blueScore += cell.getCard().getValue();
        }
      }
    }

    return new int[] {redScore, blueScore};
  }

  /**
   * Validates the board dimensions according to game rules for rectangular boards.
   * This ensures the grid representation invariant is maintained with valid dimensions.
   *
   * @param rows the number of rows
   * @param cols the number of columns
   * @throws IllegalArgumentException if dimensions are invalid
   */
  private void validateBoardDimensions(int rows, int cols) {
    // DOCUMENTED INVARIANT: This validation ensures that only valid dimensions
    // are accepted when initializing the board, contributing to the board dimensions
    // invariant by preventing invalid values from being set
    if (rows <= 0) {
      throw new IllegalArgumentException("Number of rows must be positive");
    }

    if (cols <= 1) {
      throw new IllegalArgumentException("Number of columns must be greater than 1");
    }

    if (cols % 2 == 0) {
      throw new IllegalArgumentException("Number of columns must be odd");
    }
  }

  /**
   * Creates an empty board with the specified dimensions using Lists instead of arrays.
   * This approach is fully type-safe with generics and doesn't require warning suppressions.
   *
   * @param rows the number of rows
   * @param cols the number of columns
   * @return a List of Lists containing empty cells
   */
  private List<List<PawnsBoardCell<C>>> createEmptyBoard(int rows, int cols) {
    List<List<PawnsBoardCell<C>>> newBoard = new ArrayList<>(rows);
    
    for (int r = 0; r < rows; r++) {
      List<PawnsBoardCell<C>> row = new ArrayList<>(cols);
      for (int c = 0; c < cols; c++) {
        row.add(new PawnsBoardBaseCell<>());
      }
      newBoard.add(row);
    }
    
    return newBoard;
  }

  /**
   * Initializes the starting board with pawns in the first and last columns.
   * The first column contains RED pawns, and the last column contains BLUE pawns.
   */
  private void initializeStartingBoard() {
    try {
      // Add RED pawns to first column
      for (int r = 0; r < rows; r++) {
        board.get(r).get(0).addPawn(Player.RED);
      }

      // Add BLUE pawns to last column
      for (int r = 0; r < rows; r++) {
        board.get(r).get(columns - 1).addPawn(Player.BLUE);
      }
    } catch (IllegalOwnerException e) {
      // This should never happen during initialization with empty cells
      throw new IllegalStateException("Error initializing board: " + e.getMessage());
    }
  }

  /**
   * Applies the influence of a card to surrounding cells based on the card's influence grid.
   *
   * @param card the card whose influence is being applied
   * @param row the row where the card was placed
   * @param col the column where the card was placed
   */
  private void applyCardInfluence(C card, int row, int col) {
    boolean[][] influenceGrid = card.getInfluenceGrid();

    // Center position in the influence grid
    int centerRow = 2;
    int centerCol = 2;

    // For blue player, mirror the influence grid horizontally
    if (!isPlayerRed(currentPlayer)) {
      influenceGrid = mirrorInfluenceGrid(influenceGrid);
    }

    // Apply influence to each cell in the influence grid
    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        // Skip if no influence or this is the center cell
        if (!influenceGrid[r][c] || (r == centerRow && c == centerCol)) {
          continue;
        }

        // Calculate target cell coordinates on the game board
        int targetRow = row + (r - centerRow);
        int targetCol = col + (c - centerCol);

        // Skip if target cell is outside the board
        if (targetRow < 0 || targetRow >= rows || targetCol < 0 || targetCol >= columns) {
          continue;
        }

        // Apply influence to the target cell
        applyInfluenceToCell(targetRow, targetCol);
      }
    }
  }

  /**
   * Mirrors an influence grid horizontally for the blue player.
   * The mirroring is done across the columns (y-axis).
   *
   * @param original the original influence grid
   * @return the mirrored grid
   */
  private boolean[][] mirrorInfluenceGrid(boolean[][] original) {
    int rows = original.length;
    int cols = original[0].length;
    boolean[][] mirrored = new boolean[rows][cols];

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        mirrored[r][c] = original[r][cols - 1 - c];
      }
    }

    return mirrored;
  }

  /**
   * Applies influence to a specific cell based on game rules.
   * If the cell is empty, a pawn of the current player is added.
   * If the cell has pawns owned by the current player, the pawn count is increased (up to 3).
   * If the cell has pawns owned by the opponent, ownership is transferred to the current player.
   * If the cell has a card, no effect is applied.
   *
   * @param row the row of the cell
   * @param col the column of the cell
   */
  private void applyInfluenceToCell(int row, int col) {
    PawnsBoardCell<C> cell = board.get(row).get(col);

    try {
      switch (cell.getContent()) {
        case EMPTY:
          // Add a pawn of the current player
          cell.addPawn(currentPlayer);
          break;

        case PAWNS:
          if (cell.getOwner() == currentPlayer) {
            // Increase pawn count for current player (up to max 3)
            if (cell.getPawnCount() < 3) {
              cell.addPawn(currentPlayer);
            }
          } else {
            // Convert ownership of pawns to current player
            cell.changeOwnership(currentPlayer);
          }
          break;

        case CARD:
          // No effect on cells with cards
          break;

        default:
          // Should never happen
          throw new IllegalStateException("Unknown cell content type");
      }
    } catch (IllegalOwnerException e) {
      // This should not happen with the current implementation
      throw new IllegalStateException("Error applying influence: " + e.getMessage());
    }
  }
}