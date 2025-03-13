package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.cards.Card;

/**
 * Text-based view for the Pawns Board game.
 * Renders the game state as a string with the following format:
 * - Empty cells: "__" (two underscores)
 * - Cells with pawns: The number of pawns followed by lowercase owner letter (e.g., "1r", "3b")
 * - Cells with cards: The owner uppercase letter followed by the card's value (e.g., "R2", "B1")
 * 
 * Each row also shows the row scores for RED and BLUE players.
 *
 * @param <C> the type of Card used in the game
 */
public class PawnsBoardTextualView<C extends Card> implements PawnsBoardView {
  
  private final PawnsBoard<C, ?> model;
  
  /**
   * Constructs a text view with the specified model.
   *
   * @param model the game model to display
   * @throws IllegalArgumentException if model is null
   */
  public PawnsBoardTextualView(PawnsBoard<C, ?> model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
  }
  
  /**
   * Renders the current state of the game board as text.
   * Shows each cell's content and the row scores in the format specified by the assignment.
   * Empty cells are shown as "__".
   * Cells with pawns show the number of pawns (1-3).
   * Cells with cards show the owner (R/B) followed by the card's value (e.g., R3, B2).
   *
   * @return a string representation of the board
   */
  @Override
  public String toString() {
    if (!isGameStarted()) {
      return "Game has not been started";
    }
    
    int[] dimensions = model.getBoardDimensions();
    int rows = dimensions[0];
    int cols = dimensions[1];
    
    StringBuilder result = new StringBuilder();
    
    for (int r = 0; r < rows; r++) {
      // Add RED's row score at the start of the row
      int[] rowScores = model.getRowScores(r);
      result.append(rowScores[0]).append(" ");
      
      // Add cell contents
      for (int c = 0; c < cols; c++) {
        result.append(renderCell(r, c));
        // Add space between cells (but not after the last cell)
        if (c < cols - 1) {
          result.append(" ");
        }
      }
      
      // Add BLUE's row score at the end of the row
      result.append(" ").append(rowScores[1]);
      
      // Add a newline if not the last row
      if (r < rows - 1) {
        result.append("\n");
      }
    }
    
    return result.toString();
  }
  
  /**
   * Renders a comprehensive view of the game state including:
   * - Current game status (started, in progress, over)
   * - Current player
   * - Board state
   * - Game results if the game is over
   *
   * @return a complete representation of the game state
   */
  public String renderGameState() {
    if (!isGameStarted()) {
      return "Game has not been started";
    }
    
    StringBuilder gameState = new StringBuilder();
    
    // Add current player information
    PlayerColors currentPlayer = model.getCurrentPlayer();
    gameState.append("Current Player: ").append(currentPlayer).append("\n");
    
    // Add the board representation
    gameState.append(this).append("\n");
    
    // Add game results if the game is over
    if (model.isGameOver()) {
      gameState.append("Game is over\n");
      
      // Add score information
      int[] scores = model.getTotalScore();
      gameState.append("RED score: ").append(scores[0]).append("\n");
      gameState.append("BLUE score: ").append(scores[1]).append("\n");
      
      // Add winner information
      PlayerColors winner = model.getWinner();
      if (winner != null) {
        gameState.append("Winner: ").append(winner);
      } else {
        gameState.append("Game ended in a tie!");
      }
    }
    
    return gameState.toString();
  }
  
  /**
   * Renders the game state with a custom message header.
   * Useful for indicating specific events like game start or player actions.
   *
   * @param headerMessage the message to display as a header
   * @return a string with the header and game state
   */
  public String renderGameState(String headerMessage) {
    StringBuilder result = new StringBuilder();
    result.append("--- ").append(headerMessage).append(" ---\n");
    result.append(renderGameState());
    return result.toString();
  }
  
  /**
   * Renders a single cell based on its content.
   *
   * @param row the row of the cell
   * @param col the column of the cell
   * @return a string representation of the cell
   */
  private String renderCell(int row, int col) {
    try {
      CellContent content = model.getCellContent(row, col);
      
      switch (content) {
        case EMPTY:
          return "__";
          
        case PAWNS:
          int pawnCount = model.getPawnCount(row, col);
          PlayerColors owner = model.getCellOwner(row, col);
          String ownerChar = (owner == PlayerColors.RED) ? "r" : "b";
          return pawnCount + ownerChar;
          
        case CARD:
          PlayerColors cardOwner = model.getCellOwner(row, col);
          C card = model.getCardAtCell(row, col);
          int value = card != null ? card.getValue() : 0;
          String prefix = (cardOwner == PlayerColors.RED) ? "R" : "B";
          return prefix + value;
          
        default:
          return "__"; // Fallback
      }
    } catch (IllegalArgumentException | IllegalStateException e) {
      return "__"; // Error case
    }
  }
  
  /**
   * Checks if the game has been started.
   *
   * @return true if the game has been started, false otherwise
   */
  private boolean isGameStarted() {
    try {
      model.getBoardDimensions();
      return true;
    } catch (IllegalStateException e) {
      return false;
    }
  }
}